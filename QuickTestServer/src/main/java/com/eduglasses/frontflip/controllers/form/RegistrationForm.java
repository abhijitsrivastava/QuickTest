package com.eduglasses.frontflip.controllers.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class RegistrationForm {

	@NotEmpty(message="Email must not be empty") 
	@Email(message="Email format is incorrect")
	private String email;
	
	private String role;
	
	@NotEmpty(message = "Password must not be empty")
	private String password;
	
	@NotEmpty(message = "Confirm Password must not be empty")	
	private String confirmPassword;
	
		
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
