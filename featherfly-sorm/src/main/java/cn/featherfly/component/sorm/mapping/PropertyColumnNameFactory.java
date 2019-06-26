
package cn.featherfly.component.sorm.mapping;

import cn.featherfly.common.bean.BeanProperty;

/**
 * <p>
 * PropertyColumnNameFactory
 * </p>
 * 
 * @author zhongj
 */
public interface PropertyColumnNameFactory {
    /**
     * <p>
     * 根据传入的属性返回对应的columnName
     * </p>
     * @param beanProperty beanProperty
     * @return columnName
     */
    <T> String getMappingColumnName(BeanProperty<T> beanProperty);
}
