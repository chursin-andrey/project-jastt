package com.sample.business.services.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jastt.dal.providers.UserDataProvider;
import com.jastt.utils.annotations.SessionScope;
import com.sample.business.domain.entities.User;
import com.sample.business.services.CurrentUserService;

@Service
@SessionScope
public class CurrentUserServiceImpl implements CurrentUserService {

	@Autowired
	private transient UserDataProvider dataProvider;

	@Override
	public User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		String email = (String) subject.getPrincipal();
		return dataProvider.getUserByEmail(email);
	}
}
