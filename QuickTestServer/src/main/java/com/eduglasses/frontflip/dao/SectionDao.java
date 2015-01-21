package com.eduglasses.frontflip.dao;

import java.util.List;

import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SlideNotes;

public interface SectionDao extends GenericDao<Section, Long> {

	public List<Section> getAllSectionsForLesson(Lesson lesson);

	public void saveSlideNotes(SlideNotes slideNotes);
	
	public List<SlideNotes> getSlideNotesForSection(long section_id);

}
