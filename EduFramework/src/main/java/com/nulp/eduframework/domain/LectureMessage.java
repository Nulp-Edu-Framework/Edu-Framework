package com.nulp.eduframework.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.nulp.eduframework.model.Message;

@Entity
@Table(name = "lecture_message")
public class LectureMessage {

	@Id
	@GeneratedValue
	@Column(name = "message_id")
	private Integer id;
	
	@Column(name = "message")
	private String text;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecture_id", nullable = false)
	private LectureChat lectureChat;
		
	public LectureMessage() {}

	public LectureMessage(String message) {
		this.text = message;
	}

	public String getMessage() {
		return text;
	}

	public void setMessage(String message) {
		this.text = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public LectureChat getLecture() {
		return lectureChat;
	}

	public void setLecture(LectureChat lecture) {
		this.lectureChat = lecture;
	}
	
	public Message toMessage (){
		return new Message(user.getUsername(), text);
	}

}
