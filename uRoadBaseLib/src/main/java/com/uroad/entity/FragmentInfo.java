/**
 * @Title: FragmentInfo.java
 * @Package com.uroad.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author oupy
 * @date 2013-12-30 下午4:53:20
 * @version V1.0
 */
package com.uroad.entity;

import android.os.Bundle;

/**
 * @author Administrator
 *
 */
public class FragmentInfo {
	private Class<?> clss;
	private Bundle args;

	public FragmentInfo( Class<?> _class, Bundle _args) {
		clss = _class;
		args = _args;
	}

	/**
	 * @return the args
	 */
	public Bundle getArgs() {
		return args;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Bundle args) {
		this.args = args;
	}

	/**
	 * @return the clss
	 */
	public  Class<?> getClss() {
		return clss;
	}

	/**
	 * @param clss
	 *            the clss to set
	 */
	public void setClss( Class<?> clss) {
		this.clss = clss;
	}

}
