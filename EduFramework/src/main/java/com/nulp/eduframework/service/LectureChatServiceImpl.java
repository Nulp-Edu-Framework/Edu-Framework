package com.nulp.eduframework.service;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nulp.eduframework.controller.dto.LectureDTO;
import com.nulp.eduframework.dao.LectureChatDao;
import com.nulp.eduframework.domain.LectureChat;

@Service
public class LectureChatServiceImpl implements LectureChatService {

	@Autowired
	private LectureChatDao lectureChatDao;
	 
	@Transactional
	public List<LectureChat> lectureChatsList() {
		return lectureChatDao.lectureChatsList(null);
	}

	@Transactional
	public LectureChat getLectureChatById(Integer id) {
		return lectureChatDao.getLectureChatById(id, null);
	}

	@Transactional
	public void addLectureChat(LectureChat lectureChat) {
		lectureChatDao.addLectureChat(lectureChat, null);
	}

	@Transactional
	public List<LectureChat> lectureChatsList(Session session) {
		return lectureChatDao.lectureChatsList(session);
	}

	@Transactional
	public LectureChat getLectureChatById(Integer id, Session session) {
		return lectureChatDao.getLectureChatById(id, session);
	}

	@Transactional
	public void addLectureChat(LectureChat lectureChat, Session session) {
		lectureChatDao.addLectureChat(lectureChat, session);
	}

	@Transactional
	public List<LectureDTO> getLectureDTOList() {
		return lectureChatDao.getLectureDTOList(null);
	}

	@Transactional
	public List<LectureDTO> getLectureDTOList(Session session) {
		return lectureChatDao.getLectureDTOList(session);
	}

	@Transactional
	public void deleteLecture(LectureChat lecture, Session session) {
		lectureChatDao.deleteLecture(lecture, session);
	}

	@Transactional
	public void deleteLecture(LectureChat lecture) {
		lectureChatDao.deleteLecture(lecture, null);
	}

}
