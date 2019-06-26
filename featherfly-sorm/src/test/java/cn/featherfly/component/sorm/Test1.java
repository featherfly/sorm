
package cn.featherfly.component.sorm;

import cn.featherfly.common.constant.Chars;
import cn.featherfly.common.lang.WordUtils;

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
public class Test1 {
	public static void main(String[] args) {
		System.out.println(
				WordUtils.parseToUpperFirst(
				"CLASS_NAME", Chars.UNDER_LINE.charAt(0)
				)
		);
		System.out.println(
				WordUtils.parseToUpperFirst(
						"CLASS_NAME".toLowerCase(), Chars.UNDER_LINE.charAt(0)
				)
		);
	}
}
