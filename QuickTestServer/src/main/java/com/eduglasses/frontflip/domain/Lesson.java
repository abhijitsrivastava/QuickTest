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
@Table(name = "lesson")
public class Lesson implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "lesson_name", nullable = false, length = 70)
	private String lessonName;

	@Column(name = "lesson_description", nullable = true, length = 300)
	private String lessonDescription;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "status", nullable = false)
	private LessonStatusEnum status = LessonStatusEnum.INACTIVE;

	@Column(name = "created")
	private Date created;

	@Column(name = "updated")
	private Date updated;

	@Column(name = "deleted", nullable = false, columnDefinition = "tinyint default false")
	private Boolean deleted = false;

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getLessonDescription() {
		return lessonDescription;
	}

	public void setLessonDescription(String lessonDescription) {
		this.lessonDescription = lessonDescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LessonStatusEnum getStatus() {
		return status;
	}

	public void setStatus(LessonStatusEnum status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "Lesson [id=" + id + ", lessonName=" + lessonName
				+ ", lessonDescription=" + lessonDescription + ", user=" + user
				+ ", status=" + status + ", created=" + created + ", updated="
				+ updated + "]";
	}


}
