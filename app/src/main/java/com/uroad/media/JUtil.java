package com.uroad.media;

import android.text.format.DateFormat;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/25.
 */


public class JUtil {
    public static boolean GetJsonStatu(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取接口返回的错误信息
     *
     * @Title: GetErrorString
     * @Description: TODO
     * @param jsonObject
     * @param key
     * @return
     * @return: String
     */


    /**
     *
     * @Title: GetnormalString
     * @Description: TODO
     * @param jsonObject
     * @param key
     * @return
     * @return: String
     */
    public static String GetnormalString(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static String GetJsonString(String val) {
        String reString = "";
        if (val.toLowerCase().equals("null"))
            return reString;
        return val;
    }

    public static String GetString(JSONObject jsonObject, String key) {
        try {
            return GetJsonString(jsonObject.getJSONObject("data")
                    .getString(key));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return "";
        }
    }

    /**
     * 没有data目录的获取
     *
     * @Title: GetString
     * @Description: TODO
     * @param jsonObject
     * @param key
     * @param aa
     * @return
     * @return: String
     */
    public static String GetString(JSONObject jsonObject, String key, String aa) {
        try {
            return GetJsonString(jsonObject.getString(key));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return "";
        }
    }

    public static String GetString(Map<String, String> map, String key) {
        try {
            return GetJsonString(map.get(key));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            return "";
        }
    }



    public static JSONObject GetJsonObject(JSONObject jsonObject, String key) {
        try {
            return new JSONObject(GetString(jsonObject, key));
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    // 序列化类为字符串
    public static String object2String(Object obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(obj);
            String serStr = byteArrayOutputStream.toString("ISO-8859-1");
            serStr = java.net.URLEncoder.encode(serStr, "UTF-8");

            objectOutputStream.close();
            byteArrayOutputStream.close();
            return serStr;
        } catch (Exception e) {
            // TODO: handle exception
            return "";
        }

    }

    // 反序列化字符串为对象
    @SuppressWarnings("unchecked")
    public static Object getObjectFromString(String serStr) {
        try {
            String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            return object;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    public static boolean GetJsonStatu(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public static void decodeJSONObject(JSONObject obj) {
        if (obj == null)
            return;
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = (String) it.next();

            try {
                JSONArray array = obj.getJSONArray(key);
                if (array != null && array.length() > 0) {
                    decodeJSONArray(array);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

            try {
                JSONObject jsonobject = obj.getJSONObject(key);
                if (jsonobject != null) {
                    decodeJSONObject(jsonobject);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                Object value = obj.get(key);
                if (value != null) {
                    if (value instanceof String) {
                        String val = URLDecoder.decode(value.toString());
                        obj.put(key, val);

                    } else {
                        obj.put(key, value);
                    }
                }
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public static void decodeJSONArray(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject jsonobject = array.getJSONObject(i);
                decodeJSONObject(jsonobject);
                continue;
            } catch (Exception e) {
                // TODO: handle exception
            }
            try {
                JSONArray jsonArray = array.getJSONArray(i);
                if (jsonArray != null && jsonArray.length() > 0) {
                    decodeJSONArray(jsonArray);
                    continue;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return fromJson(jsonObject, typeOfT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static <T> T fromJson(JSONObject json, Type typeOfT) {

        Gson gson = new Gson();
        String jsonString = "";
        try {
            jsonString = json.getString("data");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return gson.fromJson(jsonString, typeOfT);
    }

    public static <T> T fromJson(JSONObject json, Class<T> classOfT) {

        Gson gson = new Gson();
        String jsonString = "";
        try {
            jsonString = json.getString("data");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return gson.fromJson(jsonString, classOfT);
    }

    public static String toJson(Object obj) {
        Gson gson = new Gson();
        String jsonString = "";
        try {
            jsonString = gson.toJson(obj);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonString;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {

        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

}

