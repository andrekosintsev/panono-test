package io.youngkoss.application.enums;

/**
 * @author ykoss
 *
 */
public enum EnumShapeOperationQueries {
	// @formatter:off
	INSERTSHAPE("INSERT INTO panono.shapes (id, coordinates) VALUES (?, ?)"), 
	UPDATESHAPE("UPDATE panono.shapes SET coordinates=? WHERE id=?;"),
	DELETESHAPE("DELETE FROM panono.shapes WHERE id=?;"),
	SELECTSHAPE("SELECT * FROM panono.shapes;"),
	INSERTOPERATION("INSERT INTO panono.operations (id, op_type , op_param) VALUES (?, ?, ?)"),
	SELECTCOORDINATESFROMSHAPES("SELECT coordinates FROM panono.shapes;"),
	SELECTOPERATIONS("SELECT operations.id, operations.op_type, operations.op_param, coordinates FROM panono.operations LEFT OUTER JOIN panono.shapes ON (operations.id = shapes.id);");
	// @formatter:on
	/**
	 * 
	 */
	private String query;

	/**
	 * @param query
	 */
	private EnumShapeOperationQueries(String query) {
		this.query = query;
	}

	/**
	 * @return
	 */
	public String getQuery() {
		return query;
	}
}
