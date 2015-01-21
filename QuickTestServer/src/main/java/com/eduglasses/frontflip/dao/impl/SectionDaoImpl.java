package com.eduglasses.frontflip.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.eduglasses.frontflip.dao.SectionDao;
import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SlideNotes;
import com.eduglasses.frontflip.util.FrontFlipUtil;

@Repository("sectionDao")
public class SectionDaoImpl extends GenericDaoImpl<Section, Long> implements
		SectionDao {

	private static final Logger logger = Logger.getLogger(SectionDaoImpl.class);

	public SectionDaoImpl() {
		super(Section.class);
	}

	@Override
	public List<Section> getAllSectionsForLesson(Lesson lesson) {
		Session session = null;
		List<Section> sections = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from Section where lesson_id = :lesson_id and deleted=:deleted";
			Query query = session.createQuery(queryString);
			query.setLong("lesson_id", lesson.getId());
			query.setBoolean("deleted", false);
			sections = query.list();
		} catch (Exception e) {
			logger.fatal(FrontFlipUtil.getExceptionDescriptionString(e));
			throw e;
		} finally {
			session.close();
		}
		return sections;
	}

	@Override
	public void saveSlideNotes(SlideNotes slideNotes) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(slideNotes);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw e;
		} finally {
			session.flush();
			session.close();
		}
	}

	public List<SlideNotes> getSlideNotesForSection(long section_id) {
		Session session = null;
		List<SlideNotes> slideNotes = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from SlideNotes where section_id = :section_id";
			Query query = session.createQuery(queryString);
			query.setLong("section_id", section_id);
			slideNotes = query.list();
		} catch (Exception e) {
			logger.fatal(FrontFlipUtil.getExceptionDescriptionString(e));
			throw e;
		} finally {
			session.close();
		}
		return slideNotes;
	}

}
