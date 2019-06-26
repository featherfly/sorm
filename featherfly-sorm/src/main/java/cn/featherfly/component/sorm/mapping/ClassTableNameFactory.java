
package cn.featherfly.component.sorm.mapping;

/**
 * <p>
 * ClassTableMapping
 * </p>
 * 
 * @author zhongj
 */
public interface ClassTableNameFactory {
    /**
     * <p>
     * 根据传入的类型返回对应的tableName
     * </p>
     * @param type type
     * @return talbeName
     */
    <T> String getMappingTableName(Class<T> type);
}
