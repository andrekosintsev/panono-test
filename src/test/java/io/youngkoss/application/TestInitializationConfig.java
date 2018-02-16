package io.youngkoss.application;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import io.youngkoss.application.db.DataSource;

/**
 * @author ykoss
 *
 */
@Configuration
public class TestInitializationConfig {

	@Value("${database}")
	private String database;

	@Value("${dbuser}")
	private String dbuser;

	@Value("${dbpassword}")
	private String dbpassword;

	@Value("${dbcrypt}")
	private String dbcrypt;

	@Value("${prefix}")
	private String prefix;

	@Value("${dbhost}")
	private String dbhost;

	@Value("${dbport}")
	private String dbport;

	@Bean(name = "DataSource")
	public DataSource getFinanceDataSource() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("dbhost", dbhost);
		properties.setProperty("dbport", dbport);
		properties.setProperty("database", database);
		properties.setProperty("dbuser", dbuser);
		properties.setProperty("dbpassword", dbpassword);
		properties.setProperty("dbcrypt", dbcrypt);
		properties.setProperty("prefix", prefix);

		DataSource instance = DataSource.getInstance(properties);
		return instance;
	}

	@Bean(name = "initializeShema")
	public String initializeSchema() throws Exception {
		Resource initSchemaCreate = new ClassPathResource(Constants.DB_SCHEMA_SQL);
		DatabasePopulator databasePopulatorCreate = new ResourceDatabasePopulator(initSchemaCreate);

		DriverManagerDataSource dataSourceCreate = new DriverManagerDataSource(
				Constants.JDBC_POSTGRESQL + dbhost + ":" + dbport + "/" + database + "?");
		dataSourceCreate.setDriverClassName(Constants.ORG_POSTGRESQL_DRIVER);
		dataSourceCreate.setUsername(dbuser);
		dataSourceCreate.setPassword(dbpassword);
		DatabasePopulatorUtils.execute(databasePopulatorCreate, dataSourceCreate);
		dataSourceCreate.getConnection().close();
		return "Initialized";
	}

}
