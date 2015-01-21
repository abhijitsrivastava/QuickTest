package com.eduglasses.frontflip.dao;

import java.util.List;

import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.Question;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SlideNotes;

public interface QuestionDao extends GenericDao<Question, Long> {

	public List<Question> getQuestionsForSection(long sectionId);
}
