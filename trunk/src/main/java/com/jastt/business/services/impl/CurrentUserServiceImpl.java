package com.jastt.business.services.impl;

import java.io.Serializable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.User;
import com.jastt.business.enums.UserRoleEnum;
import com.jastt.business.services.CurrentUserService;
import com.jastt.business.services.impl.CurrentUserServiceImpl;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.frontend.beans.LoginBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service(value="currentUserService")
@Scope("session")
public class CurrentUserServiceImpl implements CurrentUserService, Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(CurrentUserServiceImpl.class);
	private static final long serialVersionUID = 3548860340897742649L;
	
	@Autowired
	private transient UserDataProvider dataProvider;
	
	private User currentUser;
	
	@Override
	public User getCurrentUser() {
		if(currentUser == null) {
			Subject subject = SecurityUtils.getSubject();
			String login = (String) subject.getPrincipal();
			currentUser = dataProvider.getUserByLogin(login);
		}
		return currentUser;
	}
	@Override
	public boolean currentUserIsAdmin(User user) {
		if (currentUser.getUserRole().equalsIgnoreCase(UserRoleEnum.ADMIN.getMark())) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
