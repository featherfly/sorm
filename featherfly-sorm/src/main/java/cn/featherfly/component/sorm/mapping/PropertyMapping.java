package cn.featherfly.component.sorm.mapping;

import cn.featherfly.common.lang.StringUtils;

/**
 * <p>
 * 属性与列的映射对象
 * </p>
 *
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class PropertyMapping {
	
	/**
	 */
	public PropertyMapping() {
		
	}
	
	private String innerPropertyName;
	
	private Class<?> innerPropertyType;

	private String propertyName;

	private String columnName;

	private Class<?> propertyType;

	private boolean primaryKey;

	/**
	 * @return 返回propertyType
	 */
	public Class<?> getPropertyType() {
		return propertyType;
	}

	/**
	 * @param propertyType 设置propertyType
	 */
	public void setPropertyType(Class<?> propertyType) {
		this.propertyType = propertyType;
	}

	/**
	 * @return 返回primaryKey
	 */
	public boolean isPrimaryKey() {
		return primaryKey;
	}

	/**
	 * @param primaryKey 设置primaryKey
	 */
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * @return 返回propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName
	 *            设置propertyName
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return 返回columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName
	 *            设置columnName
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName.toUpperCase();
	}

	/**
	 * 返回innerPropertyName
	 * @return innerPropertyName
	 */
	public String getInnerPropertyName() {
		return innerPropertyName;
	}

	/**
	 * 设置innerPropertyName
	 * @param innerPropertyName innerPropertyName
	 */
	public void setInnerPropertyName(String innerPropertyName) {
		this.innerPropertyName = innerPropertyName;
	}

	/**
	 * 返回innerPropertyType
	 * @return innerPropertyType
	 */
	public Class<?> getInnerPropertyType() {
		return innerPropertyType;
	}

	/**
	 * 设置innerPropertyType
	 * @param innerPropertyType innerPropertyType
	 */
	public void setInnerPropertyType(Class<?> innerPropertyType) {
		this.innerPropertyType = innerPropertyType;
	}

	/**
	 * 返回最终属性名称（如果是嵌套属性就是XX.YY）.
	 * @return 最终属性名称
	 */
	public String getFinalPropertyName() {
		if (StringUtils.isNotEmpty(getInnerPropertyName())) {
			return getPropertyName() + "." + getInnerPropertyName();
		} else {
			return getPropertyName();
		}
	}
	/**
	 * 返回最终属性类型（如果是嵌套属性就是XX.YY的类型）.
	 * @return 最终属性类型
	 */
	public Class<?> getFinalPropertyType() {
		if (StringUtils.isNotEmpty(getInnerPropertyName())) {
			return getInnerPropertyType();
		} else {
			return getPropertyType();
		}
	}
}
