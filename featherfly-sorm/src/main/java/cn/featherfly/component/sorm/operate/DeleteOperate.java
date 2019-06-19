package cn.featherfly.component.sorm.operate;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.component.sorm.mapping.PropertyMapping;

/**
 * <p>
 * 删除操作
 * </p>
 * @param <T> 对象类型
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class DeleteOperate<T> extends AbstractExecuteOperate<T>{
	/**
	 * 使用给定数据源以及给定对象生成删除操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public DeleteOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	/**
	 * 使用给定数据源以及给定对象生成删除操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public DeleteOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initSql() {
		StringBuilder deleteSql = new StringBuilder();
		deleteSql.append("delete from ")
			.append(classMapping.getTableName())
			.append(" where ");
		int columnNum = 0;
		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
			if (pm.isPrimaryKey()) {
				if (columnNum > 0) {
					deleteSql.append("and ");
				}
				deleteSql.append(pm.getColumnName())
					.append(" = ? ");
				columnNum++;
				propertyPositions.put(columnNum, pm.getFinalPropertyName());
			}
		}
		this.sql = deleteSql.toString();
		logger.debug("sql: {}" , this.sql);
		
	}
}
