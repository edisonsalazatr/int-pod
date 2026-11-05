package it.terna.nas.service.material.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultPolicyConverterResolver implements PolicyConverterResolver {


    private final Map<ClassPair<?, ?>, Converter<?, ?>> converters = new HashMap<>();

    @Autowired
    public DefaultPolicyConverterResolver(List<Converter<?, ?>> converterBeans) {
        for (Converter<?, ?> converter : converterBeans) {
            ClassPair<?, ?> key = getClassPair(converter);
            if(key != null 
            		&& key.getSource() != null 
            		&& key.getTarget() != null) {
                converters.put(key, converter);
                log.debug("Converter registered: {} -> {} with class {}",
                          key.getSource().getSimpleName(), key.getTarget().getSimpleName(), converter.getClass().getName());
            } else {
                log.warn("Cannot resolve generic types for converter: {}. Ignored.", converter.getClass().getName());
            }
        }
        log.info("Registered {} direct converters at start.", converters.size());
    }

    @Override
    public <S> FromStep<S> from(S source) {
        Objects.requireNonNull(source, "Source obeject cannot be null");
        return new FromStep<S>() {
            @Override
            public <T> T to(Class<T> targetType) {
                Objects.requireNonNull(targetType, "Target type cannot be null");

                log.debug("Requested Convertion from {} to {}", source.getClass().getSimpleName(), targetType.getSimpleName());

                // Primo controllo: se il tipo sorgente è già assegnabile al tipo di destinazione,
                // non serve alcuna conversione esplicita.
                if (targetType.isAssignableFrom(source.getClass())) {
                    log.debug("Source {} is already assignable to {}. No conversion is needed.",
                              source.getClass().getSimpleName(), targetType.getSimpleName());
                    @SuppressWarnings("unchecked")
                    T result = (T) source; // Casting diretto
                    return result;
                }

                // Se non è direttamente assegnabile, cerchiamo un percorso di conversione a catena
                List<Converter<?, ?>> path = findConversionPath(source.getClass(), targetType);

                if (path.isEmpty()) {
                    log.error("No suitable converters or conversion path found for {} to {}.", 
                    														source.getClass().getName(),
                    														targetType.getName());
                    throw new ConversionException(String.format("No suitable converter or conversion path found for %s to %s.",
                            												source.getClass().getName(),
                            												targetType.getName()));
                }

                log.debug("Conversion path found: {}", path.stream()
                										   .map(Object::toString)
                										   .collect(Collectors.joining(" -> ")));
                Object currentResult = source;
                for (Converter<?, ?> converter : path) {
                    try {
                        @SuppressWarnings("unchecked")
                        Converter<Object, Object> typedConverter = (Converter<Object, Object>) converter;
                        log.trace("Converter {} to {}", typedConverter.getClass().getSimpleName(), currentResult);
                        currentResult = typedConverter.convert(currentResult);
                    } catch (ClassCastException e) {
                        log.error("Internal casting error during chained conversion {}: {}", converter.getClass().getSimpleName(), e.getMessage(), e);
                        throw new ConversionException("Internal casting error during chained conversion.", e);
                    }
                }
                log.debug("Completed conversion from {} to {}. Final result: {}", 
                															source.getClass().getSimpleName(), 
                															targetType.getSimpleName(),
                															currentResult);
                return targetType.cast(currentResult);
            }
        };
    }

    /**
     * Trova il percorso di conversione più breve (lista di convertitori)
     * dalla sorgente al tipo di destinazione utilizzando una BFS.
     */
    private List<Converter<?, ?>> findConversionPath(Class<?> sourceClass, Class<?> targetClass) {
        // Il check di assegnabilità diretta è stato spostato nel metodo 'to()'
    	log.debug("Find conversion path BFS from {} to {}", sourceClass.getSimpleName(), 
        													targetClass.getSimpleName());

    	Map<Class<?>, ConversionNode> visited = new HashMap<>();
    	Queue<ConversionNode> queue = new LinkedList<>();

        ConversionNode initialNode = new ConversionNode(sourceClass, null, null);
        visited.put(sourceClass, initialNode);
        queue.offer(initialNode);

        while (!queue.isEmpty()) {
            ConversionNode currentNode = queue.poll();
            Class<?> currentType = currentNode.getType();

            // Se abbiamo raggiunto un tipo che è assegnabile al target finale
            // significa che abbiamo trovato un percorso valido.
            if (targetClass.isAssignableFrom(currentType)) {
                 log.debug("A conversion path ending with a type compatible with {} was found.", targetClass.getSimpleName());
                 return reconstructPath(currentNode);
            }

            // Esplora i convertitori disponibili da currentType
            for (Map.Entry<ClassPair<?, ?>, Converter<?, ?>> entry : converters.entrySet()) {
                ClassPair<?, ?> registeredPair = entry.getKey();
                Class<?> registeredSourceType = registeredPair.getSource();
                Class<?> registeredTargetType = registeredPair.getTarget();
                Converter<?, ?> converter = entry.getValue();

                // Se il convertitore può prendere 'currentType' come input...
                if (registeredSourceType.isAssignableFrom(currentType)) {
                    // E se non abbiamo ancora visitato questo targetType (per trovare il percorso più breve)
                    if (!visited.containsKey(registeredTargetType)) {
                        ConversionNode newNode = new ConversionNode(registeredTargetType, currentNode, converter);
                        visited.put(registeredTargetType, newNode);
                        queue.offer(newNode);
                        log.trace("Node explored: {} --[{}]--> {}", 
                        									currentType.getSimpleName(), 
                        									converter.getClass().getSimpleName(), 
                        									registeredTargetType.getSimpleName());
                    }
                }
            }
        }

        log.debug("No conversion path fourd from {} to {}.", sourceClass.getSimpleName(), 
        													 targetClass.getSimpleName());
        return Collections.emptyList(); // No path found.
    }

    /**
     *  Ricostruisce il percorso dal nodo finale risalendo i predecessori
     */
    private List<Converter<?, ?>> reconstructPath(ConversionNode targetNode) {
        LinkedList<Converter<?, ?>> path = new LinkedList<>();
        ConversionNode current = targetNode;

        while (current != null && current.getConverter() != null) {
            path.addFirst(current.getConverter());
            current = current.getPredecessor();
        }
        return path;
    }

    /**
     * Classe interna per la ricerca del percorso (BFS).
     * Rappresenta un nodo nel grafo dei tipi.
     */
    private static class ConversionNode {
        private final Class<?> type;
        private final ConversionNode predecessor;
        private final Converter<?, ?> converter; // Il convertitore usato per arrivare a questo tipo

        public ConversionNode(Class<?> type, ConversionNode predecessor, Converter<?, ?> converter) {
            this.type = type;
            this.predecessor = predecessor;
            this.converter = converter;
        }

        public Class<?> getType() {
            return type;
        }

        public ConversionNode getPredecessor() {
            return predecessor;
        }

        public Converter<?, ?> getConverter() {
            return converter;
        }
    }

    private ClassPair<?, ?> getClassPair(Converter<?, ?> converter) {
        Class<?>[] types = GenericTypeResolver.resolveTypeArguments(converter.getClass(), Converter.class);
        if (types == null || types.length < 2 || types[0] == null || types[1] == null) {
            return null;
        }
        return new ClassPair<>(types[0], types[1]);
    }

    private static class ClassPair<S, T> {
        private final Class<?> source;
        private final Class<?> target;

        ClassPair(Class<?> source, Class<?> target) {
            this.source = source;
            this.target = target;
        }

        public Class<?> getSource() {
            return source;
        }

        public Class<?> getTarget() {
            return target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ClassPair<?, ?> other)) return false;
            return source.equals(other.source) && target.equals(other.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, target);
        }

        @Override
        public String toString() {
            return "ClassPair[" + source.getName() + " -> " + target.getName() + "]";
        }
    }
    
    /**
     * Custom exception for conversion issues
     */
    class ConversionException extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

		public ConversionException(String message) {
            super(message);
        }

        public ConversionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
