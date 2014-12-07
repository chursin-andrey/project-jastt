package com.jastt.dal.providers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.pagination.LoadOptions;
import com.jastt.utils.CollectionUtils;
import com.sample.utils.HsqlDatabase;
import com.sample.utils.TestConstants;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })

public class UsersDataProviderTest {

	@Autowired
	private UserDataProvider dataProvider;

	

	@BeforeClass
	public static void setUp() {
		HsqlDatabase db = new HsqlDatabase();
		db.setUp("initial,v1.0,test");
	}
	
	@Test
	public void saveUser_NewUser_UserIsCreated() {

		String login = "Mark";
		String name = "MarkName";
		String email = "mark.green@foo.com";
		String password = "qwerty";
		String _userRole = "user";
		
		Server server = new Server();
		server.setId(1);

		server.setUrl("url");
		
		User user = new User();
		user.setLogin(login);
		user.setEmail(email);
		user.setName(name);
		user.setUserRole(_userRole);
		user.setPassword(password);
		user.setServer(server);
		dataProvider.save(user, UserEntity.class);
		
		String resultReport;
		User userq = dataProvider.findById(UserEntity.class, User.class, 3);
		if(userq != null)
			resultReport = "Value is " + userq.getEmail();
		else resultReport = "There aren't any rows in table";
		System.out.println(resultReport);
	}

	@Test
	public void getUsers_TwoUsersInDatabase_AllLoadedSuccessfully() {
		List<User> users = dataProvider.getObjects(new LoadOptions(), UserEntity.class, User.class);
		System.out.println("Print Login " + users.get(1).getLogin());
	}
	
	@Test
	public void getUsers_ByLogin_AllLoadedSuccessfully() {
		String resultReport;
		User userq = dataProvider.findById(UserEntity.class, User.class, 2);
		if(userq != null)
			resultReport = "Value is " + userq.getEmail();
		else resultReport = "There aren't any rows in table";
		System.out.println(resultReport);
	}


}
