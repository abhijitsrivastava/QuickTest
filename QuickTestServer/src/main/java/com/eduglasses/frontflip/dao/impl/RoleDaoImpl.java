package com.eduglasses.frontflip.dao.impl;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.eduglasses.frontflip.dao.RoleDao;
import com.eduglasses.frontflip.domain.Role;
import com.eduglasses.frontflip.util.FrontFlipUtil;

;

@Repository("roleDao")
public class RoleDaoImpl extends GenericDaoImpl<Role, Long> implements RoleDao {

	private static final Logger logger = Logger.getLogger(RoleDaoImpl.class);

	@Override
	public Role getRoleByType(String type) {
		Session session = null;
		Role role = null;
		try {
			session = getSessionFactory().openSession();
			String queryString = "from Role where roleType = :type";
			Query query = session.createQuery(queryString);
			query.setString("type", type);
			role = (Role) query.uniqueResult();
		} catch (Exception e) {
			logger.fatal(FrontFlipUtil.getExceptionDescriptionString(e));
			throw e;
		} finally {
			session.close();
		}
		return role;
	}

}
