
package cn.featherfly.component.sorm.mapping;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.lang.WordUtils;

/**
 * <p>
 * ClassTableMapping use _ join type every word. ClassTableMapping as class_table_mapping
 * </p>
 * 
 * @author 钟冀
 */
public class ClassTableNameSormNameMapFactory implements ClassTableNameFactory{
    
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> String getMappingTableName(Class<T> type) {
        return WordUtils.addSignBeforeUpper(type.getSimpleName(), '_' , true);
    }
    
}
