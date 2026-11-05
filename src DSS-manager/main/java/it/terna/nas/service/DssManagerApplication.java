package it.terna.nas.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.retry.annotation.EnableRetry;
import it.terna.kernel.configuration.JsonPropertyContextInitializer;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, 
		  						  HibernateJpaAutoConfiguration.class, 
		  						  JmsAutoConfiguration.class,
		  						  RedisAutoConfiguration.class})
@ComponentScan(basePackages   = {"it.terna.nas.service", 
		 						"it.terna.kernel" })
@EnableScheduling
@EnableAsync
@EnableJpaRepositories(basePackages = "it.terna.nas.service.dal.service.repository", transactionManagerRef = "tenantTransactionManager")
@EnableRetry
public class DssManagerApplication
{
	public static void main(String[] args)
	{
		new SpringApplicationBuilder(DssManagerApplication.class).initializers(new JsonPropertyContextInitializer(true)).run(args);
	}
}
