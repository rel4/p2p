package com.limaoso.phonevideo.p2p;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Looper;

import com.limaoso.phonevideo.utils.DebugLog;

public class TimerThreadManager extends BaseThreadExecutor {
	private Timer mTimer;
	private List<Runnable> tasks;
	private String TAG = "TimerThread";
	private boolean isRunnable;
	// private List<Runnable> removeTasks;

	private List<Runnable> copyTasks;

	private TimerThreadManager() {
	}

	private static TimerThreadManager instance = new TimerThreadManager();
	private LMTimerTask lmTimerTask;

	public static TimerThreadManager getInstance() {

		return instance;
	}

	private void startTimer() {
		if (!isRunnable) {
			DebugLog.e(this, "Timer运行在："
					+ (Thread.currentThread() == Looper.getMainLooper()
							.getThread() ? "主线程" : "子线程"));
			isRunnable = true;
			mTimer = new Timer();
			lmTimerTask = new LMTimerTask();
			mTimer.schedule(lmTimerTask, 0, 1000);
			DebugLog.e(TAG, "startTimer");
		}

	}

	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			DebugLog.e(TAG, "stopTimer");
		}

		if (lmTimerTask != null) {
			lmTimerTask.cancel();
			lmTimerTask = null;
		}
		isRunnable = false;
	}

	/**
	 * 控制人物
	 * 
	 * @param runnable
	 * @param state
	 *            0 移除任务 1 添加任务
	 */
	private synchronized void controlTask(Runnable runnable, int state) {
		if (tasks == null) {
			tasks = new ArrayList<Runnable>();
		}
		if (state == 1) {
			if (!tasks.contains(runnable)) {
				tasks.add(runnable);
				DebugLog.i(TAG, "添加任务成功");

			}
		} else if (state == 0) {
			if (tasks != null && tasks.contains(runnable)) {
				// if (removeTasks == null) {
				// removeTasks = new ArrayList<Runnable>();
				// }
				// removeTasks.add(runnable);
				tasks.remove(runnable);
			}
		}
		if (!isRunnable) {
			addTask(1);

		}
	}

	/**
	 * 添加任务
	 * 
	 * @param runnable
	 */
	public synchronized void addTask(Runnable runnable) {
		controlTask(runnable, 1);

	}

	/**
	 * 删除任务
	 * 
	 * @param runnable
	 */
	public synchronized void removeTask(Runnable runnable) {
		controlTask(runnable, 0);
	}

	private class LMTimerTask extends TimerTask {

		@Override
		public void run() {
			if (tasks == null || tasks.size() == 0) {
				DebugLog.e(TAG, "run()    stopTimer()");
				stopTimer();
				return;
			}
			DebugLog.e(TAG, "TimerTask 轮询  共 " + tasks.size() + "个任务");
			if (copyTasks == null) {
				copyTasks = new ArrayList<Runnable>();
			}
			copyTasks.clear();
			copyTasks.addAll(tasks);
			for (Runnable r : copyTasks) {
				DebugLog.e(TAG, "任务名字： " + r.getClass().getName());
				r.run();
			}

		}
	}

	@Override
	protected void disposeTask(int taskType, Object curentobj) {
		if (taskType == 1) {
			startTimer();
		}

	}

}
