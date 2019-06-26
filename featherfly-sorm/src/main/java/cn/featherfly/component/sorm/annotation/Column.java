
package cn.featherfly.component.sorm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 注释值对象的属性与数据库的列的对应关系.
 * </p>
 * @author zhongj
 * @since 2.0
 * @version 2.0
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/**
	 * 列的名称
	 */
	String value();
	/**
	 * 属性名称，默认值为当前属性
	 */
	String innerProperty() default "";
}
