
package cn.featherfly.component.sorm.vo;

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
@Table("school")
public class School {
	@Identity
	private String id;
	
	private String name;
	
	private String address;

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
	 * 返回address
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置address
	 * @param address address
	 */
	public void setAddress(String address) {
		this.address = address;
	}
}
