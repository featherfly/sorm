package cn.featherfly.component.sorm.operate;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.component.sorm.mapping.PropertyMapping;

/**
 * <p>
 * 插入操作
 * </p>
 * @param <T> 对象类型
 * @author zhongj
 * @since 1.0
 * @version 1.0
 */
public class InsertOperate<T> extends AbstractExecuteOperate<T> {
	
	/**
	 * 使用给定数据源以及给定对象生成更新操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public InsertOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	
	/**
	 * 使用给定数据源以及给定对象生成更新操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public InsertOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initSql() {
		StringBuilder insertSql = new StringBuilder();
		insertSql.append("insert into ")
			.append(classMapping.getTableName())
			.append(" ( ");
		int columnNum = 0;
		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
			insertSql.append(pm.getColumnName())
				.append(",");
			columnNum++;
			propertyPositions.put(columnNum, pm.getFinalPropertyName());
		}
		if (columnNum > 0) {
			insertSql.deleteCharAt(insertSql.length() - 1);
		}
		insertSql.append(" ) values( ");
		for (int i = 0; i < columnNum; i++) {
			insertSql.append("?")
				.append(",");
		}
		if (columnNum > 0) {
			insertSql.deleteCharAt(insertSql.length() - 1);
		}
		insertSql.append(" )");
		this.sql = insertSql.toString();
		logger.debug("sql: {}" , this.sql);
	}
}
