package com.limaoso.phonevideo.db.cacheinfo;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.limaoso.phonevideo.db.DbOpenHelper;

public class CacheInfoDao {
	public static final String TABLENAME = "down_cache_info";
	public static final String COLUMN_NAME_DOWN_FILE_ID = "down_file_id";
	public static final String COLUMN_NAME_DOWN_FILE_NAME = "down_file_name";
	public static final String COLUMN_NAME_DOWN_FILE_SIZE = "down_file_size";
	private DbOpenHelper dbHelper;

	public CacheInfoDao(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	synchronized public long SaveCacheInfo(CacheInfo info) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = -1;
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME_DOWN_FILE_ID, info.getFileId());
			values.put(COLUMN_NAME_DOWN_FILE_NAME, info.getFileName());
			values.put(COLUMN_NAME_DOWN_FILE_SIZE, info.getFileSize());
			id = db.insert(TABLENAME, null, values);
		}
		return id;
	}

	/**
	 * 获取全部下载信息
	 * 
	 * @return
	 */
	synchronized public List<CacheInfo> getAllCacheInfo() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<CacheInfo> listInfo = null;
		if (db.isOpen()) {
			listInfo = new ArrayList<CacheInfo>();
			Cursor cursor = db.rawQuery("select * from " + TABLENAME + " ",
					null);
			while (cursor.moveToNext()) {
				CacheInfo info = new CacheInfo();
				info.setFileId(cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_DOWN_FILE_ID)));
				info.setFileName(cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_DOWN_FILE_NAME)));
				info.setFileSize(cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_DOWN_FILE_SIZE)));
				listInfo.add(info);
			}
			cursor.close();
		}
		return listInfo;
	}

	/**
	 * 获取单个下载信息
	 * 
	 * @param fileid
	 * @return
	 */
	synchronized public CacheInfo SelectInfoByAppid(String fileid) {
		CacheInfo info = null;
		if (fileid == null || "".endsWith(fileid.trim())) {
			return info;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from " + TABLENAME
					+ " where " + COLUMN_NAME_DOWN_FILE_ID + " = " + fileid,
					null);
			while (cursor.moveToNext()) {
				info = new CacheInfo();
				info.setFileId(cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_DOWN_FILE_ID)));
				info.setFileName(cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_DOWN_FILE_NAME)));
				info.setFileSize(cursor.getString(cursor
						.getColumnIndex(COLUMN_NAME_DOWN_FILE_SIZE)));
			}
		}
		return info;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileid
	 * @return
	 */
	synchronized public boolean deleteCacheInfo(String fileid) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int delete = 0;
		if (db.isOpen()) {
			delete = db.delete(TABLENAME, COLUMN_NAME_DOWN_FILE_ID,
					new String[] { fileid });
		}
		return delete != 1 ? false : true;
	}

}
