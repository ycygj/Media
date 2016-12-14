package com.uroad.media;

import android.content.Context;

import com.uroad.net.RequestParams;
import com.uroad.net.SyncHttpClient;
import com.uroad.webserverce.BaseWebService;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/25.
 */
public class PlaymusicWS extends BaseWebService {

    public PlaymusicWS(Context context) {
  //      super(context);
        this.ct = context;
        // TODO Auto-generated constructor stub
    }

    Context ct;


    public JSONObject getmusiclist() {

        try {
            String url = ("http://ushop.u-road.com/cwwebCommon/Test/test1");
            RequestParams params = new RequestParams();
            JSONObject result = new SyncHttpClient().postToJson(url, params);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

}