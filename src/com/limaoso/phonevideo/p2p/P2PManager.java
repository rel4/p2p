package com.limaoso.phonevideo.p2p;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import com.limaoso.phonevideo.download.DownServerControl;
import com.limaoso.phonevideo.download.DownloadFileInfo;
import com.limaoso.phonevideo.service.P2pService;
import com.limaoso.phonevideo.utils.DebugLog;
import com.limaoso.phonevideo.utils.SDUtils;

public class P2PManager {
	private String TAG = "P2PManager";
	private P2pService us;
	private static Context mContext;
	private final static P2PManager instance = new P2PManager();
	private List<String> taskList = new ArrayList<String>();
	private DownServerControl mControl;
	private boolean ishaveCacheFile = false;
	private List<P2PCacheListener> cacheListeners = new ArrayList<P2PCacheListener>();

	/**
	 * p2p 内存监听
	 * 
	 * @author jb
	 * 
	 */
	public interface P2PCacheListener {
		// 缓存空间是否可用
		void isMemorySpaceUsable(String fileHash, boolean isUsable);
	}

	private P2PManager() {
	}

	/**
	 * 获取P2P管理对象
	 * 
	 * @return
	 */
	public static P2PManager getInstance(Context context) {
		mContext = context;
		return instance;
	}

	/**
	 * 服务开启
	 */
	private void startConnectionService() {
		if (!isServiceRunning(P2pService.class)) {
			mContext.startService(new Intent(mContext, P2pService.class));
		}
		if (us == null) {
			mContext.bindService(new Intent(mContext, P2pService.class), conn,
					Context.BIND_AUTO_CREATE);
		}
	}

