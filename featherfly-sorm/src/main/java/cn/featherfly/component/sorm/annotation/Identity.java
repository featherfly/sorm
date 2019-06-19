
package cn.featherfly.component.sorm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 注释值对象对应数据库主键列的属性.
 * </p>
 * @author 钟冀
 * @since 2.0
 * @version 2.0
 */
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Identity {
}
