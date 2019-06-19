
package cn.featherfly.component.sorm;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.db.JdbcUtils;
import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.common.lang.LangUtils;




/**
 * <p>
 * 简单对象记录映射.
 * </p>
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class SimpleORMFactory {

	/**
	 * 使用给定数据源构造
	 */
	public SimpleORMFactory() {

	}

	/**
	 * 使用给定数据源构造
	 * @param dataSource 数据源
	 */
	public SimpleORMFactory(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * 使用给定数据源构造
	 * @param dataSource 数据源
	 * @param dataBase 具体数据库（mysql为数据库，oracle为表空间）
	 */
	public SimpleORMFactory(DataSource dataSource, String dataBase) {
		this.dataSource = dataSource;
		this.dataBase = dataBase;
	}
	/**
	 * <p>
	 * 返回默认工厂
	 * </p>
	 * @return 默认工厂
	 */
	public static SimpleORMFactory getDefaultFactory() {
		return INSTANCE;
	}

	/**
	 * <p>
	 * 返回默认工厂，并让默认工厂使用传入的数据源
	 * </p>
	 * @param dataSource 指定数据源
	 * @return 默认工厂
	 */
	public static SimpleORMFactory getDefaultFactory(DataSource dataSource) {
		INSTANCE.setDataSource(dataSource);
		return INSTANCE;
	}

	/**
	 * <p>
	 * 根据传入的类型返回对应类型的SimpleORM对象.
	 * </p>
	 * @param <E> 泛型
	 * @param type 类型
	 * @return SimpleORM对象
	 */
	public <E> SimpleORM<E> getSimpleORM(Class<E> type) {
		AssertIllegalArgument.isNotNull(dataSource, "请先使用setDataSource为SimpleORMFactory设置数据源");
		if (LangUtils.isEmpty(dataBase)) {
			dataBase = JdbcUtils.getCatalog(dataSource);
		}
		AssertIllegalArgument.isNotEmpty(dataBase,
				"请先使用setDataBase为SimpleORMFactory设置具体数据库（mysql为数据库，oracle为表空间）");
		@SuppressWarnings("unchecked")
		SimpleORM<E> simpleORM = (SimpleORM<E>) cache.get(type);

		if (simpleORM == null) {
			synchronized (cache) {
				if (simpleORM == null) {
					simpleORM = new SimpleORM<E>(type, new JdbcTemplate(dataSource), dataBase);
					cache.put(type, simpleORM);
				}
			}
		}
		return simpleORM;
	}

	// ********************************************************************
	//	property
	// ********************************************************************

	private static final SimpleORMFactory INSTANCE = new SimpleORMFactory(null);

	private final Map<Class<?>, SimpleORM<?>> cache = new HashMap<Class<?>, SimpleORM<?>>(0);

	private DataSource dataSource;

	private String dataBase;

	/**
	 * 返回dataBase
	 * @return dataBase
	 */
	public String getDataBase() {
		return dataBase;
	}

	/**
	 * 设置dataBase
	 * @param dataBase dataBase
	 */
	public void setDataBase(String dataBase) {
		this.dataBase = dataBase;
	}

	/**
	 * 设置数据源
	 * @param dataSource 设置dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
