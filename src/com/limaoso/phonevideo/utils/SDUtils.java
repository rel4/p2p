package com.limaoso.phonevideo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

public class SDUtils {

	/**
	 * 
	 * @return ROM存储路径
	 */
	public static String getInternalMemoryPath() {
		return Environment.getDataDirectory().getPath();
	}

	/**
	 * 
	 * @return 内置sd卡路径
	 */
	public static String getExternalMemoryPath() {

		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 
	 * @return 外置sd卡路径
	 */
	public static String getSDCard2MemoryPath() {
		return "/mnt/sdcard1";
	}

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件路径的StatFs对象
	 * @throws Exception
	 *             路径为空或非法异常抛出
	 */
	public static StatFs getStatFs(String path) {
		try {
			return new StatFs(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param stat
	 *            文件StatFs对象
	 * @return 剩余存储空间的MB数
	 * 
	 */
	public static float calculateSizeInMB(StatFs stat) {
		if (stat != null)
			return stat.getAvailableBlocks()
					* (stat.getBlockSize() / (1024f * 1024f));
		return 0.0f;
	}

	/**
	 * 
	 * @return ROM剩余存储空间的MB数
	 */
	public static float getAvailableInternalMemorySize() {

		String path = getInternalMemoryPath();// 获取数据目录
		StatFs stat = getStatFs(path);
		return calculateSizeInMB(stat);
	}

	/**
	 * 
	 * @return 内置SDCard剩余存储空间MB数
	 */
	public static float getAvailableExternalMemorySize() {

		String path = getExternalMemoryPath();// 获取数据目录
		StatFs stat = getStatFs(path);
		return calculateSizeInMB(stat);

	}

	/**
	 * 
	 * @return 外置SDCard剩余存储空间MB数
	 */
	public static float getAvailableSDCard2MemorySize() {

		// String status = Environment.getExternalStorageState();
		// if (status.equals(Environment.MEDIA_MOUNTED)) {
		// }
		String path = getSDCard2MemoryPath(); // 获取数据目录
		StatFs stat = getStatFs(path);
		return calculateSizeInMB(stat);

	}

	public static List<String> getExtSDCardPath() {
		List<String> lResult = new ArrayList<String>();
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("extSdCard")) {
					String[] arr = line.split(" ");
					String path = arr[1];
					File file = new File(path);
					if (file.isDirectory()) {
						lResult.add(path);
					}
				}
			}
			isr.close();
		} catch (Exception e) {
		}
		return lResult;
	}

	private static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	/**
	 * 得到SD路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		String sdDir = null;
		long size = 1024 * 1024 * 1000;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			String path1 = getExternalMemoryPath().toString();
			long size1 = getAvailableSize(path1);
			List<String> allExterSdcardPath = getAllExterSdcardPath();
			String path2 = null;
			long size2 = 0;
			if (allExterSdcardPath != null && allExterSdcardPath.size() != 0) {
				path2 = allExterSdcardPath.get(0);
				size2 = getAvailableSize(path2);
			}

			if (!TextUtils.isEmpty(path1) && size1 > size) {
				sdDir = path1;
			} else if (!TextUtils.isEmpty(path2) && size2 > size) {
				sdDir = path2;
			} else {
				sdDir = Environment.getExternalStorageDirectory().toString();// 获取跟目录
			}
		} else {
			sdDir = Environment.getDownloadCacheDirectory().toString();
		}
		return sdDir;

	}

	// public String

	/**
	 * 获取手机自身内存路径
	 * 
	 */
	public static String getPhoneCardPath() {
		return Environment.getDataDirectory().getPath();

	}

	/**
	 * 获取sd卡路径 双sd卡时，根据”设置“里面的数据存储位置选择，获得的是内置sd卡或外置sd卡
	 * 
	 * @return
	 */
	public static String getNormalSDCardPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 获取sd卡路径 双sd卡时，获得的是外置sd卡
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		String cmd = "cat /proc/mounts";
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		BufferedInputStream in = null;
		BufferedReader inBr = null;
		try {
			Process p = run.exec(cmd);// 启动另一个进程来执行命令
			in = new BufferedInputStream(p.getInputStream());
			inBr = new BufferedReader(new InputStreamReader(in));

			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				DebugLog.i("CommonUtil:getSDCardPath", lineStr);
				if (lineStr.contains("sdcard")
						&& lineStr.contains(".android_secure")) {
					String[] strArray = lineStr.split(" ");
					if (strArray != null && strArray.length >= 5) {
						String result = strArray[1].replace("/.android_secure",
								"");
						return result;
					}
				}
				// 检查命令是否执行失败。
				if (p.waitFor() != 0 && p.exitValue() == 1) {
					// p.exitValue()==0表示正常结束，1：非正常结束
					DebugLog.e("CommonUtil:getSDCardPath", "命令执行失败!");
				}
			}
		} catch (Exception e) {
			DebugLog.e("CommonUtil:getSDCardPath", e.toString());
			// return Environment.getExternalStorageDirectory().getPath();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (inBr != null) {
					inBr.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Environment.getExternalStorageDirectory().getPath();
	}

