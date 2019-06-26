
package cn.featherfly.component.sorm.operate;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.bean.BeanUtils;
import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadataManager;
import cn.featherfly.common.lang.LangUtils;
import cn.featherfly.component.sorm.mapping.ClassMapping;

/**
 * <p>
 * 数据库操作的抽象类
 * </p>
 * @param <T> 类型
 * @author zhongj
 * @since 1.0
 * @version 1.0
 */
public abstract class AbstractOperate<T> {
	
	/**
	 * logger
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * sql 语句
	 */
	protected String sql;
	/**
	 * jdbcTemplate
	 */
	protected JdbcTemplate jdbcTemplate;
	/**
	 * 类型映射
	 */
	protected ClassMapping<T> classMapping;
	/**
	 * 数据库元数据
	 */
	protected DatabaseMetadata meta;
	/**
	 * 属性在SQL中出现的位置，即SQL语句中每个问号对应的对象属性
	 */
	protected Map<Integer, String> propertyPositions =  new HashMap<Integer, String>(0);

	/**
	 * 使用给定数据源以及给定对象生成其相应的操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 */
	public AbstractOperate(Class<T> type, JdbcTemplate jdbcTemplate) {
		this(type, jdbcTemplate, null);
	}
	/**
	 * 使用给定数据源以及给定对象生成其相应的操作.
	 * @param type 类
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public AbstractOperate(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		if (LangUtils.isEmpty(dataBase)) {
			meta = DatabaseMetadataManager.getDefaultManager().create(jdbcTemplate.getDataSource());
		} else {
			meta = DatabaseMetadataManager.getDefaultManager().create(jdbcTemplate.getDataSource(), dataBase);
		}
		this.jdbcTemplate = jdbcTemplate;
		this.classMapping = (ClassMapping<T>) ClassMapping.getMappedClass(type, meta);
		initSql();
	}
	/**
	 * @return 返回sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * <p>
	 * 设置预编译参数
	 * </p>
	 * @param prep 执行SQL的PreparedStatementWrapper
	 * @param entity 对象
	 */
	protected void setParameter(PreparedStatement prep, T entity) {
		for (Entry<Integer, String> propertyPosition : propertyPositions.entrySet()) {
			JdbcUtils.setParameter(prep, propertyPosition.getKey(),
					BeanUtils.getProperty(entity, propertyPosition.getValue()));
		}
	}
	public Object[] getParameters(T entity) {
	    Object[] params = new Object[propertyPositions.size()];
	    int i = 0;
	    for (Entry<Integer, String> propertyPosition : propertyPositions.entrySet()) {
	        params[i] = BeanUtils.getProperty(entity, propertyPosition.getValue());
	        i++;
        }
	    return params;
	}
	/**
	 * <p>
	 * 设置预编译参数
	 * </p>
	 * @param prep 执行SQL的PreparedStatementWrapper
	 * @param id 主键
	 */
	protected void setParameter(PreparedStatement prep, Serializable id) {
		// 还不支持复合主键
		JdbcUtils.setParameter(prep, 1, id);
	}

	/**
	 * <p>
	 * 初始化SQL，由具体的实现类来实现
	 * </p>
	 */
	protected abstract void initSql();
}
