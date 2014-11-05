package com.sample.business.services.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.business.domain.entities.User;
import com.sample.business.services.CurrentUserService;
import com.sample.dal.providers.UsersDataProvider;
import com.sample.utils.annotations.SessionScope;

@Service
@SessionScope
public class CurrentUserServiceImpl implements CurrentUserService {

	@Autowired
	private transient UsersDataProvider dataProvider;

	@Override
	public User getCurrentUser() {
		Subject subject = SecurityUtils.getSubject();
		String email = (String) subject.getPrincipal();
		return dataProvider.getUserByEmail(email);
	}
}
