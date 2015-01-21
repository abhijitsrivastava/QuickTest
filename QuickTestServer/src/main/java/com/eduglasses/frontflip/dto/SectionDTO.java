package com.eduglasses.frontflip.dto;

import java.util.List;

import com.eduglasses.frontflip.domain.SectionTypeEnum;

public class SectionDTO {

	private long id;

	private String sectionName;

	private SectionTypeEnum sectionType = null;

	private String sectionDescription;

	private String sectionDetails;
	
	private long numberOfSlides;
	
	private List<String> slideNotes;
	

	public List<String> getSlideNotes() {
		return slideNotes;
	}

	public void setSlideNotes(List<String> slideNotes) {
		this.slideNotes = slideNotes;
	}

	public long getNumberOfSlides() {
		return numberOfSlides;
	}

	public void setNumberOfSlides(long numberOfSlides) {
		this.numberOfSlides = numberOfSlides;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public SectionTypeEnum getSectionType() {
		return sectionType;
	}

	public void setSectionType(SectionTypeEnum sectionType) {
		this.sectionType = sectionType;
	}

	public String getSectionDescription() {
		return sectionDescription;
	}

	public void setSectionDescription(String sectionDescription) {
		this.sectionDescription = sectionDescription;
	}

	public String getSectionDetails() {
		return sectionDetails;
	}

	public void setSectionDetails(String sectionDetails) {
		this.sectionDetails = sectionDetails;
	}

}
