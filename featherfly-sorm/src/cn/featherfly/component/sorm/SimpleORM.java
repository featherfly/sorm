
package cn.featherfly.component.sorm;

import java.io.Serializable;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.featherfly.common.db.builder.ConditionBuilder;
import cn.featherfly.common.db.data.Execution;
import cn.featherfly.common.db.data.SimpleExecution;
import cn.featherfly.common.lang.AssertIllegalArgument;
import cn.featherfly.component.sorm.operate.DeleteOperate;
import cn.featherfly.component.sorm.operate.GetOperate;
import cn.featherfly.component.sorm.operate.InsertOperate;
import cn.featherfly.component.sorm.operate.MergeOperate;
import cn.featherfly.component.sorm.operate.QueryOperate;
import cn.featherfly.component.sorm.operate.UpdateOperate;


/**
 * <p>
 * 简单对象记录映射.
 * </p>
 * @param <T> 对象类型
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class SimpleORM<T> {

	/**
	 * @param type 类型
	 * @param jdbcTemplate jdbcTemplate
	 */
	public SimpleORM(Class<T> type, JdbcTemplate jdbcTemplate) {
		init(type, jdbcTemplate, null);
	}
	
	/**
	 * @param type 类型
	 * @param jdbcTemplate jdbcTemplate
	 * @param dataBase 具体库
	 */
	public SimpleORM(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		init(type, jdbcTemplate, dataBase);
	}
	
	private void init(Class<T> type, JdbcTemplate jdbcTemplate, String dataBase) {
		AssertIllegalArgument.isNotNull(jdbcTemplate, "jdbcTemplate不能为空");
		AssertIllegalArgument.isNotNull(jdbcTemplate.getDataSource(), "数据源（jdbcTemplate.dataSource）不能为空");
		insertOperate = new InsertOperate<T>(type, jdbcTemplate, dataBase);
		updateOperate = new UpdateOperate<T>(type, jdbcTemplate, dataBase);
		deleteOperate = new DeleteOperate<T>(type, jdbcTemplate, dataBase);
		mergeOperate = new MergeOperate<T>(type, jdbcTemplate, dataBase);
		getOperate = new GetOperate<T>(type, jdbcTemplate, dataBase);
		queryOperate = new QueryOperate<T>(type, jdbcTemplate, dataBase);
		setGeneratedKey(true);
	}
	/**
	 * <p>
	 * 保存.
	 * </p>
	 * @param entity 对象
	 * @return 影响的数据库行数
	 */
	public int save(T entity) {
		return insertOperate.execute(entity);
	}

	/**
	 * <p>
	 * 更新.
	 * </p>
	 * @param entity 对象
	 * @return 影响的数据库行数
	 */
	public int update(T entity) {
		return updateOperate.execute(entity);
	}

	/**
	 * <p>
	 * 合并操作，将传入对象的非空字段更新进数据库（忽略null）.
	 * </p>
	 * @param entity 对象
	 */
	public void merge(T entity) {
		// YUFEI_TODO 合并可以使用外置合并策略来修改合并的行为，比如数据库列的白黑名单，对象属性白黑名单等
		mergeOperate.execute(entity);
	}

	/**
	 * <p>
	 * 删除.
	 * </p>
	 * @param entity 对象
	 * @return 影响的数据库行数
	 */
	public int delete(T entity) {
		return deleteOperate.execute(entity);
	}

	/**
	 * <p>
	 * 获取指定的对象.
	 * </p>
	 * @see cn.featherfly.component.sorm.operate.GetOperate#get(id)
	 * @param id 唯一标识
	 * @return 指定主键值的对象
	 */
	public T get(Serializable id) {
		return getOperate.get(id);
	}
	/**
	 * <p>
	 * 加载对象.
	 * </p>
	 * @param entity 对象
	 * @return 传入对象主键值对应的数据库对象
	 */
	public T load(T entity) {
		return getOperate.get(
				getOperate.getIdentity(entity));
	}
	/**
	 * <p>
	 * 根据指定条件返回唯一结果.
	 * </p>
	 * @param conditionBuilder 查询条件构建器
	 * @return 唯一结果
	 */
	public T unique(ConditionBuilder conditionBuilder) {
		return queryOperate.unique(conditionBuilder);
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
		return queryOperate.unique(condition, params);
	}
	/**
	 * <p>
	 * 根据指定条件返回查询结果集合.
	 * </p>
	 * @param conditionBuilder 查询条件构建器
	 * @return 结果集合
	 */
	public List<T> list(ConditionBuilder conditionBuilder) {
		return queryOperate.list(conditionBuilder);
	}
	/**
	 * <p>
	 * 根据指定条件返回查询结果集合.
	 * </p>
	 * @param condition 查询条件SQL（不包含where）
	 * @param params 查询参数
	 * @return 结果集合
	 */
	public List<T> list(String condition, Object...params) {
		return queryOperate.list(condition, params);
	}
	
	/**
	 * <p>
	 * 返回唯一标示值
	 * </p>
	 * @param entity 实体对象
	 * @return 唯一标示值
	 */
	public Serializable getIdentity(T entity) {
		if (entity == null) {
			return null;
		}
		return getOperate.getIdentity(entity);
	}
	/**
	 * @return 是否自动设置生成的主键值
	 * @see cn.featherfly.component.sorm.operate.AbstractExecuteOperate#isGeneratedKey()
	 */
	public boolean isGeneratedKey() {
		return insertOperate.isGeneratedKey();
	}

	/**
	 * @param generatedKey 是否自动设置生成的主键值
	 * @see cn.featherfly.component.sorm.operate.AbstractExecuteOperate#setGeneratedKey(boolean)
	 */
	public void setGeneratedKey(boolean generatedKey) {
		insertOperate.setGeneratedKey(generatedKey);
	}
	
	/**
     * <p>
     * 根据指定条件返回Execution.
     * </p>
     * @param conditionBuilder 查询条件构建器
     * @return Execution
     */
	public Execution getQueryExecution(ConditionBuilder conditionBuilder) {
	    Execution execution = new SimpleExecution(queryOperate.getSelectSql(), 
	            conditionBuilder.getParams());
	    return execution;
	}
	/**
     * <p>
     * 返回获取指定对象的Execution.
     * </p>
     * @param id 唯一标识
     * @return Execution
     */
	public Execution getGetExecution(Serializable id) {
	    Execution execution = new SimpleExecution(getOperate.getSql(), 
                new Object[] {id});
        return execution;
	}
	/**
     * <p>
     * 返回保存对象的Execution.
     * </p>
     * @param entity 对象
     * @return Execution
     */
	public Execution getSaveExecution(T entity) {
	    insertOperate.execute(entity);
	    Execution execution = new SimpleExecution(insertOperate.getSql(), 
	            insertOperate.getParameters(entity));
        return execution;
	}
	/**
     * <p>
     * 返回更新对象的Execution.
     * </p>
     * @param entity 对象
     * @return Execution
     */
	public Execution getUpdateExecution(T entity) {
	    Execution execution = new SimpleExecution(updateOperate.getSql(), 
	            updateOperate.getParameters(entity));
	    return execution;
	}
	
//	public Execution getMergeExecution(T entity) {
//          TODO 之后来实现
	//	    Execution execution = new SimpleExecution(mergeOperate.getSql(), 
//	            new Object[] {id});
//	    return execution;
//	}
	/**
     * <p>
     * 返回删除对象的Execution.
     * </p>
     * @param entity 对象
     * @return Execution
     */
	public Execution getDeleteExecution(T entity) {
	    Execution execution = new SimpleExecution(deleteOperate.getSql(), 
	            deleteOperate.getParameters(entity));
	    return execution;
	}

	// ********************************************************************
	//	property
	// ********************************************************************

	private InsertOperate<T> insertOperate;

	private UpdateOperate<T> updateOperate;

	private DeleteOperate<T> deleteOperate;

	private MergeOperate<T> mergeOperate;

	private GetOperate<T> getOperate;
	
	private QueryOperate<T> queryOperate;


}
