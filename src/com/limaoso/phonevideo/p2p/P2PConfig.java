package com.limaoso.phonevideo.p2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.limaoso.phonevideo.utils.DebugLog;

public class P2PConfig {

	static {
		System.loadLibrary("gnustl_shared");
		System.loadLibrary("miniupnpc");
		System.loadLibrary("apr-1");
		System.loadLibrary("apriconv-1");
		System.loadLibrary("expat");
		System.loadLibrary("aprutil-1");
		System.loadLibrary("lmp2ps");
		System.loadLibrary("lmp2p");
		System.loadLibrary("lmndkp2p");
	}
	private Context mContext;
	private final static P2PConfig instance = new P2PConfig();

	public static P2PConfig getInstance() {
		return instance;
	}

	public OnConnectionChangeListener mConnectionChangeListener;
	private String TAG = getClass().getSimpleName();

	/**
	 * 初始化P2P
	 */
	public void init(Context context) {
		this.mContext = context;
		startService();
		connectionChangeReceiver();
	}

	/**
	 * 开启服务
	 */
	private void startService() {
		try {
			P2PManager.getInstance(mContext).startP2PService();
		} catch (Exception e) {
			DebugLog.e(TAG, "startP2PService() 开启P2P服务崩溃");
		}
	}

	/**
	 * 开启网络变化广播
	 */
	private void connectionChangeReceiver() {
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		ConnectionChangeReceiver myReceiver = new ConnectionChangeReceiver();
		mContext.registerReceiver(myReceiver, filter);
	}

	public class ConnectionChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
				// UIUtils.showToast(context, "网络不可以用");
				if (mConnectionChangeListener != null) {
					mConnectionChangeListener
							.onConnectionChange(PhoneNetType.NOT_NET);
				}
				try {
					P2PManager.getInstance(mContext).notifyWifi(0);
				} catch (Exception e) {
					DebugLog.e(TAG, "p2pWiFi ：notifyWifi(0)  通知崩溃");
				}

			} else if (mobNetInfo.isConnected()) {
				// UIUtils.showToast(context, "网络切换到2G/3G/4G网络状态");
				if (mConnectionChangeListener != null) {
					mConnectionChangeListener
							.onConnectionChange(PhoneNetType.PHONE_NET);
				}
				try {
					P2PManager.getInstance(mContext).notifyWifi(0);
				} catch (Exception e) {
					DebugLog.e(TAG, "p2pWiFi notifyWifi(0)  通知崩溃");
				}

			} else if (wifiNetInfo.isConnected()) {
				if (mConnectionChangeListener != null) {
					mConnectionChangeListener
							.onConnectionChange(PhoneNetType.WIFI_NET);
				}
				// UIUtils.showToast(context, "wifi网络已连接");
				try {
					P2PManager.getInstance(mContext).notifyWifi(1);
				} catch (Exception e) {
					DebugLog.e(TAG, "p2pWiFi ：notifyWifi(1)  通知崩溃");
				}

			}

		}
	}

	/**
	 * 网络类型
	 * 
	 * @author jjm
	 * 
	 */
	public enum PhoneNetType {
		WIFI_NET, PHONE_NET, NOT_NET
	}

	/**
	 * 网络监听
	 * 
	 * @author jjm
	 * 
	 */
	public interface OnConnectionChangeListener {
		void onConnectionChange(PhoneNetType type);
	}

	/**
	 * 注册网络变化的监听
	 * 
	 * @param mConnectionChangeListener
	 */
	public void setOnConnectionChangeListener(
			OnConnectionChangeListener mConnectionChangeListener) {
		this.mConnectionChangeListener = mConnectionChangeListener;
	}

}
