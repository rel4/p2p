package com.limaoso.phonevideo.download;

/**
 * 下载文件的相关信息
 */

public class DownloadFileInfo {
	public String filePath;         // 存储路径
	public String fileSHA1;         // 资源SHA1 :单个查询时不返回
	public String fileMD4;          // 资源MD4  :单个查询时不返回
	public long fileSize = 0;       // 文件大小，单位：字节
	public long dlSize = 0;         // 已下载大小，单位：字节 
	public long dlSpeed = 0;        // 下载速度，单位：字节/秒 ： 查询所有时全部为0
}
