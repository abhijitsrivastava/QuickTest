package com.eduglasses.frontflip.dto;

import java.util.ArrayList;
import java.util.List;

public class LessonDTO {

	private List<SectionDTO> sections = new ArrayList<>();

	public List<SectionDTO> getSections() {
		return sections;
	}

	public void setSections(List<SectionDTO> sections) {
		this.sections = sections;
	}
	
	
}
