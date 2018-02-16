package io.youngkoss.application.enums;

/**
 * @author ykoss
 *
 */
public enum EnumOperation {

	DELETE_SHAPE("delete-shape"), CREATE_SHAPE("create-shape"), DELETE_POINT("delete-point"), ADD_POINT("add-point");
	/**
	 * 
	 */
	private String name;

	/**
	 * @param name
	 */
	private EnumOperation(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getQuery() {
		return name;
	}

	/**
	 * @param string
	 * @return
	 */
	public static EnumOperation enumFromString(String string) {
		for (EnumOperation b : EnumOperation.values()) {
			if (b.name.equalsIgnoreCase(string)) {
				return b;
			}
		}
		return null;
	}

}
