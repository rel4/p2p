package com.limaoso.phonevideo.db;

import java.io.Serializable;

public class CacheInfo  implements Serializable{
	private String tvHash;// 播放hash
	private String tvName;// TV名称
	private String tvId;// TVid
	private long tvTime;// TV时间
	private long tvFileSize;// TV的文件大小
	private long tvDownFileSize;// TV已下载文件大小
	private long tvPlayPosition;// TV已播放时长
	private long tvPlayEndTime;// TV总时长
	private String tvPlaynum;// TV已播放剧集
	private String tvPicPath;// TV图片路径

	public String getTvPicPath() {
		return tvPicPath;
	}

	public void setTvPicPath(String tvPicPath) {
		this.tvPicPath = tvPicPath;
	}

	public long getTvPlayEndTime() {
		return tvPlayEndTime;
	}

	public void setTvPlayEndTime(long tvPlayEndTime) {
		this.tvPlayEndTime = tvPlayEndTime;
	}

	public String getTvPlaynum() {
		return tvPlaynum;
	}

	public void setTvPlaynum(String tvPlaynum) {
		this.tvPlaynum = tvPlaynum;
	}

	public String getTvId() {
		return tvId;
	}

	public void setTvId(String tvId) {
		this.tvId = tvId;
	}

	public String getTvHash() {
		return tvHash;
	}

	public void setTvHash(String tvHash) {
		this.tvHash = tvHash;
	}

	public String getTvName() {
		return tvName;
	}

	public void setTvName(String tvName) {
		this.tvName = tvName;
	}

	public long getTvTime() {
		return tvTime;
	}

	public void setTvTime(long tvTime) {
		this.tvTime = tvTime;
	}

	public long getTvFileSize() {
		return tvFileSize;
	}

	public void setTvFileSize(long tvFileSize) {
		this.tvFileSize = tvFileSize;
	}

	public long getTvDownFileSize() {
		return tvDownFileSize;
	}

	public void setTvDownFileSize(long tvDownFileSize) {
		this.tvDownFileSize = tvDownFileSize;
	}

	public long getTvPlayPosition() {
		return tvPlayPosition;
	}

	public void setTvPlayPosition(long tvPlayPosition) {
		this.tvPlayPosition = tvPlayPosition;
	}

}
