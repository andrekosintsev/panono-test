package io.youngkoss.application.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ykoss
 *
 */
public class PreparedStatement {
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PreparedStatement.class.getName());

	/**
	 * 
	 */
	private java.sql.PreparedStatement ps;

	/**
	 * @param prepareStatement
	 */
	public PreparedStatement(java.sql.PreparedStatement prepareStatement) {
		this.ps = prepareStatement;

	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int[] executeBatch() throws SQLException {

		try {

			int[] executeBatch = ps.executeBatch();

			return executeBatch;
		} catch (Exception e) {

			LOGGER.error(e.getMessage(), e);
			throw e;

		} finally {

		}

	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public int executeUpdate() throws SQLException {

		int rows = 0;
		try {
			rows = ps.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} finally {
		}
		return rows;

	}

	/**
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException {

		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		} finally {
		}

		return rs;
	}

	/**
	 * @throws SQLException
	 */
	/**
	 * @throws SQLException
	 */
	public void execute() throws SQLException {

		try {

			ps.execute();

		} catch (SQLException e) {

			LOGGER.error(e.getMessage(), e);
			throw e;

		} finally {

		}

	}

	public String toString() {

		return ps.toString();
	}

	/**
	 * @throws SQLException
	 */
	public void addBatch() throws SQLException {

		ps.addBatch();
	}

	/**
	 * @param i
	 * @param v
	 * @throws SQLException
	 */
	public void setInt(int i, int v) throws SQLException {
		ps.setInt(i, v);

	}

	/**
	 * @param i
	 * @param v
	 * @throws SQLException
	 */
	public void setString(int i, String v) throws SQLException {

		ps.setString(i, v);

	}

	/**
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		ps.close();

	}

}
