package com.eduglasses.frontflip.dao;

import com.eduglasses.frontflip.domain.Role;

public interface RoleDao extends GenericDao<Role, Long> {
	
	public Role getRoleByType(String Type);
}
