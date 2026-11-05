package it.terna.nas.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import it.terna.kernel.configuration.JsonPropertyContextInitializer;
import it.terna.kernel.process.service.CleanUpService;

@SpringBootApplication(exclude = { HibernateJpaAutoConfiguration.class, JmsAutoConfiguration.class, RedisAutoConfiguration.class})
@ComponentScan(basePackages = { "it.terna.nas.service", "it.terna.kernel" }, excludeFilters={
		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=CleanUpService.class)})
@EnableScheduling
@EnableAsync
@EnableCaching
@EnableRetry
@EnableJpaRepositories(basePackages = "it.terna.nas.service.dal.service.repository", transactionManagerRef = "tenantTransactionManager")
public class EamIntegrationApplication
{
	public static void main(String[] args)
	{
		new SpringApplicationBuilder(EamIntegrationApplication.class).initializers(new JsonPropertyContextInitializer(true)).run(args);
	}
}
