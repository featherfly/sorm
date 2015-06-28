
package cn.featherfly.component.sorm.operate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.bean.BeanUtils;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.component.sorm.SimpleORMException;
import cn.featherfly.component.sorm.mapping.PropertyMapping;

/**
 * <p>
 * 数据库操作的抽象类
 * </p>
 * @param <T> 对象类型
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public abstract class AbstractExecuteOperate<T> extends AbstractOperate<T>{

	/**
	 * 使用给定数据源以及给定对象生成其相应的操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public AbstractExecuteOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	/**
	 * 使用给定数据源以及给定对象生成其相应的操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public AbstractExecuteOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}

	/**
	 * <p>
	 * 执行操作.
	 * 操作的类型由具体子类构造的不同SQL来区分.
	 * </p>
	 * @param entity 对象
	 * @return 操作影响的数据行数
	 */
	public int execute(final T entity) {
		return jdbcTemplate.execute(new ConnectionCallback<Integer>() {
			@Override
			public Integer doInConnection(Connection con) throws SQLException,
					DataAccessException {
				PreparedStatement prep = null;
				if (generatedKey) {
					prep = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				} else {
					prep = con.prepareStatement(sql);
				}
				setParameter(prep, entity);
				logger.debug("execute sql: {}", sql);
				int result = prep.executeUpdate();
				if (generatedKey) {
					PropertyMapping pm = null;
					int i = 0;
					for (PropertyMapping propertyMapping : classMapping.getPropertyMappings()) {
						if (propertyMapping.isPrimaryKey()) {
							pm = propertyMapping;
							i++;
						}
					}
					if (i > 1) {
					    throw new SimpleORMException("#generate.multi.pk");
//						throw new SimpleORMException("出现多个自动生成的键值，" +
//								"目前不支持多个自动生成的键值，" +
//								"你可以选择关闭自动生成键值setGeneratedKey(false)");
					}
					ResultSet res = prep.getGeneratedKeys();
					StringBuilder msg = null;
					if (logger.isDebugEnabled()) {
						msg = new StringBuilder("自动生成的键值 : ");
					}
					if (res.next()) {
						Object value = JdbcUtils.getResultSetValue(res, 1, pm.getPropertyType());
						if (logger.isDebugEnabled()) {
							msg.append(" ")
								.append(value)
								.append(", ");
						}
						BeanUtils.setProperty(entity, pm.getPropertyName(), value);
					}
					if (logger.isDebugEnabled()) {
						logger.debug(msg.toString());
					}
				}
				prep.close();
				return result;
			}
		});
	}

	// ********************************************************************
	//	property
	// ********************************************************************

	private boolean generatedKey;

	/**
	 * @return 返回generatedKey
	 */
	public boolean isGeneratedKey() {
		return generatedKey;
	}

	/**
	 * @param generatedKey 设置generatedKey
	 */
	public void setGeneratedKey(boolean generatedKey) {
		this.generatedKey = generatedKey;
	}
}
