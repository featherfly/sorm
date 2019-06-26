
package cn.featherfly.component.sorm.vo;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import cn.featherfly.component.sorm.annotation.Column;
import cn.featherfly.component.sorm.annotation.Identity;
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
@Table("teacher")
public class Teacher {
	@Identity
	private String id;
	
	private String className;
	@Column(value = "school_id", innerProperty = "id")
	@javax.persistence.Column
	@OneToOne
	@ManyToOne
	private School school;
	
	/**
	 * 返回id
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置id
	 * @param id id
	 */
	public void setId(String id) {
		this.id = id;
	}

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

	/**
	 * 返回school
	 * @return school
	 */
	public School getSchool() {
		return school;
	}

	/**
	 * 设置school
	 * @param school school
	 */
	public void setSchool(School school) {
		this.school = school;
	}
}
