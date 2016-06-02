package com.limaoso.phonevideo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import com.limaoso.phonevideo.download.DownServerControl;
import com.limaoso.phonevideo.p2p.P2PManagerThread;
import com.limaoso.phonevideo.p2p.TimerThreadManager;
import com.limaoso.phonevideo.utils.DebugLog;

public class P2pService extends Service {

	public static final String TAG = "P2PService2";
	private Context mContext;
	// P2P管理类
	private DownServerControl p2pControl;

	private String currentTaskName;

	public class MyBinder extends Binder {
		public Service getService() {
			return P2pService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			P2PManagerThread.getInstance(mContext).startP2Pservice();
		} catch (Exception e) {
			// TODO: handle exception
		}
		DebugLog.e(TAG, "onStartCommand开启");
		return super.onStartCommand(intent, flags, startId);
	}

	// @SuppressWarnings("deprecation")
	// @Override
	// public void onStart(Intent intent, int startId) {
	// super.onStart(intent, startId);
	// getP2PManagerThread().startP2Pservice();
	// }

	@Override
	public void onDestroy() {
		P2PManagerThread.getInstance(mContext).stopService();
		super.onDestroy();
	}

	/**
	 * 获取P2管理类
	 * 
	 * @return
	 */
	private DownServerControl getDownServerControl() {
		if (p2pControl == null) {
			p2pControl = new DownServerControl();
		}
		return p2pControl;
	}

	/**
	 * 缓存P2P文件
	 * 
	 * @param uri
	 */
	public void cacheP2PFile(String uri) {
		currentTaskName = uri;
		P2PManagerThread.getInstance(mContext).cacheP2PFile(uri);
	}

	/**
	 * 删除文件
	 * 
	 * @param uri
	 */
	public void deleteP2PFile(String uri) {
		if (!TextUtils.isEmpty(uri) && uri.equals(getCurrentTaskName())) {
			currentTaskName = "";
		}
		P2PManagerThread.getInstance(mContext).deleteFile(uri);
	}

	/**
	 * 添加轮询任务
	 * 
	 * @param uri
	 * @return
	 */
	public void addQueryTask(Runnable runnable) {
		TimerThreadManager.getInstance().addTask(runnable);
	}

	/**
	 * 移除轮询任务
	 * 
	 * @param runnable
	 */
	public void removeQueryTask(Runnable runnable) {
		TimerThreadManager.getInstance().removeTask(runnable);
	}

	/**
	 * 暂停任务
	 * 
	 * @param uri
	 */
	public void stopP2PCache(String uri) {
		if (!TextUtils.isEmpty(uri)) {
			currentTaskName = "";
		}
		P2PManagerThread.getInstance(mContext).stopP2PCache(uri);
	}

	/**
	 * 暂停任务
	 * 
	 * @param uri
	 *            flag 0 是没有WiFi状态 1 有WiFi状态
	 */
	public void notifyWifi(int flag) {
		P2PManagerThread.getInstance(mContext).notifyWifi(flag);
	}

	/**
	 * 获取当前下载任务名称
	 * 
	 * @return
	 */
	public String getCurrentTaskName() {

		return currentTaskName;
	}

}
