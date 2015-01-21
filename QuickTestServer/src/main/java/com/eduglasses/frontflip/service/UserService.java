package com.eduglasses.frontflip.service;

import com.eduglasses.frontflip.controllers.form.RegistrationForm;
import com.eduglasses.frontflip.domain.User;

public interface UserService {

	public User getUserByEmail(String email);
	
	public void saveUser(RegistrationForm registrationForm);
}
