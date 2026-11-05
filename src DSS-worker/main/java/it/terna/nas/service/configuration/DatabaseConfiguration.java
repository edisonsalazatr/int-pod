package it.terna.nas.service.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.terna.kernel.source.connector.database.DataConnectionConfiguration;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.InterferenzeAsset;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class DatabaseConfiguration extends DataConnectionConfiguration
{

	public static final String	HIBERNATE_USED_DIALECT	= "org.hibernate.dialect.Oracle12cDialect";
	public static final String	DATASOURCE_KEY			= "eam";

	@Override
	public String[] getPackagesToScan()
	{
		return new String[] {
							 Asset.class.getPackage().getName(),
							 InterferenzeAsset.class.getPackage().getName()};
	}

	@Override
	public String getDatasourceKey()
	{
		return DATASOURCE_KEY;
	}

	@Override
	public String getHibernateDialect()
	{
		return HIBERNATE_USED_DIALECT;
	}
}
