package com.eduframework.edroid.holder;

import com.eduframework.edroid.R;

import android.view.View;
import android.widget.TextView;

public class LectureHolder {
	
	public final String LECTURE_HOLDER_TAG = this.getClass().getSimpleName();
	
	private TextView lectureTitle;
	
    public LectureHolder() {}

    public LectureHolder(View view) {
        lectureTitle = (TextView) view.findViewById(R.id.lecture_name);
    }
    
    public LectureHolder(TextView lectureTitle) {
        this.lectureTitle = lectureTitle;
    }

	public TextView getLectureTitle() {
		return lectureTitle;
	}

	public void setLectureTitle(TextView lectureTitle) {
		this.lectureTitle = lectureTitle;
	}

}
