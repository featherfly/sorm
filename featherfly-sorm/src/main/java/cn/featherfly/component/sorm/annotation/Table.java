
package cn.featherfly.component.sorm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 注释值对象与数据库的对应关系.
 * </p>
 * @author zhongj
 * @since 2.0
 * @version 2.0
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/**
	 * 表的名称
	 */
	String value();
}
