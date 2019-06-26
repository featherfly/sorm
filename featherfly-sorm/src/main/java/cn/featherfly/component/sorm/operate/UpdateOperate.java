package cn.featherfly.component.sorm.operate;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.component.sorm.mapping.PropertyMapping;

/**
 * <p>
 * 更新操作
 * </p>
 * @param <T> 对象类型
 * @author zhongj
 * @since 1.0
 * @version 1.0
 */
public class UpdateOperate<T> extends AbstractExecuteOperate<T> {
		
	/**
	 * 使用给定数据源以及给定对象生成更新操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public UpdateOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	
	/**
	 * 使用给定数据源以及给定对象生成更新操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public UpdateOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initSql() {
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update ")
			.append(classMapping.getTableName())
			.append(" set ");
		int columnNum = 0;
		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
			updateSql.append(pm.getColumnName())
				.append(" = ? ,");
			columnNum++;
			propertyPositions.put(columnNum, pm.getFinalPropertyName());
		}
		if (columnNum > 0) {
			updateSql.deleteCharAt(updateSql.length() - 1);
		}
		int pkNum = 0;
		updateSql.append("where ");
		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
			if (pm.isPrimaryKey()) {
				if (pkNum > 0) {
					updateSql.append("and ");
				}
				updateSql.append(pm.getColumnName())
					.append(" = ? ");
				pkNum++;
				propertyPositions.put(columnNum + pkNum, pm.getFinalPropertyName());
			}
		}
		this.sql = updateSql.toString();
		logger.debug("sql: {}" , this.sql);
	}
}
