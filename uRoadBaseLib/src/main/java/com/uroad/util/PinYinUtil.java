package com.uroad.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {
	// public static String getPinYin(String input) {
	// ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
	// StringBuilder sb = new StringBuilder();
	// if (tokens != null && tokens.size() > 0) {
	// for (Token token : tokens) {
	// if (Token.PINYIN == token.type) {
	// sb.append(token.target);
	// } else {
	// sb.append(token.source);
	// }
	// }
	// }
	// return sb.toString().toLowerCase();
	// }

	/**
	 *
	 * 将字符串中的中文转化为拼音,其他字符不变
	 *
	 * 花花大神->huahuadashen
	 *
	 * @param inputString
	 *
	 * @return
	 */

	public static String getPinYin(String inputString) {

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();

		String output = "";

		try {

			for (char curchar : input) {

				if (java.lang.Character.toString(curchar).matches(

						"[\\u4E00-\\u9FA5]+")) {

					String[] temp = PinyinHelper.toHanyuPinyinStringArray(

							curchar, format);

					output += temp[0];

				} else

					output += java.lang.Character.toString(curchar);

			}

		} catch (BadHanyuPinyinOutputFormatCombination e) {

			e.printStackTrace();

		}

		return output;

	}

	/**
	 *
	 * 汉字转换为汉语拼音首字母，英文字符不变
	 *
	 * 花花大神->hhds
	 *
	 * @param chines
	 *
	 *            汉字
	 *
	 * @return 拼音
	 */

	public static String getFirstSpell(String chinese) {

		StringBuffer pybf = new StringBuffer();

		char[] arr = chinese.toCharArray();

		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

		for (char curchar : arr) {

			if (curchar > 128) {

				try {

					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							curchar, defaultFormat);

					if (temp != null) {

						pybf.append(temp[0].charAt(0));

					}

				} catch (BadHanyuPinyinOutputFormatCombination e) {

					e.printStackTrace();

				}

			} else {

				pybf.append(curchar);

			}

		}

		return pybf.toString().replaceAll("\\W", "").trim();

	}

}
