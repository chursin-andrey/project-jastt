package com.jastt.business.services;

import com.jastt.business.domain.entities.User;

public interface CurrentUserService {
	User getCurrentUser();
	boolean currentUserIsAdmin(User user);
}
