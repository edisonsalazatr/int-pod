package it.terna.nas.service.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@ConditionalOnProperty( value = "cache.config.latencyLoggerEnabled",
		havingValue		= "true", 
		matchIfMissing	= false )
public class RedisResponseTimeInterceptor  {
	
	public RedisResponseTimeInterceptor() {
		log.info(" - Redis latency logging enabled...");
	}
	
	@Pointcut("@annotation(org.springframework.cache.annotation.Cacheable)")
	public void cacheableMethod() {}
	
	@Around("cacheableMethod()")
	public Object measureLatency(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime = System.nanoTime();
		
		try {
			return joinPoint.proceed();
		} finally {
			double latencyMs = (System.nanoTime() - startTime) / 1_000_000;
			log.info(" - Method @Cacheable '{}' executed in {} ms", 
													joinPoint.getSignature().getName(),
													String.format("%.4f", latencyMs));
		}
	}
}
