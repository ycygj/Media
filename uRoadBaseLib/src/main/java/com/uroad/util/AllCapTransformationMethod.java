package com.uroad.util;

import android.text.method.ReplacementTransformationMethod;
/**
 * Transfor to Upper
 * **/
public class AllCapTransformationMethod extends ReplacementTransformationMethod {

	@Override
	protected char[] getOriginal() {
		// TODO Auto-generated method stub
		char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		return aa;
	}

	@Override
	protected char[] getReplacement() {
		// TODO Auto-generated method stub
		char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		return cc;
	}

}
