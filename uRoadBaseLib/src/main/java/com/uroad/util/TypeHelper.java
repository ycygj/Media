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

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public final class TypeHelper {

	public static boolean isByte(Class<?> cls) {
		return (cls == byte.class || cls == Byte.class);
	}

	public static boolean isShort(Class<?> cls) {
		return (cls == short.class || cls == Short.class);
	}

	public static boolean isInteger(Class<?> cls) {
		return (cls == int.class || cls == Integer.class);
	}

	public static boolean isLong(Class<?> cls) {
		return (cls == long.class || cls == Long.class);
	}

	public static boolean isFloat(Class<?> cls) {
		return (cls == float.class || cls == Float.class);
	}

	public static boolean isDouble(Class<?> cls) {
		return (cls == double.class || cls == Double.class);
	}

	public static boolean isBoolean(Class<?> cls) {
		return (cls == boolean.class || cls == Boolean.class);
	}

	public static boolean isCharacter(Class<?> cls) {
		return (cls == char.class || cls == Character.class);
	}

	//

	public static boolean isString(Class<?> cls) {
		return cls == String.class;
	}

	public static boolean isEnum(Class<?> cls) {
		return cls.isEnum();
	}

	public static boolean isUUID(Class<?> cls) {
		return UUID.class.isAssignableFrom(cls);
	}

	public static boolean isDate(Class<?> cls) {
		return Date.class.isAssignableFrom(cls);
	}

	//

	public static boolean isByteArray(Class<?> cls) {
		return cls == byte[].class;
	}

	public static boolean isArray(Class<?> cls) {
		return cls.isArray();
	}

	public static boolean isCollection(Class<?> cls) {
		return Collection.class.isAssignableFrom(cls);
	}

	//

	public static boolean isBitmap(Class<?> cls) {
		return Bitmap.class.isAssignableFrom(cls);
	}

	public static boolean isDrawable(Class<?> cls) {
		return Drawable.class.isAssignableFrom(cls);
	}

	//

	public static boolean isJsonObject(Class<?> cls) {
		return JSONObject.class.isAssignableFrom(cls);
	}

	public static boolean isJsonArray(Class<?> cls) {
		return JSONArray.class.isAssignableFrom(cls);
	}


//
//	//
//
//	public static Object[] toObjectArr(Object someArr) {
//		// as autoboxing won't work for Arrays.asList(int[] value)
//		Class<?> arrCls = someArr.getClass();
//		if (arrCls == byte[].class) {
//			return toObject((byte[]) someArr);
//		} else if (arrCls == short[].class) {
//			return toObject((short[]) someArr);
//		} else if (arrCls == int[].class) {
//			return toObject((int[]) someArr);
//		} else if (arrCls == long[].class) {
//			return toObject((long[]) someArr);
//		} else if (arrCls == float[].class) {
//			return toObject((float[]) someArr);
//		} else if (arrCls == double[].class) {
//			return toObject((double[]) someArr);
//		} else if (arrCls == boolean[].class) {
//			return toObject((boolean[]) someArr);
//		} else if (arrCls == char[].class) {
//			return toObject((char[]) someArr);
//		} else {
//			// out of primitives
//			return (Object[]) someArr;
//		}
//	}
//
//	public static Object toTypeArr(Class<?> arrValType, Object[] arr) {
//		if (isModel(arrValType)) {
//			Object modelArr = Array.newInstance(arrValType, arr.length);
//			for (int i = 0; i < arr.length; i++) {
//				Array.set(modelArr, i, arr[i]);
//			}
//			return modelArr;
//		} else {
//			String[] arr2 = new String[arr.length];
//			for (int i = 0; i < arr.length; i++) {
//				arr2[i] = arr[i].toString();
//			}
//			return toTypeArr(arrValType, arr2);
//		}
//	}
//
//	public static Object toTypeArr(Class<?> arrValType, String[] arr) {
//		if (isByte(arrValType)) {
//			ArrayList<Byte> list = toTypeColl(Byte.class, arr);
//			Byte[] tArr = list.toArray(new Byte[list.size()]);
//			return (arrValType == byte.class) ? toPrimitive(tArr) : tArr;
//		} else if (isShort(arrValType)) {
//			ArrayList<Short> list = toTypeColl(Short.class, arr);
//			Short[] tArr = list.toArray(new Short[list.size()]);
//			return (arrValType == short.class) ? toPrimitive(tArr) : tArr;
//		} else if (isInteger(arrValType)) {
//			ArrayList<Integer> list = toTypeColl(Integer.class, arr);
//			Integer[] tArr = list.toArray(new Integer[list.size()]);
//			return (arrValType == int.class) ? toPrimitive(tArr) : tArr;
//		} else if (isLong(arrValType)) {
//			ArrayList<Long> list = toTypeColl(Long.class, arr);
//			Long[] tArr = list.toArray(new Long[list.size()]);
//			return (arrValType == long.class) ? toPrimitive(tArr) : tArr;
//		} else if (isFloat(arrValType)) {
//			ArrayList<Float> list = toTypeColl(Float.class, arr);
//			Float[] tArr = list.toArray(new Float[list.size()]);
//			return (arrValType == float.class) ? toPrimitive(tArr) : tArr;
//		} else if (isDouble(arrValType)) {
//			ArrayList<Double> list = toTypeColl(Double.class, arr);
//			Double[] tArr = list.toArray(new Double[list.size()]);
//			return (arrValType == double.class) ? toPrimitive(tArr) : tArr;
//		} else if (isBoolean(arrValType)) {
//			ArrayList<Boolean> list = toTypeColl(Boolean.class, arr);
//			Boolean[] tArr = list.toArray(new Boolean[list.size()]);
//			return (arrValType == boolean.class) ? toPrimitive(tArr) : tArr;
//		} else if (isCharacter(arrValType)) {
//			ArrayList<Character> list = toTypeColl(Character.class, arr);
//			Character[] tArr = list.toArray(new Character[list.size()]);
//			return (arrValType == char.class) ? toPrimitive(tArr) : tArr;
//		} else if (isString(arrValType)) {
//			return arr;
//		} else if (isEnum(arrValType)) {
//			@SuppressWarnings({ "rawtypes", "unchecked" })
//			ArrayList<? extends Enum> list = (ArrayList<? extends Enum>) toTypeColl(
//					arrValType, arr);
//			Object enumArr = Array.newInstance(arrValType, list.size());
//			for (int i = 0; i < list.size(); i++) {
//				Array.set(enumArr, i, list.get(i));
//			}
//			return enumArr;
//		} else if (isUUID(arrValType)) {
//			ArrayList<UUID> list = toTypeColl(UUID.class, arr);
//			return list.toArray(new UUID[list.size()]);
//		} else if (isDate(arrValType)) {
//			ArrayList<Date> list = toTypeColl(Date.class, arr);
//			return list.toArray(new Date[list.size()]);
//		} else if (isJsonObject(arrValType)) {
//			ArrayList<JSONObject> list = toTypeColl(JSONObject.class, arr);
//			return list.toArray(new JSONObject[list.size()]);
//		} else if (isJsonArray(arrValType)) {
//			ArrayList<JSONArray> list = toTypeColl(JSONArray.class, arr);
//			return list.toArray(new JSONArray[list.size()]);
//		} else {
//			throw new IllegalArgumentException("Unable to convert to "
//					+ arrValType + ".");
//		}
//	}
//
//	public static <T> ArrayList<T> toTypeColl(Class<T> valCls,
//			String[] valStrArr) throws IllegalArgumentException {
//		ArrayList<Object> list = new ArrayList<Object>();
//		for (String str : valStrArr) {
//			list.add(parseValue(valCls, str));
//		}
//		@SuppressWarnings("unchecked")
//		ArrayList<T> typedList = (ArrayList<T>) list;
//		return typedList;
//	}
//
//	public static Object parseValue(Class<?> valCls, String valStr)
//			throws IllegalArgumentException {
//		if (isByte(valCls)) {
//			return Byte.valueOf(valStr);
//		} else if (isShort(valCls)) {
//			return Short.valueOf(valStr);
//		} else if (isInteger(valCls)) {
//			return Integer.valueOf(valStr);
//		} else if (isLong(valCls)) {
//			return Long.valueOf(valStr);
//		} else if (isFloat(valCls)) {
//			return Float.valueOf(valStr);
//		} else if (isDouble(valCls)) {
//			return Double.valueOf(valStr);
//		} else if (isBoolean(valCls)) {
//			return Boolean.valueOf(valStr);
//		} else if (isCharacter(valCls)) {
//			return Character.valueOf((valStr.length() == 0) ? ' ' : valStr
//					.charAt(0));
//		} else if (isString(valCls)) {
//			return valStr;
//		} else if (isEnum(valCls)) {
//			return instantiateEnum(valCls, valStr);
//		} else if (isUUID(valCls)) {
//			return UUID.fromString(valStr);
//		} else if (isDate(valCls)) {
//			return new Date(Long.valueOf(valStr));
//		} else if (isJsonObject(valCls) || isJsonArray(valCls)) {
//			try {
//				return isJsonObject(valCls) ? new JSONObject(valStr)
//						: new JSONArray(valStr);
//			} catch (JSONException e) {
//				throw new IllegalArgumentException(e);
//			}
//		} else {
//			throw new IllegalArgumentException("Unable to convert '" + valStr
//					+ "' to " + valCls.getSimpleName() + ".");
//		}
//	}

	protected TypeHelper() {
	}
}
