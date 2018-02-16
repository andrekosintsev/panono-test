package io.youngkoss.application.pjo;

import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.youngkoss.application.db.DataConnection;
import io.youngkoss.application.db.DataSource;
import io.youngkoss.application.db.PreparedStatement;
import io.youngkoss.application.db.PsqlUtil;
import io.youngkoss.application.enums.EnumOperation;
import io.youngkoss.application.enums.EnumShapeOperationQueries;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
public class Operation implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Operation.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;
	/**
	 * 
	 */
	private String command;
	/**
	 * 
	 */
	private String uuid;
	/**
	 * 
	 */
	private String params;

	/**
	 * @param dataSource
	 * @param objects
	 * @throws IOException
	 */
	public static void initializeOperations(DataSource dataSource, List<Operation> objects) throws IOException {

		PreparedStatement stmt = null;

		DataConnection conn = null;
		Iterator<Operation> iteratorObjects = objects.iterator();

		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement(EnumShapeOperationQueries.INSERTOPERATION);
			while (iteratorObjects.hasNext()) {
				Operation operation = iteratorObjects.next();
				stmt.setString(1, operation.getUuid());
				stmt.setString(2, operation.getCommand());
				stmt.setString(3, operation.getParams());
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

	/**
	 * @param dataSource
	 * @throws IOException
	 */
	public static Map<EnumOperation, List<Shape>> invokeOperation(DataSource dataSource) throws IOException {
		Map<EnumOperation, List<Shape>> crudMap = new HashMap<EnumOperation, List<Shape>>();

		PreparedStatement stmt = null;
		DataConnection conn = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(EnumShapeOperationQueries.SELECTOPERATIONS);

			rs = stmt.executeQuery();

			while (rs.next()) {
				String operation = rs.getString(2);
				if ((null != operation) && !operation.isEmpty()) {
					switch (EnumOperation.enumFromString(operation)) {

					case ADD_POINT:
						String coordinate = rs.getString(4);
						coordinate = coordinate.concat(";" + rs.getString(3));
						if (crudMap.get(EnumOperation.ADD_POINT) == null) {
							List<Shape> shapes = new ArrayList<Shape>();
							shapes.add(new Shape(rs.getString(1), coordinate));
							crudMap.put(EnumOperation.ADD_POINT, shapes);
						} else {
							crudMap.get(EnumOperation.ADD_POINT).add(new Shape(rs.getString(1), coordinate));
						}

						break;
					case CREATE_SHAPE:
						if (crudMap.get(EnumOperation.CREATE_SHAPE) == null) {
							List<Shape> shapes = new ArrayList<Shape>();
							shapes.add(new Shape(rs.getString(1), rs.getString(3)));
							crudMap.put(EnumOperation.CREATE_SHAPE, shapes);
						} else {
							crudMap.get(EnumOperation.CREATE_SHAPE).add(new Shape(rs.getString(1), rs.getString(3)));
						}
						break;
					case DELETE_POINT:
						String initialCoordinatesString = rs.getString(4);
						String result = "";
						String[] initialCoordinates = initialCoordinatesString.split("\\;");
						String indexString = rs.getString(3);
						int indexSkip = Integer.valueOf(indexString);
						int index = 0;
						for (String point : initialCoordinates) {
							if (index != 0) {
								result = result.concat(";");
							}
							if (index != indexSkip) {
								result = result.concat(point);
							}
							index++;
						}
						if (crudMap.get(EnumOperation.DELETE_POINT) == null) {
							List<Shape> shapes = new ArrayList<Shape>();
							shapes.add(new Shape(rs.getString(1), result));
							crudMap.put(EnumOperation.DELETE_POINT, shapes);
						} else {
							crudMap.get(EnumOperation.DELETE_POINT).add(new Shape(rs.getString(1), result));
						}
						break;
					case DELETE_SHAPE:
						if (crudMap.get(EnumOperation.DELETE_SHAPE) == null) {
							List<Shape> shapes = new ArrayList<Shape>();
							shapes.add(new Shape(rs.getString(1), rs.getString(4)));
							crudMap.put(EnumOperation.DELETE_SHAPE, shapes);
						} else {
							crudMap.get(EnumOperation.DELETE_SHAPE).add(new Shape(rs.getString(1), rs.getString(4)));
						}
						break;
					default:
						break;
					}

				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);

		} finally {

			PsqlUtil.closeEverything(rs, stmt, conn);
		}
		return crudMap;
	}
}
