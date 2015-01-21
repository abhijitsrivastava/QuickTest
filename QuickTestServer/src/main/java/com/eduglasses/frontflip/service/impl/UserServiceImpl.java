package com.eduglasses.frontflip.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduglasses.frontflip.controllers.form.RegistrationForm;
import com.eduglasses.frontflip.dao.RoleDao;
import com.eduglasses.frontflip.dao.UserDao;
import com.eduglasses.frontflip.domain.Role;
import com.eduglasses.frontflip.domain.User;
import com.eduglasses.frontflip.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Override
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	public void saveUser(RegistrationForm registrationForm) {
		String email = registrationForm.getEmail();
		String roleType = registrationForm.getRole();

		Role role = roleDao.getRoleByType(roleType);

		User user = new User();
		user.setEmail(email);
		user.setRole(role);
		user.setPassword(registrationForm.getPassword());
		user.setCreated(new Date());

		userDao.saveUser(user);
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
}
