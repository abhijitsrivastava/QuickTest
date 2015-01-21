package com.eduglasses.frontflip.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "section")
public class Section implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "section_name", nullable = true, length = 70)
	private String sectionName;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "lesson_id")
	private Lesson lesson;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "section_type", nullable = false)
	private SectionTypeEnum sectionType = null;

	@Column(name = "section_description", nullable = true, length = 300)
	private String sectionDescription;

	@Column(name = "section_details", nullable = true, length = 300)
	private String sectionDetails;

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	@Column(name = "number_of_slides")
	private int numberOfSlides = 0;

	@Column(name = "deleted", nullable = false, columnDefinition = "tinyint default false")
	private Boolean deleted = false;

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public int getNumberOfSlides() {
		return numberOfSlides;
	}

	public void setNumberOfSlides(int numberOfSlides) {
		this.numberOfSlides = numberOfSlides;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
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

	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
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

	@Override
	public String toString() {
		return "Section [id=" + id + ", sectionName=" + sectionName
				+ ", lesson=" + lesson + ", sectionType=" + sectionType
				+ ", sectionDescription=" + sectionDescription
				+ ", sectionDetails=" + sectionDetails + "]";
	}

}
