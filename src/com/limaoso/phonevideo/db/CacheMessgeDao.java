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

import java.util.List;

import android.content.ContentValues;
import android.content.Context;

public class CacheMessgeDao {
	public static final String TABLE_NAME = "new_play_info";
	public static final String COLUMN_TV_HASH = "tv_hash";
	public static final String COLUMN_TV_NAME = "tv_name";
	public static final String COLUMN_TV_TIME = "tv_time";
	public static final String COLUMN_TV_ID = "tv_ID";
	public static final String COLUMN_TV_FILE_SIZE = "tv_file_size";
	public static final String COLUMN_TV_DOWN_FILE_SIZE = "tv_down_file_size";
	public static final String COLUMN_TV_PLAY_POSITION = "tv_play_position";
	public static final String COLUMN_TV_PLAY_END_TIME = "tv_play_end_time";
	public static final String COLUMN_TV_PLAY_NUM = "tv_play_num";
	public static final String COLUMN_TV_PIC_PATH = "tv_pic_path";

	public CacheMessgeDao(Context context) {
		MyDBManager.getInstance().onInit(context);
	}

	/**
	 * 保存message
	 * 
	 * @param message
	 * @return 返回这条messaged在db中的id
	 */
	public Integer saveMessage(CacheInfo cacheInfo) {
		return MyDBManager.getInstance().saveCacheInfo(cacheInfo);
	}

	/**
	 * 更新message
	 * 
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(String msgId, ContentValues values) {
		MyDBManager.getInstance().updateMessage(msgId, values);
	}

	/**
	 * 删除
	 * 
	 * @param hash
	 */
	public void deleteMessage(String hash) {
		MyDBManager.getInstance().deleteMessage(hash);
	}

	/**
	 * 获取单条信息
	 * 
	 * @param hash
	 * @return
	 */
	public CacheInfo getCacheInfo(String hash) {
		return MyDBManager.getInstance().getCacheInfo(hash);
	}

	/**
	 * 获取全部信息
	 * 
	 * @param hash
	 * @return
	 */
	public List<CacheInfo> getCacheInfoList() {
		return MyDBManager.getInstance().geCacheInfoList();
	}
}
