package com.eduglasses.frontflip.service;

import java.util.List;

import com.eduglasses.frontflip.controllers.form.AddSectionForm;
import com.eduglasses.frontflip.domain.Lesson;
import com.eduglasses.frontflip.domain.Section;
import com.eduglasses.frontflip.domain.SlideNotes;

public interface SectionService {

	public List<Section> getAllSectionsForLesson(Lesson lesson);

	public void saveSection(AddSectionForm addSectionForm);

	public Section saveSection(Section section);
	
	public Section getSection(Long id);
	
	public List<SlideNotes> getSlideNotesForSection(long section_id);

}