	// 查看所有的sd路径
	public static String getSDCardPathEx() {
		String mount = new String();
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;

				if (line.contains("fat")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat("*" + columns[1] + "\n");
					}
				} else if (line.contains("fuse")) {
					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						mount = mount.concat(columns[1] + "\n");
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mount;
	}

	// 获取当前路径，可用空间
	public static long getAvailableSize(String path) {
		try {
			File base = new File(path);
			if (!base.exists()) {
				base.mkdirs();
			}
			StatFs stat = new StatFs(base.getPath());
			long nAvailableCount = stat.getBlockSize()
					* ((long) stat.getAvailableBlocks());
			return nAvailableCount;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 获取当前路径，空间大小
	public static long getTotalAvailableSize(String path) {
		try {
			File base = new File(path);
			if (!base.exists()) {
				base.mkdirs();
			}
			StatFs stat = new StatFs(base.getPath());
			long nSDTotalSize = stat.getBlockSize()
					* ((long) stat.getBlockCount());
			return nSDTotalSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 得到缓存可用空间
	 * 
	 * @return
	 */
	public static long getCacheAvailableSize(Context context) {
		return getAvailableSize(getRootFile(context));
	}

	/**
	 * 获取缓存空间总大小
	 * 
	 * @return
	 */
	public static long getCacheTotalAvailableSize(Context context) {
		return getTotalAvailableSize(getRootFile(context));
	}

	/**
	 * 获取 /data/data/package.name/ 目录
	 * 
	 * @param path
	 *            data目录文件夹名
	 * @return
	 */
	public static String getRootDataPath(Context context, String pathName) {
		String infoRootPath = SDUtils.getInternalMemoryPath() + File.separator
				+ "data" + File.separator + context.getPackageName()
				+ File.separator + pathName + File.separator;
		File file = new File(infoRootPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath().toString();
	}

	/**
	 * p2p下载文件目录
	 * 
	 * @return
	 */
	// p2p/download
	public static String getDownFileDir(Context context) {
		String path = getRootFile(context) + File.separator + "p2p"
				+ File.separator + "download" + File.separator;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getAbsolutePath().toString();
	}

	/**
	 * 获取跟路径
	 * 
	 * @return
	 */

	public static String getRootFile(Context context) {
		String rootPath = getSharedPreferences(context).getString(
				context.getPackageName(), null);
		if (rootPath != null) {
			File file = new File(rootPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (!file.isDirectory()) {
				file.delete();
				file.mkdirs();
			}
			return rootPath;
		}
		String sdPath = getSDPath() + File.separator + getRootDir(context)
				+ File.separator;
		if (sdPath != null) {
			File file = new File(sdPath);
			if (!file.isDirectory()) {
				file.delete();
			}
			if (!file.exists() || !file.isDirectory()) {
				file.mkdirs();
			}
			getSharedPreferences(context)
					.edit()
					.putString(context.getPackageName(),
							file.getAbsolutePath().toString()).commit();

			return file.getAbsolutePath().toString();
		}
		return null;
	}

	public static String getRootDir(Context context) {
		String packageName = context.getPackageName();
		if (packageName.contains(".")) {
			return packageName.substring(packageName.lastIndexOf(".") + 1);
		}
		if (TextUtils.isEmpty(packageName)) {
			return "Android";
		}
		return packageName;
	}

	private static SharedPreferences getSharedPreferences(Context context) {

		return context.getSharedPreferences(context.getPackageName(),
				Context.MODE_PRIVATE);
	}

	public static List<String> getAllExterSdcardPath() {
		List<String> SdList = new ArrayList<String>();

		String firstPath = getFirstExterPath();

		// 得到路径
		try {
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("mount");
			InputStream is = proc.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			String line;
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null) {
				// 将常见的linux分区过滤掉
				if (line.contains("secure"))
					continue;
				if (line.contains("asec"))
					continue;
				if (line.contains("media"))
					continue;
				if (line.contains("system") || line.contains("cache")
						|| line.contains("sys") || line.contains("data")
						|| line.contains("tmpfs") || line.contains("shell")
						|| line.contains("root") || line.contains("acct")
						|| line.contains("proc") || line.contains("misc")
						|| line.contains("obb")) {
					continue;
				}

				if (line.contains("fat") || line.contains("fuse")
						|| (line.contains("ntfs"))) {

					String columns[] = line.split(" ");
					if (columns != null && columns.length > 1) {
						String path = columns[1];
						if (path != null && !SdList.contains(path)
								&& path.contains("sd"))
							SdList.add(columns[1]);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if (!SdList.contains(firstPath)) {
		// SdList.add(firstPath);
		// }

		return SdList;
	}

	private static String getFirstExterPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

}
