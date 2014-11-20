package com.eduframework.edroid.util;

import android.os.AsyncTask;

public class AsyncWorkTask extends AsyncTask<Object, Object, Object> {

	private OnFinishTask onFinishTask;
	
	@Override
	protected Object doInBackground(Object... params) {
		onFinishTask = (OnFinishTask) params[0];
		return onFinishTask.doInBackground();
	}

	@Override
	protected void onPostExecute(Object result) {
		onFinishTask.onFinish(result);
	}
	
}
