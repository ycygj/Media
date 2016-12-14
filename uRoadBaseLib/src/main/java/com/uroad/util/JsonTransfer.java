package com.uroad.util;

import java.lang.reflect.Type;
import org.json.JSONObject;
import com.google.gson.Gson;

public class JsonTransfer {
	
	/**
	 *new TypeToken<List<T>>(){}.getType()
	 * 
	 */
	public static <T> T fromJson(String json,Type typeOfT)
	{
		return fromJson(json,typeOfT,"data");		 
	}
	
	public static <T> T fromJson(String json,Type typeOfT,String name)
	{
		try {
			JSONObject jsonObject = new JSONObject(json);
			return fromJson(jsonObject, typeOfT,name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *new TypeToken<List<T>>(){}.getType()
	 * 
	 */
	public static <T> T fromJson(JSONObject json,Type typeOfT)
	{ 
		return fromJson(json,typeOfT,"data");
	}
	
	/**
	 *new TypeToken<List<T>>(){}.getType()
	 * 
	 */
	public static <T> T fromJson(JSONObject json,Type typeOfT,String name)
	{ 
		Gson gson = new Gson();
		String jsonString="";
		try {
			jsonString=json.getString(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.fromJson(jsonString, typeOfT);
	}
	
	public static <T> T fromJson(JSONObject json, Class<T> classOfT)
	{
		return fromJson(json, classOfT,"data");
	}
	
	public static <T> T fromJson(JSONObject json, Class<T> classOfT,String name)
	{		
		Gson gson = new Gson();
		String jsonString="";
		try {
			jsonString=json.getJSONArray(name).getString(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gson.fromJson(jsonString, classOfT);
	}
	
	public static String toJson(Object obj )
	{
		Gson gson = new Gson();
		String jsonString="";
		try {
			jsonString=gson.toJson(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public static <T> T simpleFromJson(String str,Class<T> classOfT)
	{
		Gson gson = new Gson();
		return gson.fromJson(str, classOfT);
	}
	
	public static <T> T simpleFromJson(String str,Type typeOfT)
	{
		Gson gson = new Gson();
		return gson.fromJson(str, typeOfT);
	}
}
