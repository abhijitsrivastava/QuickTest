package com.eduglasses.frontflip.controllers.form;

import org.hibernate.validator.constraints.NotEmpty;

public class AddQuestionForm {

	@NotEmpty(message = "Question Description must not be empty")
	private String description;

	private long sectionId;

	@NotEmpty(message = "Please provide option A")
	private String optionA;

	@NotEmpty(message = "Please provide option Bs")
	private String optionB;

	private String optionC;

	private String optionD;

	private String optionE;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getSectionId() {
		return sectionId;
	}

	public void setSectionId(long sectionId) {
		this.sectionId = sectionId;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public String getOptionD() {
		return optionD;
	}

	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}

	public String getOptionE() {
		return optionE;
	}

	public void setOptionE(String optionE) {
		this.optionE = optionE;
	}

}
