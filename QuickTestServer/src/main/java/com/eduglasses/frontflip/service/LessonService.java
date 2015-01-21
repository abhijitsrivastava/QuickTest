package com.eduglasses.frontflip.service;

import java.util.List;

import com.eduglasses.frontflip.controllers.form.AddLessonForm;
import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.User;

public interface LessonService {

	public Lesson saveLesson(AddLessonForm addLessonForm, String email);

	public List<Lesson> getAllLessons(User user);
	
	public Lesson getLesson(long id);
	
	public Lesson saveLesson(Lesson lesson);
}
