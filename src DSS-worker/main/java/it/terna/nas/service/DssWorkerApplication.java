package it.terna.nas.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import it.terna.kernel.configuration.JsonPropertyContextInitializer;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, 
		  						  HibernateJpaAutoConfiguration.class, 
		  						  JmsAutoConfiguration.class,
		  						  RedisAutoConfiguration.class})
@ComponentScan(basePackages   = {"it.terna.nas.service", 
		 						"it.terna.kernel" })
@EnableMongoRepositories(basePackages     = {"it.terna.nas.service.dal.repository",
 											 "it.terna.kernel.process.status.handler.service.dal.repository"},
                                            mongoTemplateRef = "mongoConnector")

@EnableScheduling
@EnableAsync
@EnableCaching
@EnableJpaRepositories(basePackages = "it.terna.nas.service.dal.repository", transactionManagerRef = "tenantTransactionManager")
@EnableRetry
public class DssWorkerApplication
{
	public static void main(String[] args)
	{
		new SpringApplicationBuilder(DssWorkerApplication.class).initializers(new JsonPropertyContextInitializer(true)).run(args);
	}
}
