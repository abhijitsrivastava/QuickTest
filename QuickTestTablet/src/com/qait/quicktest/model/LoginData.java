package com.qait.quicktest.model;

public class LoginData {

	private String lessonId;
	private String studentId;

	private static LoginData singleton;

	/*
	 * A private Constructor prevents any other class from instantiating.
	 */
	private LoginData() {
	}

	/* Static 'instance' method */
	public static LoginData getInstance() {
		if (null == singleton) {
			singleton = new LoginData();
		}
		return singleton;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}

	@Override
	public String toString() {
		return "\nLoginData :\nstudentId: " + studentId + "\nLessonId : "
				+ lessonId;
	}
}
