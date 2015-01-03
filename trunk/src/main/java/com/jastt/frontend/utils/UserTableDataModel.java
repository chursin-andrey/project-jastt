package com.jastt.frontend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.jastt.business.domain.entities.User;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.dal.providers.pagination.LoadOptions;
import com.jastt.dal.providers.pagination.PagedList;
import com.jastt.frontend.utils.TableDataModel;
import com.jastt.dal.entities.UserEntity;

@Service
@Scope("session")
public class UserTableDataModel extends TableDataModel<User> {

	protected static final Logger LOG = LoggerFactory.getLogger(UserTableDataModel.class);

	private static final long serialVersionUID = 5386259788419219637L;
	
    @Autowired
    private UserDataProvider userDataProvider;

    @Override
    protected PagedList<User> getData(LoadOptions loadOptions) {
        return userDataProvider.getObjects(loadOptions, UserEntity.class, User.class);
    }

    @Override
    public Object getRowKey(User user) {
        return user.getId();
    }
}
