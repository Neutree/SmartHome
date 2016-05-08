package com.neucrack.tool;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Session {
	static private HashMap<Object, Object> mAttribute = new HashMap<Object, Object>();//记录键值
	static private Map<Object,Long> mTime = new HashMap<Object, Long>();//记录时间戳
	static private ArrayList<Object> mKeys= new ArrayList<Object>();
	static private long mMaxInactiveInterval = 7776000;//设置超时时间 90天(7776000s)
	static private long mAutoClearInterval = 7776000;//自动清除失效的session
	

	public Session() {
		mMaxInactiveInterval = 1800000;//30分钟
		mAutoClearInterval = mMaxInactiveInterval;
	}

	static public Object getAttribute(Object key) {
		if(!mAttribute.containsKey(key))
			return null;
		if(System.currentTimeMillis() - mTime.get(key) > mMaxInactiveInterval)
			return null;
		return mAttribute.get(key);
	}

	static public Object setAttribute(Object key, Object value) {
		mTime.put(key, System.currentTimeMillis());
		mKeys.add(key);
		return mAttribute.put(key, value);
			
	}
	static public void invalidate() {
		mAttribute.clear();
	}

	static public long getMaxInactiveInterval() {
		return mMaxInactiveInterval;
	}

	static public void setMaxInactiveInterval(long maxInactiveInterval) {
		mMaxInactiveInterval = maxInactiveInterval;
	}
	
	static public long getmAutoClearInterval() {
		return mAutoClearInterval;
	}

	static public void setmAutoClearInterval(long mAutoClearInterval) {
		mAutoClearInterval = mAutoClearInterval;
	}

	public void EnableAutoClear() {
		AutoClear autoClear = new AutoClear();
		autoClear.start();
	}
	private class AutoClear extends Thread{

		@Override
		public void run() {
			super.run();
			while(true){
				for(int i=0;i<mAttribute.size();++i){
					if(System.currentTimeMillis() - mTime.get(mKeys.get(i)) > mMaxInactiveInterval)
						mAttribute.remove(mKeys.get(i));
				}
				try {
					Thread.sleep(mAutoClearInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
