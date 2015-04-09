package cn.featherfly.component.sorm.operate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.bean.BeanUtils;
import cn.featherfly.component.sorm.SimpleORMException;
import cn.featherfly.component.sorm.mapping.PropertyMapping;

/**
 * <p>
 * 根据ID读取操作
 * </p>
 * @param <T> 对象类型
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class GetOperate<T> extends AbstractQueryOperate<T>{
	
	/**
	 * 使用给定数据源以及给定对象生成读取操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public GetOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	/**
	 * 使用给定数据源以及给定对象生成读取操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public GetOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}

	/**
	 * <p>
	 * 返回对象的id值.如果传入对象为空或没有主键标示属性，则返回空.
	 * </p>
	 * @param entity 对象
	 * @return id值
	 */
	public Serializable getIdentity(T entity) {
		if (entity == null) {
			return null;
		}
		return (Serializable) BeanUtils.getProperty(entity, pkPm.getPropertyName());
	}

	/**
	 * <p>
	 * 返回指定ID的对象.
	 * </p>
	 * @param id 对象唯一标识
	 * @return 指定ID的对象
	 */
	public T get(final Serializable id) {; 
		if (id == null) {
			throw new SimpleORMException("get id 不能为null");
		}
		return jdbcTemplate.execute(new ConnectionCallback<T>() {
			@Override
			public T doInConnection(Connection conn) throws SQLException,
					DataAccessException {
				PreparedStatement prep = conn.prepareStatement(sql);
				setParameter(prep, id);
				ResultSet res = prep.executeQuery();
				int index = 0;
				T t = null;
				while (res.next()) {
					t = mapRow(res, index);
				}
				prep.close();
				return t;
			}
		});
		
//		ConnectionWrapper conn = JdbcUtils.getConnectionWrapper(dataSource);
//		PreparedStatementWrapper prep =	conn.prepareStatement(sql);
//		setParameter(prep, id);
//		ResultSetWrapper res = prep.executeQuery();
//		int index = 0;
//		T t = null;
//		while (res.next()) {
//			t = mapRow(res, index);
//		}
//		prep.close();
//		conn.close();
//		return t;
	}

//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected void initSql() {
//		StringBuilder getSql = new StringBuilder();		
//		getSql.append(getSelectSql())
//			.append(" where ");
//		int columnNum = 0;
//		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
//			if (pm.isPrimaryKey()) {
//				if (columnNum > 0) {
//					getSql.append("and ");
//				}
//				getSql.append(pm.getColumnName())
//					.append(" = ? ");
//				columnNum++;
//				propertyPositions.put(columnNum, pm.getFinalPropertyName());
//				// 设置主键值
//				pkPm = pm;
//			}
//		}
//		this.sql = getSql.toString();
//		LOGGER.debug("sql: {}" , this.sql);
//	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String initCondition() {
		StringBuilder condition = new StringBuilder();
		int columnNum = 0;
		for (PropertyMapping pm : classMapping.getPropertyMappings()) {
			if (pm.isPrimaryKey()) {
				if (columnNum > 0) {
					condition.append("and ");
				}
				condition.append(pm.getColumnName())
					.append(" = ? ");
				columnNum++;
				propertyPositions.put(columnNum, pm.getFinalPropertyName());
				// 设置主键值
				pkPm = pm;
			}
		}
		logger.debug("condition -> " + condition.toString());
		return condition.toString();
	}
	
	private PropertyMapping pkPm;

}
