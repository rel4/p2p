package com.limaoso.phonevideo.download;

import java.util.ArrayList;

/**
 * 后台服务控制接口
 */

public class DownServerControl {

	/**
	 * 开启服务
	 * path[0]: save文件路径  包名：package.name
	 * path[1]: download文件路径根目录  /sdcard/limao/p2p/download
	 * @return 服务器端口号
	 */
	public native int startServer(String[] path);

	/**
	 * 停止服务
	 */
	public native void stopServer();
	
	/**
	 * 文件路径
	 */
	public native void notifyFilePath(String path);
	
	/**
	 * 查询单个文件信息
	 */
	public native DownloadFileInfo getFilesInfo(String uri);
	
	/**
	 * 查询所有
	 */
	public native ArrayList<DownloadFileInfo> getAllFilesInfo();
	
	/**
	 * p2p缓存文件
	 */
	public native void cacheP2PFile(String uri);
	
	/**
	 * dht缓存文件
	 */
	public native void cacheDHTFile(String uri);
	
	/**
	 * 暂停p2p缓存
	 */
	public native void stopP2PCache(String uri);
	
	/**
	 * 暂停DHT缓存
	 */
	public native void stopDHTCache(String uri);
	
	/**
	 * 删除文件
	 */
	public native void deleteFile(String uri);
	
	/**
	 * wiff状态
	 * @param curentobj 0:表示关闭，1:表示开启
	 */
	public native void notifyWifi(Object curentobj);
	
	/**
	 * 状态通知
	 */
	public native void setResponseStatus(IP2PListener lister);
}
