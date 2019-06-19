package cn.featherfly.component.sorm.operate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.bean.BeanUtils;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.component.sorm.mapping.PropertyMapping;

/**
 * <p>
 * 合并操作
 * </p>
 * @param <T> 对象类型
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class MergeOperate<T> extends AbstractOperate<T> {
	/**
	 * 使用给定数据源以及给定对象生成更新操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public MergeOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	
	/**
	 * 使用给定数据源以及给定对象生成更新操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public MergeOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}

	/**
	 * <p>
	 * 合并操作，将传入对象的非空字段更新进数据库（忽略null）.
	 * </p>
	 * @param entity 对象
	 * @return 操作影响的数据行数
	 */
	public int execute(final T entity) {
		return jdbcTemplate.execute(new ConnectionCallback<Integer>() {
			@Override
			public Integer doInConnection(Connection conn) throws SQLException,
					DataAccessException {
				Map<Integer, String> propertyPositions = new HashMap<Integer, String>();
				String sql = getDynamicSql(entity, propertyPositions);
				logger.debug("execute sql: {}", sql);
				PreparedStatement prep =	conn.prepareStatement(sql);
				for (Entry<Integer, String> propertyPosition : propertyPositions.entrySet()) {
					JdbcUtils.setParameter(prep, propertyPosition.getKey(),
							BeanUtils.getProperty(entity, propertyPosition.getValue()));
				}
				int result = prep.executeUpdate();
				prep.close();
				return result;
			}
		});
	}

	private String getDynamicSql(T entity, Map<Integer, String> propertyPositions) {
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update ")
			.append(classMapping.getTableName())
			.append(" set ");
		int columnNum = 0;
		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
			// 如果未空忽略
			if (BeanUtils.getProperty(entity, pm.getPropertyName()) == null) {
				continue;
			}
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
		return updateSql.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initSql() {
		// 不需要实现
	}
}
