package com.eduglasses.frontflip.dao;

import java.util.List;

import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.User;

public interface LessonDao extends GenericDao<Lesson, Long> {

	public void saveLesson(Lesson lesson);

	public List<Lesson> getAllLessons(User user);

}
