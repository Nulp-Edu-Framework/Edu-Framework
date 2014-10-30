package com.nulp.eduframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nulp.eduframework.dao.LectureChatDao;
import com.nulp.eduframework.domain.LectureChat;

@Service
public class LectureChatServiceImpl implements LectureChatService {

	@Autowired
	private LectureChatDao lectureChatDao;
	 
	@Transactional
	public List<LectureChat> lectureChatsList() {
		return lectureChatDao.lectureChatsList();
	}

	@Transactional
	public LectureChat getLectureChatById(Integer id) {
		return lectureChatDao.getLectureChatById(id);
	}

	@Override
	public void addLectureChat(LectureChat lectureChat) {
		lectureChatDao.addLectureChat(lectureChat);
	}

}
