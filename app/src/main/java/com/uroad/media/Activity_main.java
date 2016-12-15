package com.uroad.media;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.uroad.util.DialogHelper;
import com.uroad.widget.slidingmenu.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/10/20.
 */
public class Activity_main extends Activity {
    private ImageView img_play;
    private PlayService playService;
    private MediaPlayer mediaplay;
    private GridView gdv_musiclist;
    private Getmusiclist getmusictask;
    private Button btn_dianbo;
    private LinearLayout layout_dianbo;
    private Button gouxuan;
    private Dialog dialog;
    private SlidingMenu menu;
    private List<String> urls = new ArrayList<>();

    private List<DianboMDL> list = new ArrayList<DianboMDL>();
    private ThisAdapter adapter;
    private int playIndex = 0;
    private TextView tv_namelist;
    private SeekBar seb_seekbar;
    private TextView tv_one, tv_two;
    private int ms = 0;
    private View leftView;
    private ImageView img_set;
    private  int ms2 = 0;
    private Timer timer;

    private ServiceConnection connplay = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            playService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            playService = ((PlayService.MyBinder) service).getService();
            mediaplay = playService.returnMediaPlayer();
            mediaplay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    System.out.print("");
                    final MyHandler myhandler = new MyHandler();
                    TimerTask task = new TimerTask() {
                        public void run() {

                            Message message = new Message();
                            message.what = 1;
                            myhandler.sendMessage(message);

                        }

                    };
                     timer = new Timer(true);
                    timer.schedule(task, 0, 1000);
                    DialogHelper.showTost(Activity_main.this, "缓冲完成");
                    int max = (mediaplay.getDuration())/1000;
                    seb_seekbar.setMax(max);
                    mediaplay.start();
                }
            });

            mediaplay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    timer.cancel();
                    ms=0;
                    ms2=0;
                    DialogHelper.showTost(Activity_main.this, "播放完成");
                    if (playIndex < urls.size() - 1) {
                        playService.play(urls.get(++playIndex));

                    } else {
                        playIndex = 0;
                    }
                }
            });


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initMenu();
        getDialog();
        intM();
        getmusictask = new Getmusiclist();
        getmusictask.execute();
    }


        private  void intM(){
            seb_seekbar=(SeekBar)findViewById(R.id.seb_seekbar);
            tv_one = (TextView) findViewById(R.id.tv_one);
            tv_two = (TextView) findViewById(R.id.tv_two);
            img_set=(ImageView)findViewById(R.id.img_set);
            gdv_musiclist = (GridView) dialog.findViewById(R.id.gdv_musiclist);
            gouxuan = (Button) dialog.findViewById(R.id.btn_gouxuan);
            layout_dianbo = (LinearLayout) findViewById(R.id.layout_dianbo);
            img_play = (ImageView) findViewById(R.id.img_play);
            btn_dianbo = (Button) findViewById(R.id.btn_dianbo);
            tv_namelist = (TextView) findViewById(R.id.tv_namelist);


            //    tv_namelist.setText(list.get(playIndex).getName()) ;


            img_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.showMenu();
                }
            });
            btn_dianbo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog.show();


                    //visibility
                }
            });

            gouxuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isCheck()) {
                            urls.add(list.get(i).getUrl());

                        }

                    }
                    if (urls.size() == 0) {
                        DialogHelper.showTost(Activity_main.this, "请选择");
                    } else {

                        dialog.dismiss();
                        playService.play(urls.get(playIndex));
                    }
                }
            });
            adapter = new ThisAdapter();
            gdv_musiclist.setAdapter(adapter);

            img_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaplay.start();
            

                }
            });


        }
    private void getDialog() {
        dialog = new Dialog(Activity_main.this, R.style.MyDialog);
        dialog.setContentView(R.layout.dialog_layout);
        Window window = dialog.getWindow();

        window.setGravity(Gravity.BOTTOM);
    }

    class ThisAdapter extends BaseAdapter {
        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return list.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater minflater = getLayoutInflater();
                convertView = minflater.inflate(R.layout.item, null);
                holder.ckb_gouxuan = (CheckBox) convertView.findViewById(R.id.ckb_gouxuan);
                holder.tv_geming = (TextView) convertView.findViewById(R.id.tv_geming);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_geming.setText(list.get(position).getName());
            holder.ckb_gouxuan.setChecked(list.get(position).isCheck());
            holder.ckb_gouxuan.setOnCheckedChangeListener(new MyCheckListener(position));
            return convertView;
        }

        class MyCheckListener implements OnCheckedChangeListener {
            private int position = -1;

            public MyCheckListener(int p) {
                position = p;
            }

            /**
             * Called when the checked state of a compound button has changed.
             *
             * @param buttonView The compound button view whose state has changed.
             * @param isChecked  The new checked state of buttonView.
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setCheck(isChecked);

            }

        }

        class ViewHolder {
            public TextView tv_geming;
            public CheckBox ckb_gouxuan;
        }
    }
    public View makeView(){
        LayoutInflater ninflater = getLayoutInflater();
        leftView = ninflater.inflate(R.layout.fragment_left, null);
        return leftView;
    }


    public void initMenu() {


        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        menu.attachToActivity((Activity)this,SlidingMenu.SLIDING_CONTENT);
        menu.setFadeDegree(0.35f);
        menu.setBehindOffset(70);
        menu.setMenu(makeView());

    }

    public static String toTime(int time) {
        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        return String.format("%02d:%02d", minute, second);
    }

    class MyHandler extends Handler {


        // 子类必须重写此方法,接受数据
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int ms1;
                    ms1 = mediaplay.getDuration();

                    tv_two.setText(toTime(ms1));


                    tv_one.setText(toTime(mediaplay.getCurrentPosition()));
                        int ms3;
                    ms3=mediaplay.getCurrentPosition() / 1000;
                    seb_seekbar.setProgress(ms3);
                    Log.e("------" , "positopn = " + ms3  +" --- max = " + seb_seekbar.getMax());




                    break;
                case 2:

                    break;
            }

        }
    }

    public void init() {
        Intent intent = new Intent();
        intent.setClass(Activity_main.this, PlayService.class);
        bindService(intent, connplay, BIND_AUTO_CREATE);


    }

    class Getmusiclist extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                result = new JSONObject("{\n" +
                        "  \"status\": \"OK\",\n" +
                        "  \"data\": [\n" +
                        "    {\n" +
                        "      \"id\": 1,\n" +
                        "      \"name\": \"全市路况\",\n" +
                        "      \"url\": \"http://mp3.13400.com:99/20150512/82.mp3\",\n" +
                        "      \"itype\": 0,\n" +
                        "      \"intime\": \"2016-10-20 11:12:42\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 2,\n" +
                        "      \"name\": \"全市路况\",\n" +
                        "      \"url\": \"http://mp3.13400.com:99/20150512/80.mp3\",\n" +
                        "      \"itype\": 0,\n" +
                        "      \"intime\": \"2016-10-20 11:12:42\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"id\": 3,\n" +
                        "      \"name\": \"全市路况\",\n" +
                        "      \"url\": \"http://mp3.13400.com:99/20150512/98.mp3\",\n" +
                        "      \"itype\": 0,\n" +
                        "      \"intime\": \"2016-10-20 11:12:42\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}");
            } catch (JSONException e) {

            }

            super.onPostExecute(result);
            if (JUtil.GetJsonStatu(result)) {
                List<DianboMDL> datas = JUtil.fromJson(result, new TypeToken<List<DianboMDL>>() {
                }.getType());
                list.clear();
                list.addAll(datas);


            }
        }


    }
}
