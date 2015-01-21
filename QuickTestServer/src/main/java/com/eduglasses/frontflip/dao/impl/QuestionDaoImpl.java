package com.eduglasses.frontflip.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.eduglasses.frontflip.dao.QuestionDao;
import com.eduglasses.frontflip.domain.Question;
import com.eduglasses.frontflip.util.FrontFlipUtil;

@Repository("questionDao")
public class QuestionDaoImpl extends GenericDaoImpl<Question, Long> implements
		QuestionDao {

	private static final Logger logger = Logger
			.getLogger(QuestionDaoImpl.class);

	public QuestionDaoImpl() {
		super(Question.class);
	}

	public List<Question> getQuestionsForSection(long sectionId) {
		Session session = null;
		List<Question> questions = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from Question where section_id = :section_id and deleted=:deleted";
			Query query = session.createQuery(queryString);
			query.setLong("section_id", sectionId);
			query.setBoolean("deleted", false);
			questions = query.list();
		} catch (Exception e) {
			logger.fatal(FrontFlipUtil.getExceptionDescriptionString(e));
			throw e;
		} finally {
			session.close();
		}
		return questions;

	}
	
	
}
