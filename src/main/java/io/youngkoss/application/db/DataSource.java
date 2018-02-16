package io.youngkoss.application.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author ykoss
 *
 */
public class DataSource {
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DataSource.class.getName());

	/**
	 * 
	 */
	private static DataSource datasource;

	/**
	 * 
	 */
	private ComboPooledDataSource cpds;

	private String dbHost;
	private String dbPort;
	private String database;
	private String dbUser;
	private String dbPassword;
	private String dbCrypt;
	private String prefix;

	/**
	 * @param configFile
	 * @throws Exception
	 */
	private DataSource(String configFile) throws Exception {
		try {
			Properties properties = new Properties();
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(configFile));
			properties.load(stream);
			stream.close();
			configure(properties);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param properties
	 */
	private DataSource(Properties properties) {
		try {
			configure(properties);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param properties
	 */
	private void configure(Properties properties) {

		dbHost = properties.getProperty("dbhost");
		dbPort = properties.getProperty("dbport");
		database = properties.getProperty("database");
		dbUser = properties.getProperty("dbuser");
		dbPassword = properties.getProperty("dbpassword");
		dbCrypt = properties.getProperty("dbcrypt");
		prefix = properties.getProperty("prefix");

		cpds = new ComboPooledDataSource();
		cpds.setJdbcUrl("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + database + "?");
		cpds.setUser(dbUser);
		cpds.setPassword(dbPassword);
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static DataSource getInstance() throws Exception {
		if (datasource == null) {
			datasource = new DataSource("application.properties");
			return datasource;
		} else {
			return datasource;
		}
	}

	/**
	 * @param properties
	 * @return
	 * @throws SQLException
	 */
	public static DataSource getInstance(Properties properties) throws SQLException {
		if (datasource == null) {
			datasource = new DataSource(properties);
			return datasource;
		} else {
			return datasource;
		}
	}

	/**
	 * @param configFile
	 * @return
	 * @throws Exception
	 */
	public static DataSource getInstance(String configFile) throws Exception {
		if (datasource == null) {
			datasource = new DataSource(configFile);
			return datasource;
		} else {
			return datasource;
		}
	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public DataConnection getConnection() throws SQLException {
		return new DataConnection(this.cpds.getConnection());
	}

	/**
	 * @return
	 */
	public ComboPooledDataSource getCpds() {
		return cpds;
	}

	/**
	 * @return
	 */
	public String getPrefix() {
		if (prefix == null) {
			return "test";
		} else {
			return prefix;
		}
	}

	/**
	 * @return
	 */
	public String getDbCrypt() {
		return dbCrypt;
	}

	/**
	 * @param dbCrypt
	 */
	public void setDbCrypt(String dbCrypt) {
		this.dbCrypt = dbCrypt;
	}

	/**
	 * 
	 */
	public void close() {
		cpds.close();
		datasource = null;
	}

	/**
	 * @return
	 */
	public String getDbHost() {
		return dbHost;
	}

	/**
	 * @return
	 */
	public String getDbPort() {
		return dbPort;
	}

	/**
	 * @return
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return
	 */
	public String getDbUser() {
		return dbUser;
	}

	/**
	 * @return
	 */
	public String getDbPassword() {
		return dbPassword;
	}

}
