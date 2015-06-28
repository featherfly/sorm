
package cn.featherfly.component.sorm.mapping;


import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.bean.BeanDescriptor;

/**
 * <p>
 * ClassTableMapping jpa impl. use javax.persistence.Table
 * </p>
 * 
 * @author 钟冀
 */
public class ClassTableNameJpaFactory implements ClassTableNameFactory{
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> String getMappingTableName(Class<T> type) {
        BeanDescriptor<T> bd = BeanDescriptor.getBeanDescriptor(type);        
        Table table = bd.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }
        return null;
    }
    
}
