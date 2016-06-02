package com.limaoso.phonevideo.p2p;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.limaoso.phonevideo.download.DownServerControl;
import com.limaoso.phonevideo.utils.DebugLog;
import com.limaoso.phonevideo.utils.SDUtils;

public class P2PManagerThread extends BaseThreadExecutor {
	private String TAG = "P2PManagerThread";
	private DownServerControl p2pControl;
	private static Context mContext;
	// private int threadID;
	// 字符串参数
	// private String strParameter;
	// // int类型参数
	// private int intParameter;
	// // 行为状态码
	// private int actionCode;

	// 开启P2P服务
	public final static int FLAG_SATART_SERVICE_ACTION = 1;
	// 关闭P2P服务
	public final static int FLAG_STOP_SERVICE_ACTION = 2;
	// 改变文件路径
	public final static int FLAG_NOTIFY_FILEPATH_ACTION = 3;
	// 停止P2P缓存
	public final static int FLAG_STOP_P2PCACHE_ACTION = 4;
	// 删除文件
	public final static int FLAG_DELETE_FILE_ACTION = 5;
	// 通知WiFi改变
	public final static int FLAG_NOTIFY_WIFI_ACTION = 6;
	// 缓存P2P文件
	public final static int FLAG_CACHE_P2P_FILE_ACTION = 7;

	private P2PManagerThread() {
		if (p2pControl == null) {
			p2pControl = new DownServerControl();
		}

	}

	private static P2PManagerThread instance = new P2PManagerThread();

	public static P2PManagerThread getInstance(Context context) {
		mContext = context;
		return instance;
	}

	@Override
	protected void disposeTask(int taskType, Object curentobj) {
		DebugLog.e("",
				"任务在子线程： "
						+ (Thread.currentThread() != Looper.getMainLooper()
								.getThread()));
		switch (taskType) {
		case FLAG_SATART_SERVICE_ACTION:
			long id = Thread.currentThread().getId();
			// String rootDataPath = SDUtils.getRootDataPath("files");
			String rootDataPath = mContext.getPackageName();
			String downFileDir = SDUtils.getDownFileDir(mContext);
			DebugLog.e(TAG, "rootDataPath : " + rootDataPath);
			DebugLog.e(TAG, "downFileDir : " + downFileDir);
			DebugLog.e(TAG, "线程 ID : " + id);
			p2pControl.startServer(new String[] { rootDataPath, downFileDir });
			DebugLog.e(TAG, "收到用户行为 ：开启p2p服务");
			break;
		case FLAG_STOP_SERVICE_ACTION:
			p2pControl.stopServer();
			DebugLog.e(TAG, "收到用户行为 ：停止p2p服务");
			break;
		case FLAG_NOTIFY_FILEPATH_ACTION:
			p2pControl.notifyFilePath((String) curentobj);
			DebugLog.e(TAG, "收到用户行为 ：通知路径变化");
			break;
		case FLAG_STOP_P2PCACHE_ACTION:
			p2pControl.stopP2PCache((String) curentobj);
			DebugLog.e(TAG, "收到用户行为 ：停止缓存文件hash ： " + curentobj);
			break;
		case FLAG_NOTIFY_WIFI_ACTION:
			p2pControl.notifyWifi(((Integer) curentobj).intValue());
			DebugLog.e(TAG, "收到用户行为 ：通知WiFi变化 ： " + curentobj);
			break;
		case FLAG_DELETE_FILE_ACTION:
			p2pControl.deleteFile((String) curentobj);
			DebugLog.e(TAG, "收到用户行为 ：删除文件 hash ： " + curentobj);
			break;
		case FLAG_CACHE_P2P_FILE_ACTION:
			p2pControl.cacheP2PFile((String) curentobj);
			DebugLog.e(TAG, "收到用户行为 ： 缓存P2p文件 hash  : " + curentobj);
			break;
		}

	}

	// @Override
	// public void run() {
	// super.run();
	//
	//
	// }

	/**
	 * 开启P2P服务
	 */
	public void startP2Pservice() {
		// actionCode = FLAG_SATART_SERVICE_ACTION;
		// run();
		addTask(FLAG_SATART_SERVICE_ACTION);
	}

	public void stopService() {
		addTask(FLAG_STOP_SERVICE_ACTION);
		// actionCode = FLAG_STOP_SERVICE_ACTION;
		// run();
	}

	/**
	 * 修改文件保存路径
	 * 
	 * @param path
	 */
	public void notifyFilePath(String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		addTask(FLAG_NOTIFY_FILEPATH_ACTION, path);
		// strParameter = path;
		// actionCode = FLAG_NOTIFY_FILEPATH_ACTION;
		// run();
	}

	public void stopP2PCache(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		addTask(FLAG_STOP_P2PCACHE_ACTION, uri);
		// strParameter = uri;
		// actionCode = FLAG_STOP_P2PCACHE_ACTION;
		// run();
	}

	public void deleteFile(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		addTask(FLAG_DELETE_FILE_ACTION, uri);
		// strParameter = uri;
		// actionCode = FLAG_DELETE_FILE_ACTION;
		// run();
	}

	public void notifyWifi(int flag) {
		// intParameter = flag;
		// actionCode = FLAG_NOTIFY_WIFI_ACTION;
		// run();
		addTask(FLAG_NOTIFY_WIFI_ACTION, flag);
	}

	public void cacheP2PFile(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		addTask(FLAG_CACHE_P2P_FILE_ACTION, uri);
		// strParameter = uri;
		// actionCode = FLAG_CACHE_P2P_FILE_ACTION;
		// run();
	}

}
