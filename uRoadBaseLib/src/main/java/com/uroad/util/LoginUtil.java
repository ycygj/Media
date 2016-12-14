package com.uroad.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 用于账号登录、注册校验的工具类
 * **/
public class LoginUtil {
	/**
	 * 正则表达式，java与android稍有不同，如java中\s+ 需要加多\ 变成\\s+, android匹配字符 + Zero or more
	 * (possessive). ?+ Zero or one (possessive). ++ One or more (possessive).
	 * {n}+ Exactlyn(possessive). {n,}+ At leastn(possessive). {n,m}+ At
	 * leastnbut not more thanm(possessive).
	 **/
	/**
	 * 是否为账号规范 如： 6~18个字符，可使用字母、数字、下划线，需以字母开头
	 *
	 * @param text
	 * @return
	 * @author luman
	 */
	public static boolean isAccountStandard(String text) {
		// 不能包含中文
		if (hasChinese(text)) {
			return false;
		}

		/**
		 * 正则匹配： [a-zA-Z]:字母开头 \\w :可包含大小写字母，数字，下划线,@ {5,17} 5到17位，加上开头字母
		 * 字符串长度6到18
		 */
		String format = "[a-zA-Z](@?+\\w){5,17}+";
		if (text.matches(format)) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为密码规范
	 *
	 * @param text
	 * @return
	 * @author luman
	 */
	public static boolean isPasswordStandard(String text) {

		// 不能包含中文
		if (hasChinese(text)) {
			return false;
		}

		/**
		 * 正则匹配 \\w{6,18}匹配所有字母、数字、下划线 字符串长度6到18（不含空格）
		 */
		String format = "\\w{6,18}+";
		if (text.matches(format)) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为Urod+密码规范
	 *
	 * @param text
	 * @return
	 * @author luman
	 */
	public static boolean isPasswordForUroad(String text) {

		// 不能包含中文
		if (hasChinese(text)) {
			return false;
		}

		/**
		 * 正则匹配 \\w{3,18}匹配所有字母、数字、下划线 字符串长度3到18（不含空格）
		 */
		String format = "\\w{3,18}+";
		if (text.matches(format)) {
			return true;
		}
		return false;
	}

	/**
	 * 中文识别
	 *
	 * @author luman
	 */
	public static boolean hasChinese(String source) {
		String reg_charset = "([\\u4E00-\\u9FA5]*+)";
		Pattern p = Pattern.compile(reg_charset);
		Matcher m = p.matcher(source);
		boolean hasChinese = false;
		while (m.find()) {
			if (!"".equals(m.group(1))) {
				hasChinese = true;
			}

		}
		return hasChinese;
	}

	/**
	 * 校验是否为正确的手机号码
	 * **/
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 校验是否为正确的邮箱
	 * **/
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}



}
