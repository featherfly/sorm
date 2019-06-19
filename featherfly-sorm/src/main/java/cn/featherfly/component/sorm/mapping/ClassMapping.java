package cn.featherfly.component.sorm.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.featherfly.common.bean.BeanDescriptor;
import cn.featherfly.common.bean.BeanProperty;
import cn.featherfly.common.bean.matcher.BeanPropertyAnnotationMatcher;
import cn.featherfly.common.bean.matcher.BeanPropertyNameRegexMatcher;
import cn.featherfly.common.constant.Chars;
import cn.featherfly.common.db.metadata.ColumnMetadata;
import cn.featherfly.common.db.metadata.DatabaseMetadata;
import cn.featherfly.common.db.metadata.TableMetadata;
import cn.featherfly.common.enums.Logic;
import cn.featherfly.common.lang.LangUtils;
import cn.featherfly.common.lang.ServiceLoaderUtils;
import cn.featherfly.common.lang.StringUtils;
import cn.featherfly.common.lang.SystemPropertyUtils;
import cn.featherfly.common.lang.WordUtils;
import cn.featherfly.component.sorm.SimpleORMException;
import cn.featherfly.component.sorm.annotation.Column;
import cn.featherfly.component.sorm.annotation.Identity;

/**
 * <p>
 * 类映射
 * </p>
 * @param <T> 类型
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class ClassMapping<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassMapping.class);

	private static final char UNDER_LINE = '_';

	private static final Map<Class<?>, ClassMapping<?>> MAPPED_CLASS = new HashMap<Class<?>, ClassMapping<?>>();
	
	private static final List<ClassTableNameFactory> TABLE_NAME_FACTORIES;
	
//	private static final List<PropertyColumnNameFactory> COLUMN_NAME_FACTORIES;
	
	static {
	    TABLE_NAME_FACTORIES = ServiceLoaderUtils.loadAll(ClassTableNameFactory.class);
	    TABLE_NAME_FACTORIES.add(new ClassTableNameJpaFactory());	    
	    TABLE_NAME_FACTORIES.add(new ClassTableNameSormAnnotationFactory());
	    TABLE_NAME_FACTORIES.add(new ClassTableNameSormNameMapFactory());
	    
//	    COLUMN_NAME_FACTORIES = ServiceLoaderUtils.loadAll(PropertyColumnNameFactory.class);
	}
	
	/**
	 *
	 * @param type 类型
	 * @param tableName 表名
	 */
	public ClassMapping(Class<T> type, String tableName) {
		this.type = type;
		this.tableName = tableName;
	}

	/**
	 * <p>
	 * 返回指定属性名称的属性映射.
	 * 没有找到返回null.
	 * </p>
	 * @param propertyName 属性名称
	 * @return 属性映射对象
	 */
	public PropertyMapping getPropertyMapping(String propertyName) {
		return propertyMappings.get(propertyName);
	}
	/**
	 * <p>
	 * 返回所有属性映射
	 * </p>
	 * @return 所有属性映射
	 */
	public Collection<PropertyMapping> getPropertyMappings() {
		return propertyMappings.values();
	}

	// ********************************************************************
	//
	// ********************************************************************

	/**
	 * 返回指定类型的描述
	 * @param <T> 类型
	 * @param type 类型
	 * @param meta 数据库元数据
	 * @return 指定类型的描述
	 */
	public static <T> ClassMapping<T> getMappedClass(Class<T> type, DatabaseMetadata meta) {
		@SuppressWarnings("unchecked")
		ClassMapping<T> cm = (ClassMapping<T>) MAPPED_CLASS.get(type);
		if (cm == null) {
			Map<String, PropertyMapping> tableMapping = new HashMap<String, PropertyMapping>();
			
			StringBuilder logInfo = new StringBuilder();
//			// 从对象中读取有Column的列，找到显示映射，使用scan扫描			
			BeanDescriptor<T> bd = BeanDescriptor.getBeanDescriptor(type);
		    String tableName = getTableName(type);
			tableName = tableName.toUpperCase();
			logInfo.append(String.format("###%s类%s映射到表%s",
					SystemPropertyUtils.getLineSeparator(), type.getName(), tableName));

			Collection<BeanProperty<?>> bps =  bd.findBeanPropertys(
					new BeanPropertyAnnotationMatcher(Logic.OR, Column.class, Identity.class, Id.class));
			boolean findPk = false;
			for (BeanProperty<?> beanProperty : bps) {
				if (mapping(beanProperty, tableMapping, logInfo)) {
					findPk = true;
				}
			}
			if (!findPk) {
				throw new SimpleORMException("#id.map.not.exists", new Object[] {type.getName()});
			}
			// 从对象对应的table读取列，找到隐式映射（下划线分割后的自动大写）
			TableMetadata tm = meta.getTable(tableName);
			if (tm == null) {
				throw new SimpleORMException("#talbe.not.exists", new Object[] {tableName});
			}
			for (ColumnMetadata cmd : tm.getColumns()) {
				mappingHidden(bd, tableMapping, cmd, logInfo);
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug(logInfo.toString());
			}
			// 形成映射对象
			cm = new ClassMapping<T>(type, tableName);
			cm.addPropertyMappings(tableMapping.values());
			MAPPED_CLASS.put(type, cm);
		}
		return cm;
	}


	// ********************************************************************
	//	private method
	// ********************************************************************
	
	private static String getTableName(Class<?> type) {
	    String tableName = null;
	    for (ClassTableNameFactory classTableNameFactory : TABLE_NAME_FACTORIES) {
	        tableName = classTableNameFactory.getMappingTableName(type);
	        if (LangUtils.isNotEmpty(tableName)) return tableName;
        }
	    if (LangUtils.isEmpty(tableName)) {
	        throw new SimpleORMException("#notalbe.map.type", new Object[] {type.getName()});
	    }
	    return tableName;
	}
	
