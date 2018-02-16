package io.youngkoss.application.pjo;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.youngkoss.application.db.DataConnection;
import io.youngkoss.application.db.DataSource;
import io.youngkoss.application.db.PreparedStatement;
import io.youngkoss.application.db.PsqlUtil;
import io.youngkoss.application.enums.EnumShapeOperationQueries;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author ykoss
 *
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Shape implements Serializable {
	private static final Logger LOGGER = LoggerFactory.getLogger(Shape.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
	/**
	 * 
	 */
	private String uuid;
	/**
	 * 
	 */
	private String coordinates;

	/**
	 * @param dataSource
	 * @param objects
	 * @throws IOException
	 */
	public static void initializeShapes(DataSource dataSource, List<Shape> objects) throws IOException {

		PreparedStatement stmt = null;

		DataConnection conn = null;
		Iterator<Shape> iteratorObjects = objects.iterator();

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(EnumShapeOperationQueries.INSERTSHAPE);
			while (iteratorObjects.hasNext()) {
				Shape shape = iteratorObjects.next();
				stmt.setString(1, shape.getUuid());
				stmt.setString(2, shape.getCoordinates());
				stmt.addBatch();
			}
			int[] numOfUpdates = stmt.executeBatch();
			int index = 0;
			for (int update : numOfUpdates) {
				if (update == -2) {
					LOGGER.warn("Execution " + index + ": unknown number of rows updated");
				} else {
					LOGGER.warn("Execution " + index + " successful: " + update + " rows updated");
				}
				index++;
			}
			conn.commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

		} finally {

			PsqlUtil.closeEverything(null, stmt, conn);
		}

	}

	public static void updateShapes(DataSource dataSource, List<Shape> objects) throws IOException {

		PreparedStatement stmt = null;

		DataConnection conn = null;
		Iterator<Shape> iteratorObjects = objects.iterator();

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(EnumShapeOperationQueries.UPDATESHAPE);
			while (iteratorObjects.hasNext()) {
				Shape shape = iteratorObjects.next();
				stmt.setString(1, shape.getUuid());
				stmt.setString(2, shape.getCoordinates());
				stmt.addBatch();
			}
			int[] numOfUpdates = stmt.executeBatch();
			int index = 0;
			for (int update : numOfUpdates) {
				if (update == -2) {
					LOGGER.warn("Execution " + index + ": unknown number of rows updated");
				} else {
					LOGGER.warn("Execution " + index + " successful: " + update + " rows updated");
				}
				index++;
			}
			conn.commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

		} finally {

			PsqlUtil.closeEverything(null, stmt, conn);
		}

	}

	public static void deleteShapes(DataSource dataSource, List<Shape> objects) throws IOException {

		PreparedStatement stmt = null;

		DataConnection conn = null;
		Iterator<Shape> iteratorObjects = objects.iterator();

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(EnumShapeOperationQueries.DELETESHAPE);
			while (iteratorObjects.hasNext()) {
				Shape shape = iteratorObjects.next();
				stmt.setString(1, shape.getUuid());
				stmt.addBatch();
			}
			int[] numOfUpdates = stmt.executeBatch();
			int index = 0;
			for (int update : numOfUpdates) {
				if (update == -2) {
					LOGGER.warn("Execution " + index + ": unknown number of rows updated");
				} else {
					LOGGER.warn("Execution " + index + " successful: " + update + " rows updated");
				}
				index++;
			}
			conn.commit();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

		} finally {

			PsqlUtil.closeEverything(null, stmt, conn);
		}

	}

	public static List<Shape> selectShapesToPrint(DataSource dataSource) throws IOException {
		List<Shape> shapes = new ArrayList<Shape>();
		PreparedStatement stmt = null;

		DataConnection conn = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(EnumShapeOperationQueries.SELECTSHAPE);
			rs = stmt.executeQuery();
			while (rs.next()) {
				shapes.add(new Shape(rs.getString(1), rs.getString(2)));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

		} finally {

			PsqlUtil.closeEverything(rs, stmt, conn);
		}
		return shapes;

	}

	public static List<Integer> getAnglesPerFigure(DataSource dataSource) throws IOException {
		List<Integer> arrayOfAngles = null;

		PreparedStatement stmt = null;
		DataConnection conn = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(EnumShapeOperationQueries.SELECTCOORDINATESFROMSHAPES);

			rs = stmt.executeQuery();

			while (rs.next()) {
				String coordinates = rs.getString(1);
				if ((null != coordinates) && !coordinates.isEmpty()) {
					if (null == arrayOfAngles) {
						arrayOfAngles = new ArrayList<Integer>();
					}
					arrayOfAngles.add(coordinates.split("\\;").length);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

		} finally {

			PsqlUtil.closeEverything(null, stmt, conn);
		}
		return arrayOfAngles;
	}

}
