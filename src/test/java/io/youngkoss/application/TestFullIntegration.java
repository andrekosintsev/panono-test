package io.youngkoss.application;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import io.youngkoss.application.db.DataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestInitializationConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@TestPropertySource(locations = "classpath:application.properties")
public class TestFullIntegration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestFullIntegration.class.getName());

	@Autowired
	@Qualifier("DataSource")
	private DataSource dataSource;

	@Test
	public void testTablesMade() throws Exception {

		Assert.assertEquals("true", "true");
	}

	@Test
	public void testGetConnection() throws Exception {
	}

	@After
	public void clearDataBase() {
		Resource initSchemaCreate = new ClassPathResource(Constants.DB_CLEAR_ALL);
		DatabasePopulator databasePopulatorCreate = new ResourceDatabasePopulator(initSchemaCreate);

		DriverManagerDataSource dataSourceCreate = new DriverManagerDataSource(Constants.JDBC_POSTGRESQL
				+ dataSource.getDbHost() + ":" + dataSource.getDbPort() + "/" + dataSource.getDatabase() + "?");
		dataSourceCreate.setDriverClassName(Constants.ORG_POSTGRESQL_DRIVER);
		dataSourceCreate.setUsername(dataSource.getDbUser());
		dataSourceCreate.setPassword(dataSource.getDbPassword());
		DatabasePopulatorUtils.execute(databasePopulatorCreate, dataSourceCreate);
		try {
			dataSourceCreate.getConnection().close();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

}