//	private static String getColumnName(BeanProperty<?> bp) {
//	    String columnName = null;
//	    for (PropertyColumnNameFactory columnNameFactory : COLUMN_NAME_FACTORIES) {
//	        columnName = columnNameFactory.getMappingColumnName(bp);
//	        if (columnName != null) return columnName;
//	    }
//	    if (LangUtils.isEmpty(columnName)) {
//            throw new SimpleORMException(String.format("没有找到类型%s的属性%s对应的列名"
//                    , bp.getOwnerType().getName(), bp.getName()));
//        }
//	    return columnName;
//	}

	private static boolean mapping(BeanProperty<?> beanProperty, Map<String, PropertyMapping> tableMapping
			, StringBuilder logInfo) {
		PropertyMapping mapping = new PropertyMapping();
		Column columnAnnotation = beanProperty.getAnnotation(Column.class);
		String columnName = null;
		String innerProperty = null;
		if (columnAnnotation == null) {
			columnName = WordUtils.addSignBeforeUpper(
					beanProperty.getName(), UNDER_LINE , true);
		} else {
			columnName = columnAnnotation.value();
			innerProperty = columnAnnotation.innerProperty();
		}
		mapping.setColumnName(columnName.toUpperCase());
		mapping.setPropertyName(beanProperty.getName());
		mapping.setPropertyType(beanProperty.getType());
		if (StringUtils.isNotEmpty(innerProperty)) {
			BeanDescriptor<?> innerBd = BeanDescriptor
				.getBeanDescriptor(beanProperty.getType());
			BeanProperty<?> innerBeanProperty = innerBd.getBeanProperty(
					innerProperty);
			mapping.setInnerPropertyName(innerBeanProperty.getName());
			mapping.setInnerPropertyType(innerBeanProperty.getType());
		}
		boolean hasPk = beanProperty.hasAnnotation(Identity.class);;
		if (!hasPk) {
		    hasPk = beanProperty.hasAnnotation(Id.class);
		}
		mapping.setPrimaryKey(hasPk);
		tableMapping.put(mapping.getColumnName(), mapping);
		if (LOGGER.isDebugEnabled()) {
			logInfo.append(String.format("%s###\t%s -> %s",
					SystemPropertyUtils.getLineSeparator(),
					mapping.getFinalPropertyName(), mapping.getColumnName()));
		}
		return hasPk;
	}

	private static <T> void mappingHidden(BeanDescriptor<T> bd, Map<String, PropertyMapping> tableMapping
			, ColumnMetadata cmd, StringBuilder logInfo) {
		if (!tableMapping.containsKey(cmd.getName())) {
			// 转换下划线，并使用驼峰
			String columnName = cmd.getName().toLowerCase();
			String propertyName = WordUtils.parseToUpperFirst(
					columnName, Chars.UNDER_LINE.charAt(0));
			BeanProperty<?> beanProperty = bd.findBeanProperty(
					new BeanPropertyNameRegexMatcher(propertyName));
			if (beanProperty != null) {
				PropertyMapping mapping = new PropertyMapping();
				mapping.setPropertyType(beanProperty.getType());
				mapping.setPropertyName(propertyName);
				mapping.setColumnName(columnName.toUpperCase());
				mapping.setPrimaryKey(cmd.isPrimaryKey());
				tableMapping.put(mapping.getColumnName(), mapping);
				if (LOGGER.isDebugEnabled()) {
					logInfo.append(String.format("%s###\t%s -> %s",
						SystemPropertyUtils.getLineSeparator(),
						mapping.getPropertyName(), mapping.getColumnName()));
				}
			} else {
				if (LOGGER.isDebugEnabled()) {
					logInfo.append(String.format("%s\t没有属性 -> %s [列%s的隐式映射]",
						SystemPropertyUtils.getLineSeparator(), propertyName, cmd.getName()));
				}
			}
		}
	}

	private void addPropertyMapping(PropertyMapping propertyMapping) {
		propertyMappings.put(propertyMapping.getPropertyName(), propertyMapping);
	}

//	private void addPropertyMappings(PropertyMapping...propertyMappings) {
//		for (PropertyMapping propertyMapping : propertyMappings) {
//			addPropertyMapping(propertyMapping);
//		}
//	}

	private void addPropertyMappings(Collection<PropertyMapping> propertyMappings) {
		for (PropertyMapping propertyMapping : propertyMappings) {
			addPropertyMapping(propertyMapping);
		}
	}

	// ********************************************************************
	//
	// ********************************************************************

	private Map<String, PropertyMapping> propertyMappings = new HashMap<String, PropertyMapping>(0);

	private String tableName;

	private Class<?> type;

	/**
	 * @return 返回tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return 返回type
	 */
	public Class<?> getType() {
		return type;
	}
}
