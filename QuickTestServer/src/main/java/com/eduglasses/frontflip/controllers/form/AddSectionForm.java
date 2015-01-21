package com.eduglasses.frontflip.controllers.form;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.eduglasses.frontflip.controllers.validator.FileTypeMatch;

public class AddSectionForm {

	@NotEmpty(message = "Section Name must not be empty")
	private String sectionName;

	private long lessonId;

	private int sectionType = -1;

	private String sectionDescription;
	
	private String teacherId;

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public int getSectionType() {
		return sectionType;
	}

	public void setSectionType(int sectionType) {
		this.sectionType = sectionType;
	}

	@FileTypeMatch
	private CommonsMultipartFile sectionDetails;

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public long getLessonId() {
		return lessonId;
	}

	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}

	public String getSectionDescription() {
		return sectionDescription;
	}

	public void setSectionDescription(String sectionDescription) {
		this.sectionDescription = sectionDescription;
	}

	public CommonsMultipartFile getSectionDetails() {
		return sectionDetails;
	}

	public void setSectionDetails(CommonsMultipartFile sectionDetails) {
		this.sectionDetails = sectionDetails;
	}

}
