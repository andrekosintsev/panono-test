package io.youngkoss.application.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ykoss
 *
 */
public class PsqlUtil {
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PsqlUtil.class.getName());

	/**
	 * @param stmt
	 * @param con
	 */
	public static void closeEverything(PreparedStatement stmt, DataConnection con) {
		closeEverything(null, stmt, con);
	}

	/**
	 * @param rs
	 * @param stmt
	 * @param con
	 */
	public static void closeEverything(ResultSet rs, PreparedStatement stmt, DataConnection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LOGGER.warn(e.getMessage(), e);
			}
		}
	}
}
