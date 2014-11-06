package com.nulp.eduframework.dao;

import java.util.List;

import org.hibernate.Session;

import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.LectureMessage;

public interface LectureMessageDao {
	public void addLectureMessage (LectureMessage message, Session session);
    public List<LectureMessage> getAllMessagesByLecture (LectureChat lecture, Session session);
}
