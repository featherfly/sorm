package cn.featherfly.component.sorm.operate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.db.JdbcException;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.builder.ConditionBuilder;
import cn.featherfly.common.lang.CollectionUtils;
import cn.featherfly.common.lang.LangUtils;
import cn.featherfly.component.sorm.SimpleORMException;

/**
 * <p>
 * 查询操作
 * </p>
 * @param <T> 对象类型
 * @author zhongj
 * @since 1.0
 * @version 1.0
 */
public class QueryOperate<T> extends AbstractQueryOperate<T>{
	
	/**
	 * 使用给定数据源以及给定对象生成读取操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public QueryOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		super(type, jdbcTemplate);
	}
	/**
	 * 使用给定数据源以及给定对象生成读取操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public QueryOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		super(type, jdbcTemplate, dataBase);
	}
	
	/**
	 * <p>
	 * 根据指定条件返回唯一结果.
	 * </p>
	 * @param conditionBuilder 查询条件构建器
	 * @return 唯一结果
	 */
	public T unique(ConditionBuilder conditionBuilder) {
		conditionBuilder.setBuildWithWhere(false);
		return unique(conditionBuilder.build(), CollectionUtils.toArray(conditionBuilder.getParams(), Object.class));
	}

	/**
	 * <p>
	 * 根据查询条件返回唯一对象.
	 * </p>
	 * @param condition 查询条件SQL（不包含where）
	 * @param params 查询参数
	 * @return 指定ID的对象
	 */
	public T unique(String condition, Object...params) {
		List<T> list = list(condition, params);
		if (LangUtils.isNotEmpty(list)) {
			if (list.size() > 1) {
				throw new JdbcException("invoke unique method, but multy object find");
			}
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * <p>
	 * 根据指定条件返回查询结果集合.
	 * </p>
	 * @param conditionBuilder 查询条件构建器
	 * @return 结果集合
	 */
	public List<T> list(ConditionBuilder conditionBuilder) {
		conditionBuilder.setBuildWithWhere(true);
        return query(sql + " " + conditionBuilder.build(), CollectionUtils.toArray(conditionBuilder.getParams(), Object.class));
	}
	
	/**
	 * <p>
	 * 根据指定条件返回查询结果集合.
	 * </p>
	 * @param condition 查询条件SQL（不包含where）
	 * @param params 查询参数
	 * @return 结果集合
	 */
	public List<T> list(final String condition, final Object...params) {
		if (LangUtils.isEmpty(condition)) {
			throw new SimpleORMException("#list.condition.null");
		}
		return query(getSql(condition), params);
	}
	
	private List<T> query(final String executeSql, final Object...params) {
	    return jdbcTemplate.execute(new ConnectionCallback<List<T>>() {
            @Override
            public List<T> doInConnection(Connection conn) throws SQLException,
                    DataAccessException {
                logger.debug("execute sql: {}" , executeSql);
                PreparedStatement prep = conn.prepareStatement(executeSql);
                
                JdbcUtils.setParameters(prep, params);
                
                ResultSet res = prep.executeQuery();
                List<T> list = new ArrayList<>();
                int index = 0;
                T t = null;
                while (res.next()) {
                    t = mapRow(res, index);
                    index++;
                    list.add(t);
                }
                prep.close();
                conn.close();
                return list;
            }
        });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String initCondition() {
		return "";
	}
	/**
	 * <p>
	 * 获取带条件的sql
	 * </p>
	 * @param condition 条件
	 * @return 带条件的sql
	 */
	public String getSql(String condition) {
	    if (LangUtils.isNotEmpty(condition)) {
	        condition = " where " + condition;
	    } else {
	        condition = "";
	    }
	    return sql + condition;
	}
	/**
     * <p>
     * 获取带条件的sql
     * </p>
     * @param builder 条件构建器
     * @return 带条件的sql
     */
	public String getSql(ConditionBuilder builder) {
	    builder.setBuildWithWhere(true);
	    return sql + " " + builder.build();
	}
}
