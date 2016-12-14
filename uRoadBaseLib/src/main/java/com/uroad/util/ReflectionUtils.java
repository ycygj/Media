/**
 * Copyright 2013 Alex Yanchenko
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.uroad.util;

import static com.uroad.util.TypeHelper.isArray;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public final class ReflectionUtils {

	public static <ValType> ValType getFieldVal(Object obj, Field field)
			throws IllegalArgumentException {
		try {
			field.setAccessible(true);
			@SuppressWarnings("unchecked")
			ValType val = (ValType) field.get(obj);
			return val;
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static void setFieldVal(Object obj, Field field, Object val)
			throws IllegalArgumentException {
		try {
			field.setAccessible(true);
			field.set(obj, val);
		} catch (Exception e) { 			
			throw new IllegalArgumentException(e);
		}
	}

	public static Class<?> classForName(String clsName)
			throws IllegalArgumentException {
		try {
			return Class.forName(clsName);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <InstanceType> InstanceType instantiate(
			Class<InstanceType> cls) throws IllegalArgumentException {
		try {
			return cls.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Enum<?> instantiateEnum(Class<?> enumClass, String enumStr) {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Enum en = Enum.valueOf(enumClass.asSubclass(Enum.class), enumStr);
		return en;
	}

	public static List<Field> listAnnotatedFields(Class<?> cls) {
		ArrayList<Class<?>> clsTree = new ArrayList<Class<?>>();
		boolean enteredDroidParts = false;
		do {
			clsTree.add(0, cls);
			boolean inDroidParts = cls.getName().startsWith("org.droidparts");
			if (enteredDroidParts && !inDroidParts) {
				break;
			} else {
				enteredDroidParts = inDroidParts;
				cls = cls.getSuperclass();
			}
		} while (cls != null);
		ArrayList<Field> fields = new ArrayList<Field>();
		for (Class<?> c : clsTree) {
			for (Field f : c.getDeclaredFields()) {
				if (f.getAnnotations().length > 0) {
					fields.add(f);
				}
			}
		}
		return fields;
	}

	public static Class<?> getArrayType(Class<?> arrCls) {
		if (arrCls == byte[].class) {
			return byte.class;
		} else if (arrCls == short[].class) {
			return short.class;
		} else if (arrCls == int[].class) {
			return int.class;
		} else if (arrCls == long[].class) {
			return long.class;
		} else if (arrCls == float[].class) {
			return float.class;
		} else if (arrCls == double[].class) {
			return double.class;
		} else if (arrCls == boolean[].class) {
			return boolean.class;
		} else if (arrCls == char[].class) {
			return char.class;
		} else {
			// objects - [Ljava.lang.String;
			String clsName = arrCls.getName();
			clsName = clsName.substring(2, clsName.length() - 1);
			return classForName(clsName);
		}
	}

	public static Class<?>[] getFieldGenericArgs(Field field) {
		Type genericType = field.getGenericType();
		if (genericType instanceof ParameterizedType) {
			Type[] typeArr = ((ParameterizedType) genericType)
					.getActualTypeArguments();
			Class<?>[] argsArr = new Class<?>[typeArr.length];
			for (int i = 0; i < typeArr.length; i++) {
				// class java.lang.String
				String[] nameParts = typeArr[i].toString().split(" ");
				String clsName = nameParts[nameParts.length - 1];
				argsArr[i] = classForName(clsName);
			}
			return argsArr;
		} else {
			return new Class<?>[0];
		}
	}

	public static Object[] varArgsHack(Object[] varArgs) {
		if (varArgs != null && varArgs.length == 1) {
			Object firstArg = varArgs[0];
			if (firstArg != null) {
				Class<?> firstArgCls = firstArg.getClass();
				if (isArray(firstArgCls)) {
					//varArgs = toObjectArr(firstArg);
				}
			}
		}
		return varArgs;
	}

}
