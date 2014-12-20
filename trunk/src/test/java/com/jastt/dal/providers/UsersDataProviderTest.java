package com.jastt.dal.providers;

import java.util.Date;
import java.util.List;
import java.util.Set;
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

import com.jastt.business.domain.entities.Assignee;
import com.jastt.business.domain.entities.Issue;
import com.jastt.business.domain.entities.Project;
import com.jastt.business.domain.entities.Server;
import com.jastt.business.domain.entities.User;
import com.jastt.business.services.AssigneeService;
import com.jastt.dal.entities.UserEntity;
import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.pagination.LoadOptions;
import com.jastt.utils.CollectionUtils;
import com.jastt.utils.HsqlDatabase;
import com.jastt.utils.TestConstants;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:testApplicationContext.xml" })

public class UsersDataProviderTest {

	@Autowired
	private UserDataProvider dataProvider;
	@Autowired
	private IssueDataProvider issueDataProvider;
	@Autowired
	private ProjectDataProvider projectDataProvider;
	@Autowired
	private AssigneeDataProvider assigneeDataProvider;
	@Autowired
	private AssigneeService assigneeService;

	

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
		String userRole = "user";
		
		Server server = new Server();
		server.setId(1);

		server.setUrl("url");
		
		User user = new User();
		user.setLogin(login);
		user.setEmail(email);
		user.setName(name);
		user.setUserRole(userRole);
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
		Project project = projectDataProvider.getProjectByName("demo pro");
		Set<Assignee> assignees = assigneeService.getAssigneesByProject(project);
		for(Assignee asg : assignees) {
			System.out.println("Assignee printed>> " + asg.getName());
		}
		//Issue issue = issueDataProvider.getLatestIssue(project);
		//System.out.println("Latest Issue key " + issue.getKey());
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
