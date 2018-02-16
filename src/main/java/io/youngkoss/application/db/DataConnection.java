package io.youngkoss.application.db;

import java.sql.SQLException;

import io.youngkoss.application.enums.EnumShapeOperationQueries;

/**
 * @author ykoss
 *
 */
public class DataConnection {
	/**
	 * 
	 */
	private java.sql.Connection connection;

	/**
	 * @param connection
	 */
	public DataConnection(java.sql.Connection connection) {
		this.connection = connection;
	}

	public PreparedStatement prepareStatement(EnumShapeOperationQueries q) throws SQLException {

		return new PreparedStatement(connection.prepareStatement(q.getQuery()));
	}

	/**
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		connection.close();
	}

	/**
	 * @param b
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean b) throws SQLException {
		connection.setAutoCommit(b);
	}

	/**
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		connection.commit();
	}

	/**
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		connection.rollback();
	}
}
