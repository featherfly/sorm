
package cn.featherfly.component.sorm.vo;

import cn.featherfly.component.sorm.annotation.Identity;

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
@javax.persistence.Table(name = "person")
public class Person {
	@Identity
	private String id;
	
	private String name;
	
	private String sex;
	
	private Integer age;

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
	 * 返回name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置name
	 * @param name name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回sex
	 * @return sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * 设置sex
	 * @param sex sex
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 返回age
	 * @return age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * 设置age
	 * @param age age
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
}
