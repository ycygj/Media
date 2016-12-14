package com.uroad.media;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class PlayService extends Service {

	private MediaPlayer mediaPlayer; // 媒体播放器对象
	private boolean isPause;

	public class MyBinder extends Binder {

		public PlayService getService() {
			return PlayService.this;
		}
	}

	public MediaPlayer returnMediaPlayer() {
		return mediaPlayer;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MyBinder();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mediaPlayer = new MediaPlayer();
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void play(String path) {
		try {
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepareAsync();// 进行缓冲
			// mediaPlayer.setOnPreparedListener(new PreparedListener());
			// 注册一个监听器
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void pause() {
		Log.i("PLAY", "pause");
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			isPause = true;
		}
	}

	public void resume() {
		Log.i("PLAY", "resume");
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}
}
