package com.nulp.eduframework.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nulp.eduframework.dao.LectureMessageDao;
import com.nulp.eduframework.domain.LectureChat;
import com.nulp.eduframework.domain.LectureMessage;

@Service
public class LectureMessageServiceImpl implements LectureMessageService {

	@Autowired
	private LectureMessageDao messageDao;
	
	@Transactional
	public void addLectureMessage(LectureMessage message) {
		messageDao.addLectureMessage(message, null);
	}

	@Transactional
	public List<LectureMessage> getAllMessagesByLecture(LectureChat lecture) {
		return messageDao.getAllMessagesByLecture(lecture, null);
	}

	@Transactional
	public void addLectureMessage(LectureMessage message, Session session) {
		messageDao.addLectureMessage(message, session);
	}

	@Transactional
	public List<LectureMessage> getAllMessagesByLecture(LectureChat lecture, Session session) {
		return messageDao.getAllMessagesByLecture(lecture, session);
	}

}
