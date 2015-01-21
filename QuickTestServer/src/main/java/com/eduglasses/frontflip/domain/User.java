package com.eduglasses.frontflip.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private long userId;

	@Column(name = "email", nullable = false, unique = true, length = 70)
	private String email;

	@Column(name = "password", nullable = false, length = 15)
	private String password;

	@Column(name = "created")
	private Date created;

	@ManyToOne(cascade = CascadeType.ALL)
	private Role role;

	@Column(name = "edugrade_teacherid")
	private long eduGradeTeacherId = 0;

	public long getEduGradeTeacherId() {
		return eduGradeTeacherId;
	}

	public void setEduGradeTeacherId(long eduGradeTeacherId) {
		this.eduGradeTeacherId = eduGradeTeacherId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [user_id =" + userId + ", email =" + email + "]";
	}

}
