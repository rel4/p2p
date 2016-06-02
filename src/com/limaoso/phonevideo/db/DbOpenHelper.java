/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.limaoso.phonevideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.limaoso.phonevideo.utils.SDUtils;

public class DbOpenHelper extends SQLiteOpenHelper {
	// private String CREATE_BOOK =
	// "create table play_cache.db(bookId integer primarykey,bookName text);";

	// private String CREATE_TEMP_BOOK = "alter table "
	// + CacheMessgeDao.TABLE_NAME + " rename to _temp_book";
	//
	// private String INSERT_DATA = "insert into " + CacheMessgeDao.TABLE_NAME
	// + " select *,'' from _temp_book";
	//
	// private String DROP_BOOK = "drop table _temp_book";
	private static final int DATABASE_VERSION = 6;
	private static DbOpenHelper instance;
	private String add = "ALTER TABLE " + CacheMessgeDao.TABLE_NAME
			+ " ADD COLUMN " + CacheMessgeDao.COLUMN_TV_PIC_PATH + " TEXT";
	// private static final String USERNAME_TABLE_CREATE = "CREATE TABLE "
	// + UserDao.TABLE_NAME + " ("
	// + UserDao.COLUMN_NAME_NICK + " TEXT, "
	// + UserDao.COLUMN_NAME_AVATAR + " TEXT, "
	// + UserDao.COLUMN_APP_ID + " TEXT, "
	// + UserDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";
	/**
	 * 播放详情
	 */
	private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
			+ CacheMessgeDao.TABLE_NAME + " (" + CacheMessgeDao.COLUMN_TV_HASH
			+ " TEXT PRIMARY KEY, " + CacheMessgeDao.COLUMN_TV_NAME + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_DOWN_FILE_SIZE + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_ID + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_PLAY_END_TIME + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_FILE_SIZE + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_PIC_PATH + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_PLAY_POSITION + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_PLAY_NUM + " TEXT, "
			+ CacheMessgeDao.COLUMN_TV_TIME + " TEXT);";

	private DbOpenHelper(Context context) {
		super(context, getUserDatabaseName(context), null, DATABASE_VERSION);
	}

	public static DbOpenHelper getInstance(Context context) {
		if (instance == null) {
			instance = new DbOpenHelper(context.getApplicationContext());
		}
		return instance;
	}

	private static String getUserDatabaseName(Context context) {
		return SDUtils.getRootDir(context) + ".db";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		switch (newVersion) {

		// case 5:
		//
		// db.execSQL(CREATE_TEMP_BOOK);
		//
		// db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
		//
		// db.execSQL(INSERT_DATA);
		//
		// db.execSQL(DROP_BOOK);
		//
		// break;
		case 6:
			db.execSQL(add);
			break;

		}
	}

	public void closeDB() {
		if (instance != null) {
			try {
				SQLiteDatabase db = instance.getWritableDatabase();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance = null;
		}
	}

}
