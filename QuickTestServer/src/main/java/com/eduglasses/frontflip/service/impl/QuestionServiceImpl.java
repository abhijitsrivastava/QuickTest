package com.eduglasses.frontflip.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduglasses.frontflip.controllers.form.AddQuestionForm;
import com.eduglasses.frontflip.dao.QuestionDao;
import com.eduglasses.frontflip.dao.SectionDao;
import com.eduglasses.frontflip.domain.Question;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.service.QuestionService;
import com.eduglasses.frontflip.util.ImageWrite;

@Service("questionService")
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private SectionDao sectionDao;

	@Autowired
	private QuestionDao questionDao;

	public SectionDao getSectionDao() {
		return sectionDao;
	}

	public void setSectionDao(SectionDao sectionDao) {
		this.sectionDao = sectionDao;
	}

	public List<Question> getQuestionsForSection(long sectionId) {
		return questionDao.getQuestionsForSection(sectionId);
	}

	public void saveQuestion(AddQuestionForm addQuestionForm) {
		Question question = new Question();
		question.setCreated(new Date());
		question.setUpdated(new Date());
		question.setQuestionDescription(addQuestionForm.getDescription());
		question.setOptionA(addQuestionForm.getOptionA());
		question.setOptionB(addQuestionForm.getOptionB());
		question.setOptionC(addQuestionForm.getOptionC());
		question.setOptionD(addQuestionForm.getOptionD());
		question.setOptionE(addQuestionForm.getOptionE());
		Section section = sectionDao.get(addQuestionForm.getSectionId());
		question.setSection(section);
		List<Question> questions = getQuestionsForSection(addQuestionForm
				.getSectionId());
		ImageWrite.imageWrite(section, question, questions.size() + 1);
		questionDao.save(question);
		section.setNumberOfSlides(questions.size() + 1);
		sectionDao.save(section);
	}
}
