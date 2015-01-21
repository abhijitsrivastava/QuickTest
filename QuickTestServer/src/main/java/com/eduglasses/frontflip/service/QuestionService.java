package com.eduglasses.frontflip.service;

import java.util.List;

import com.eduglasses.frontflip.controllers.form.AddQuestionForm;
import com.eduglasses.frontflip.domain.Question;

public interface QuestionService {

	public List<Question> getQuestionsForSection(long sectionId);

	public void saveQuestion(AddQuestionForm addQuestionForm);
}
