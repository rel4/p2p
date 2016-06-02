package com.limaoso.phonevideo.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDBManager {
	static private MyDBManager dbMgr = new MyDBManager();
	private DbOpenHelper dbHelper;

	void onInit(Context context) {
		dbHelper = DbOpenHelper.getInstance(context);
	}

	public static synchronized MyDBManager getInstance() {
		return dbMgr;
	}

	/**
	 * 保存message
	 * 
	 * @param message
	 * @return 返回这条messaged在db中的id
	 */
	public synchronized Integer saveCacheInfo(CacheInfo message) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int id = -1;
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put(CacheMessgeDao.COLUMN_TV_DOWN_FILE_SIZE,message.getTvDownFileSize());
			values.put(CacheMessgeDao.COLUMN_TV_HASH,message.getTvHash());
			values.put(CacheMessgeDao.COLUMN_TV_PIC_PATH,message.getTvPicPath());
			values.put(CacheMessgeDao.COLUMN_TV_FILE_SIZE,message.getTvFileSize());
			values.put(CacheMessgeDao.COLUMN_TV_NAME, message.getTvName());
			values.put(CacheMessgeDao.COLUMN_TV_ID, message.getTvId());
			values.put(CacheMessgeDao.COLUMN_TV_TIME, message.getTvTime());
			values.put(CacheMessgeDao.COLUMN_TV_PLAY_NUM, message.getTvPlaynum());
			values.put(CacheMessgeDao.COLUMN_TV_PLAY_END_TIME, message.getTvPlayEndTime());
			values.put(CacheMessgeDao.COLUMN_TV_PLAY_POSITION,message.getTvPlayPosition());
			id = (int) db.replace(CacheMessgeDao.TABLE_NAME, null, values);
		}
		return id;
	}

	/**
	 * 更新message
	 * 
	 * @param msgId
	 * @param values
	 */
	synchronized public void updateMessage(String msgId, ContentValues values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			int update = db.update(CacheMessgeDao.TABLE_NAME, values,
					CacheMessgeDao.COLUMN_TV_HASH + "=?",
					new String[] { String.valueOf(msgId) });
		}
	}

	synchronized public void deleteMessage(String hash) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(CacheMessgeDao.TABLE_NAME, CacheMessgeDao.COLUMN_TV_HASH
					+ " = ?", new String[] { hash });
		}
	}

	/**
	 * 获取全部播放信息
	 * 
	 * @return
	 */
	synchronized public List<CacheInfo> geCacheInfoList() {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<CacheInfo> mInfos = new ArrayList<CacheInfo>();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select * from "
					+ CacheMessgeDao.TABLE_NAME + " desc", null);
			while (cursor.moveToNext()) {
				CacheInfo mInfo = new CacheInfo();
				long downFileSize = cursor
						.getLong(cursor
								.getColumnIndex(CacheMessgeDao.COLUMN_TV_DOWN_FILE_SIZE));
				long tvFileSize = cursor.getLong(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_FILE_SIZE));
				String tvHash = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_HASH));
				String tvName = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_NAME));
				String tvId = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_ID));
				String tvNum = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PLAY_NUM));
				String tvPicPath = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PIC_PATH));
				long tvTime = cursor.getLong(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_TIME));
				long tvEndTime = cursor.getLong(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PLAY_END_TIME));
				long tvPlayPosition = cursor.getInt(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PLAY_POSITION));
				mInfo.setTvDownFileSize(downFileSize);
				mInfo.setTvFileSize(tvFileSize);
				mInfo.setTvHash(tvHash);
				mInfo.setTvId(tvId);
				mInfo.setTvName(tvName);
				mInfo.setTvPlaynum(tvNum);
				mInfo.setTvTime(tvTime);
				mInfo.setTvPicPath(tvPicPath);
				mInfo.setTvPlayEndTime(tvEndTime);
				mInfo.setTvPlayPosition(tvPlayPosition);
				mInfos.add(mInfo);
			}
			cursor.close();
		}
		return mInfos;
	}

	/**
	 * 获取播放信息
	 * 
	 * @return
	 */
	synchronized public CacheInfo getCacheInfo(String hash) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		CacheInfo mInfo = null;
		if (db.isOpen()) {
			// Cursor cursor =
			// db.rawQuery("select * from new_play_info where tv_hash=?",
			// new String[] { hash });
			Cursor cursor = db.rawQuery("select * from "
					+ CacheMessgeDao.TABLE_NAME + " where "
					+ CacheMessgeDao.COLUMN_TV_HASH + "=?",
					new String[] { hash });
			while (cursor.moveToNext()) {
				mInfo = new CacheInfo();
				long downFileSize = cursor
						.getLong(cursor
								.getColumnIndex(CacheMessgeDao.COLUMN_TV_DOWN_FILE_SIZE));
				long tvFileSize = cursor.getLong(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_FILE_SIZE));
				String tvHash = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_HASH));
				String tvName = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_NAME));
				String tvId = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_ID));
				String tvNum = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PLAY_NUM));
				String tvPicPath = cursor.getString(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PIC_PATH));
				long tvTime = cursor.getLong(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_TIME));
				long tvEndTime = cursor.getLong(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PLAY_END_TIME));
				long tvPlayPosition = cursor.getInt(cursor
						.getColumnIndex(CacheMessgeDao.COLUMN_TV_PLAY_POSITION));
				mInfo.setTvDownFileSize(downFileSize);
				mInfo.setTvFileSize(tvFileSize);
				mInfo.setTvHash(tvHash);
				mInfo.setTvId(tvId);
				mInfo.setTvPlaynum(tvNum);
				mInfo.setTvName(tvName);
				mInfo.setTvTime(tvTime);
				mInfo.setTvPicPath(tvPicPath);
				mInfo.setTvPlayEndTime(tvEndTime);
				mInfo.setTvPlayPosition(tvPlayPosition);
			}
			cursor.close();
		}
		return mInfo;
	}

	synchronized public void closeDB() {
		if (dbHelper != null) {
			dbHelper.closeDB();
		}
	}

}