package com.eduglasses.frontflip.dao;

import com.eduglasses.frontflip.domain.User;

public interface UserDao extends GenericDao<User, Long> {

	public void saveUser(User user);
	
	public User getUserByEmail(String email);

}
