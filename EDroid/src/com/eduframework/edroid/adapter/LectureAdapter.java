package com.eduframework.edroid.adapter;

import java.util.List;

import com.eduframework.edroid.dto.LectureDTO;
import com.eduframework.edroid.holder.LectureHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class LectureAdapter extends ArrayAdapter<LectureDTO> {
    private Context context;
    private int layoutResourceId;
    private List<LectureDTO> data;

    public LectureAdapter(Context context, int layoutResourceId, List<LectureDTO> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LectureHolder lectureHolder = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, parent, false);
            lectureHolder = new LectureHolder(view);
            view.setTag(lectureHolder);
        } else {
            lectureHolder = (LectureHolder) view.getTag();
        }

        LectureDTO lecture = data.get(position);
        filllectureHolder(lectureHolder, lecture);

        return view;
    }
    
    public LectureDTO getLectureByPostition (Integer position) {
    	return data.get(position);
    }

    private void filllectureHolder(LectureHolder lectureHolder, LectureDTO lecture) {

        if (lecture == null) {
        } else {
            lectureHolder.getLectureTitle().setText(lecture.getName());
        }

    }
}
