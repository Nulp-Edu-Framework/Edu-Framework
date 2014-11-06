package com.nulp.eduframework.service;

import java.util.List;

import org.hibernate.Session;

import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.LectureMessage;

public interface LectureMessageService {
	public void addLectureMessage (LectureMessage message);
    public List<LectureMessage> getAllMessagesByLecture (LectureChat lecture);
	public void addLectureMessage (LectureMessage message, Session session);
    public List<LectureMessage> getAllMessagesByLecture (LectureChat lecture, Session session);
}
