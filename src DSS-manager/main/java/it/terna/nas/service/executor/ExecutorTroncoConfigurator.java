package it.terna.nas.service.executor;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.utility.Assorted;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Initialize a ExecutorService only if the property 'executorSendKafkaAssetNumThreadPool' is configured
 * </br>
 * if the property 'executorThreadPool' is set to 0 or 1 Runtime.getRuntime().availableProcessors() will be used
 *
 */

@Slf4j
@Service
@ConditionalOnProperty( value			= "executorTroncoThreadPool",
						matchIfMissing	= false )
public class ExecutorTroncoConfigurator
{
	@Value("${executorTroncoThreadPool}")
	private Integer			maxThreadPoolGlobal;

	@Getter
	private ExecutorService	executorService	= null;


	@PostConstruct
	private void init ()
	{
		log.info("  - ExecutorTroncoConfigurator - initializing threadpoolGlobal of [{}]", maxThreadPoolGlobal);
		// configure ExecutorService for parallelism to use for file processing with pre-configured pool size, if configured value is less than 1, the number of available processor value will be used
		executorService = Executors.newFixedThreadPool(Assorted.isValid(maxThreadPoolGlobal) ? maxThreadPoolGlobal 
		                                                                               : Runtime.getRuntime().availableProcessors());
	}

	@PreDestroy
	private void destroy ()
	{
		log.info("  - ExecutorTroncoConfigurator - shutting down threadpoolGlobal");
		executorService.shutdown();
	}
}
