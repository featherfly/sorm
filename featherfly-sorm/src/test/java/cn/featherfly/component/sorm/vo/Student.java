
package cn.featherfly.component.sorm.vo;

import cn.featherfly.component.sorm.annotation.Table;

/**
 * <p>
 * 类的说明放这里
 * </p>
 * <p>
 * copyright featherfly 2010-2020, all rights reserved.
 * </p>
 * 
 * @author zhongj
 */
@Table("student")
public class Student extends Person{

	private String className;

	/**
	 * 返回className
	 * @return className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 设置className
	 * @param className className
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
}
