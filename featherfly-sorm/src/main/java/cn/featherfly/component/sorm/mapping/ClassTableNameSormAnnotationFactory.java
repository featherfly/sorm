
package cn.featherfly.component.sorm.mapping;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.bean.BeanDescriptor;
import cn.featherfly.component.sorm.annotation.Table;

/**
 * <p>
 * ClassTableMapping sorm impl. use cn.featherfly.component.sorm.annotation.Table
 * </p>
 * 
 * @author zhongj
 */
public class ClassTableNameSormAnnotationFactory implements ClassTableNameFactory{
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> String getMappingTableName(Class<T> type) {
        BeanDescriptor<T> bd = BeanDescriptor.getBeanDescriptor(type);        
        Table table = bd.getAnnotation(Table.class);
        if (table != null) {
            return table.value();
        }
        return null;
    }
    
}