	/**
	 * 服务连接对象
	 */
	private ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			us = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			DebugLog.e(this, "P2P服务连接成功");
			if (service instanceof P2pService.MyBinder) {
				P2pService.MyBinder binder = (P2pService.MyBinder) service;
				Service sv = binder.getService();
				if (sv instanceof P2pService) {
					us = (P2pService) sv;
				}

			}

		}
	};

	/**
	 * 判断服务开启
	 * 
	 * @param serviceName
	 * @return
	 */
	private boolean isServiceRunning(Class clz) {
		String serviceName = clz.getName();
		ActivityManager manager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 开启服务
	 */
	public void startP2PService() {
		if (us == null) {
			startConnectionService();
		} else {

		}
		// if (us == null) {
		// addQureyTask(startService);
		// DebugLog.v(TAG, "再次开启服务startP2PService");
		// }

	}

	// private Runnable startService = new Runnable() {
	//
	// @Override
	// public void run() {
	// if (us==null) {
	//
	// }else {
	// removeTask(startService);
	// }
	// }
	// };
	/**
	 * 缓存P2P文件
	 * 
	 * @param uri
	 */
	public void cacheP2PFile(String uri) {
		if (!taskList.contains(uri)) {
			taskList.add(uri);
		}
		DebugLog.v(TAG, "添加缓存任务， 缓存任务共有 ： " + taskList.size() + "个");
		if (!ishaveCacheFile) {
			DebugLog.v(TAG, "开始缓存");
			ishaveCacheFile = true;
			cacheNextP2pFile();
		}
	}

	/**
	 * 开始缓存下个文件
	 * 
	 * @return
	 */
	private boolean cacheNextP2pFile() {
		DebugLog.v(TAG, "开始下个缓存任务");
		boolean isSucceed;
		if (us == null) {
			startP2PService();
			DebugLog.v(TAG, "缓存任务,p2p服务没有连接");
			isSucceed = false;
		} else {
			if (taskList.size() == 0) {
				ishaveCacheFile = false;
				DebugLog.v(TAG, "---------缓存队列任务完成---------");
			} else {
				String currentTaskName = getCurrentTaskName();
				if (TextUtils.isEmpty(currentTaskName)) {
					us.cacheP2PFile(taskList.get(0));
					currentTaskStartTime = System.currentTimeMillis();
					DebugLog.v(TAG, "开始缓存下个任务 ， 文件hash ： " + taskList.get(0));
					addQureyTask(queryTask);
				}
			}
			isSucceed = true;

		}
		return isSucceed;
	}

	/**
	 * 删除文件
	 * 
	 * @param uri
	 * @return
	 */
	public boolean deleteFile(String uri) {
		boolean isSucceed;
		if (us != null) {
			if (!TextUtils.isEmpty(uri) && uri.equals(us.getCurrentTaskName())) {
				ishaveCacheFile = false;
			}
			us.deleteP2PFile(uri);
			if (taskList.contains(uri)) {
				taskList.remove(uri);
			}
			isSucceed = true;
		} else {
			startConnectionService();
			isSucceed = false;
		}
		return isSucceed;
	}

	/**
	 * 暂停任务
	 * 
	 * @param uri
	 */
	public boolean stopP2PCache(String uri) {
		boolean isSucceed;
		if (us != null) {
			us.stopP2PCache(uri);
			isSucceed = true;
		} else {
			startP2PService();
			isSucceed = false;
		}
		return isSucceed;
	}

	/**
	 * 通知WiFi改变
	 * 
	 * @param flag
	 *            0是没有WiFi状态 1有WiFi状态
	 */
	public boolean notifyWifi(int flag) {
		boolean isSucceed = false;
		if (us != null) {
			us.notifyWifi(flag);
			DebugLog.v(TAG, flag == 0 ? "通知WiFi关闭" : "通知WiFi打开");
			isSucceed = true;
			// } else {
			// startP2PService();
			// DebugLog.v(TAG, "通知WiFi打开失败");
			// isSucceed = false;
		}
		return isSucceed;
	}

	/**
	 * 添加任务
	 * 
	 * @param runnable
	 * @return
	 */
	public boolean addQureyTask(Runnable runnable) {
		boolean isSucceed;
		if (us == null) {
			startP2PService();
			isSucceed = false;
		} else {
			us.addQueryTask(runnable);
			isSucceed = true;
		}
		return isSucceed;
	}

	/**
	 * 移除任务
	 * 
	 * @param runnable
	 * @return
	 */
	public boolean removeTask(Runnable runnable) {

		boolean isSucceed;
		if (us == null) {
			startP2PService();
			isSucceed = false;
		} else {
			us.removeQueryTask(runnable);
			isSucceed = true;
		}
		return isSucceed;
	}

	/**
	 * 获取当前下载任务的名称
	 * 
	 * @return
	 */
	public String getCurrentTaskName() {
		if (us == null) {
			return null;
		}
		return us.getCurrentTaskName();
	}

	private DownServerControl getDownServerControl() {
		if (mControl == null) {
			mControl = new DownServerControl();
		}
		return mControl;
	}

	/**
	 * 缓存页面缓存文件
	 */
	public void startCachePageFile(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		String currentTaskName = getCurrentTaskName();
		if (!uri.equals(currentTaskName)) {
			stopP2PCache(uri);
			removeTask(queryTask);
			if (taskList.contains(uri)) {
				taskList.remove(uri);
			}
			taskList.add(0, uri);
			cacheNextP2pFile();
		}

	}

	public void stopCachePageFile(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return;
		}
		String currentTaskName = getCurrentTaskName();
		if (uri.equals(currentTaskName)) {
			stopP2PCache(uri);
			if (taskList.contains(uri)) {
				taskList.remove(uri);
			}
			cacheNextP2pFile();
		}

	}

	/**
	 * 查询任务
	 */
	private Runnable queryTask = new Runnable() {

		@Override
		public void run() {

			long id = Thread.currentThread().getId();
			DebugLog.v(TAG, Thread.currentThread() == Looper.getMainLooper()
					.getThread() ? "主线程运行 id : " + id : "子线程运行");
			String currentTaskName = getCurrentTaskName();
			if (TextUtils.isEmpty(currentTaskName)) {
				removeTask(queryTask);
				return;
			}
			DownloadFileInfo filesInfo = getDownServerControl().getFilesInfo(
					currentTaskName);
			long startQueryTime = System.currentTimeMillis();
			// text(filesInfo);
			if (filesInfo != null) {
				long dlSize = filesInfo.dlSize;
				long fileSize = filesInfo.fileSize;
				long dlSpeed = filesInfo.dlSpeed;
				// if (fileSize!=0&&dlSize==fileSize) {
				// 文件下载完成
				if (fileSize != 0 && dlSize >= fileSize) {
					DebugLog.v(TAG,
							"----------------------文件下载完成------------------------------");
					if (taskList.contains(currentTaskName)) {
						// taskList.remove(currentTaskName);
						DebugLog.v(TAG, "hash : " + currentTaskName
								+ "下载文件--完成--任务，开始下个文件");
						DebugLog.v(TAG, "完成文件文件路径 : " + filesInfo.filePath
								+ "文件，开始下个文件");
						// removeTask(queryTask);
						// cacheNextP2pFile();
						timeOutAndNextTask(currentTaskName);
						return;
					}
					// 查询下载文件信息为空超时
				} else if (fileSize == 0) {
					DebugLog.v(TAG,
							"----------------------查询下载文件信息为空超时------------------------------");
					if ((startQueryTime - currentTaskStartTime) >= TIME_OUT) {
						DebugLog.v(TAG, "hash ：  " + currentTaskName
								+ "下载文件--查询--超时 ,继续下个任务");
						// removeTask(queryTask);
						// cacheNextP2pFile();
						timeOutAndNextTask(currentTaskName);
						return;
					} else {
						DebugLog.v(TAG, "超时 ："
								+ formatData(startQueryTime
										- currentTaskStartTime));
					}

					// DebugLog.v(TAG, "dlSize : " +
					// NetWorkUtil.FormetFileSize((filesInfo.dlSize)));
					// 查询到文件，下载文件大小为0，超时
				} else if (fileSize > SDUtils.getCacheAvailableSize(mContext)) {
					if (cacheListeners != null && cacheListeners.size() != 0) {
						List<P2PCacheListener> copyList = new ArrayList<P2PManager.P2PCacheListener>();
						copyList.clear();
						copyList.addAll(cacheListeners);
						for (P2PCacheListener p2pCacheListener : copyList) {
							p2pCacheListener.isMemorySpaceUsable(
									currentTaskName, true);
						}
					}
					timeOutAndNextTask(currentTaskName);
					return;
				} else if (fileSize != 0 && dlSize == 0) {
					DebugLog.v(TAG,
							"----------------------查询到文件，下载文件--大小--为0，超时------------------------------");
					if (!isDownFileSizeTimeOut) {
						downFileTimeOutStartTime = startQueryTime;
						isDownFileSizeTimeOut = true;
						DebugLog.v(TAG, "hash ：  " + currentTaskName
								+ "文件下载--大小-超时开始");
					} else {
						if (startQueryTime - downFileTimeOutStartTime >= TIME_OUT) {
							DebugLog.v(TAG, "hash ：  " + currentTaskName
									+ "文件下载--大小--超时 ,继续下个任务");
							timeOutAndNextTask(currentTaskName);
							return;
						} else {
							DebugLog.v(TAG, "超时 ："
									+ formatData(startQueryTime
											- downFileTimeOutStartTime));
						}
					}
					// 查询到文件，下载文件速度大小为0超时
				} else if (fileSize != 0 && dlSize != 0 && dlSpeed <= 0) {
					DebugLog.v(TAG,
							"----------------------查询到文件，下载文件--速度--为0超时------------------------------");
					if (!isDownFileSpeedTimeOut) {
						downFileSpeedTimeOutStartTime = startQueryTime;
						isDownFileSpeedTimeOut = true;
						DebugLog.v(TAG, "hash ：  " + currentTaskName
								+ "文件下载--速度--超时开始");
					} else {
						if (startQueryTime - downFileSpeedTimeOutStartTime >= TIME_OUT) {
							DebugLog.v(TAG, "hash ：  " + currentTaskName
									+ "文件下载--速度--超时 ,继续下个任务");
							timeOutAndNextTask(currentTaskName);
							return;
						} else {
							DebugLog.v(TAG, "超时 ："
									+ formatData(startQueryTime
											- downFileSpeedTimeOutStartTime));
						}
					}
					// 正常下载
				} else {
					DebugLog.v(TAG,
							"----------------------正常下载------------------------------");
					if (isDownFileSizeTimeOut) {
						isDownFileSizeTimeOut = false;
					}
					if (isDownFileSpeedTimeOut) {
						isDownFileSpeedTimeOut = false;
					}
					// DebugLog.v(
					// TAG,
					// "hash :  "
					// + currentTaskName
					// + "文件正常下载  dlSpeed : "
					// + NetWorkUtil
					// .FormetFileSize((filesInfo.dlSpeed))
					// + "KB/S");
				}
			} else {
				DebugLog.v(TAG,
						"----------------------查收hash为空------------------------------");
				if ((startQueryTime - currentTaskStartTime) >= TIME_OUT) {
					DebugLog.v(TAG, "文件hash ：  " + currentTaskName
							+ "文件下载超时 ,继续下个任务");
					// removeTask(queryTask);
					// cacheNextP2pFile();
					timeOutAndNextTask(currentTaskName);
					return;
				}
			}

		}
	};

	private String formatData(long nowTime) {
		Date date = new Date(nowTime);
		SimpleDateFormat time = new SimpleDateFormat("m:s");
		return time.format(date);
	}

	// 当前任务开始时间
	private long currentTaskStartTime;
	// 设置超时时间
	public final static long TIME_OUT = 5 * 60 * 1000;

	// 查询到文件，下载文件大小为0，超时
	private boolean isDownFileSizeTimeOut = false;

	// 下载文件大小为0超时开始时间
	private long downFileTimeOutStartTime;

	// 下载文件速度为0 超时
	private boolean isDownFileSpeedTimeOut = false;
	// 下载文件速度为0 超时开始时间
	private long downFileSpeedTimeOutStartTime;

	private void timeOutAndNextTask(String currentTaskName) {
		if (isDownFileSizeTimeOut) {
			isDownFileSizeTimeOut = false;
		}
		if (isDownFileSpeedTimeOut) {
			isDownFileSpeedTimeOut = false;
		}
		if (taskList.contains(currentTaskName)) {
			taskList.remove(currentTaskName);
			stopP2PCache(currentTaskName);
			DebugLog.v(TAG, "---------hash ：  " + currentTaskName
					+ "文件移除---------");
		}
		removeTask(queryTask);
		cacheNextP2pFile();

	}

	/**
	 * 注册内存监听
	 */
	public void registerCacheListener(P2PCacheListener listener) {
		if (listener != null) {
			cacheListeners.add(listener);
		}

	}

	/**
	 * 注册内存监听
	 */
	public void unregisterCacheListener(P2PCacheListener listener) {
		if (listener != null && cacheListeners.contains(listener)) {
			cacheListeners.remove(listener);
		}

	}

	/**
	 * 开始任务
	 */
	public void startTask() {
		cacheNextP2pFile();
	}

	/**
	 * 停止任务
	 */
	public void stopTask() {
		stopP2PCache(getCurrentTaskName());
	}

}
