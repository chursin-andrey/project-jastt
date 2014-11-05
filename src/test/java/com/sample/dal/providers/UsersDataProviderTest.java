package com.sample.dal.providers;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jastt.dal.exceptions.DaoException;
import com.jastt.dal.providers.UserDataProvider;
import com.jastt.dal.providers.pagination.LoadOptions;
import com.jastt.utils.CollectionUtils;
import com.sample.business.domain.entities.User;
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
	public void getUsers_TwoUsersInDatabase_AllLoadedSuccessfully() {
		List<User> users = dataProvider.getObjects(new LoadOptions());
		Assert.assertNotNull(users);

		User johnDoe = CollectionUtils.findByID(users, 1);
		assertJohnDoe(johnDoe);

		User andrewBlack = CollectionUtils.findByID(users, 2);
		assertAndrewBlack(andrewBlack);
	}

	@Test
	public void getUserByEmail_ExistingUser_UserIsLoaded() {
		User johnDoe = dataProvider.getUserByEmail(TestConstants.JOHN_DOE_EMAIL);
		assertJohnDoe(johnDoe);
	}

	@Test
	public void getUserByEmail_NonExistingUser_NullLoaded() {
		String randomEmail = UUID.randomUUID().toString();
		User user = dataProvider.getUserByEmail(randomEmail);
		Assert.assertNull(user);
	}

	@Test(expected = DaoException.class)
	public void saveUser_NullUserIsPassed_ExceptionIsThrown() {
		dataProvider.update(null);
	}

	@Test
	public void saveUser_NewUser_UserIsCreated() {

		String firstName = "Mark";
		String lastName = "Green";
		String email = "mark.green@foo.com";
		String password = "qwerty";
		Date birthday = new Date();

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setPassword(password);
		user.setBirthday(birthday);

		dataProvider.create(user);

		User persisted = dataProvider.getUserByEmail("mark.green@foo.com");
		Assert.assertNotNull(persisted);
		Assert.assertEquals(firstName, persisted.getFirstName());
		Assert.assertEquals(lastName, persisted.getLastName());
		Assert.assertEquals(email, persisted.getEmail());
		Assert.assertEquals(password, persisted.getPassword());
		Assert.assertEquals(birthday, persisted.getBirthday());
	}

	@Test
	public void saveUser_ExistingUser_UserIsUpdated() {
		User user = new User();
		user.setFirstName("Mark1");
		user.setLastName("Green1");
		user.setEmail("mark1.green1@foo.com");
		user.setPassword("qwerty1");
		user.setBirthday(new Date());

		dataProvider.create(user);

		user.setFirstName("Mark2");
		user.setLastName("Green2");
		user.setEmail("mark2.green2@foo.com");
		user.setPassword("qwerty2");
		Date newBirthday = (new DateTime()).plusDays(10).toDate();
		user.setBirthday(newBirthday);

		dataProvider.create(user);

		User persisted = dataProvider.getUserByEmail("mark2.green2@foo.com");

		Assert.assertNotNull(persisted);
		Assert.assertEquals("Mark2", persisted.getFirstName());
		Assert.assertEquals("Green2", persisted.getLastName());
		Assert.assertEquals("mark2.green2@foo.com", persisted.getEmail());
		Assert.assertEquals("qwerty2", persisted.getPassword());
		Assert.assertEquals(newBirthday, persisted.getBirthday());
	}

	private void assertJohnDoe(User user) {
		Assert.assertNotNull(user);
		Assert.assertEquals(TestConstants.JOHN_DOE_ID, user.getId());
		Assert.assertEquals("John", user.getFirstName());
		Assert.assertEquals("Doe", user.getLastName());
		Assert.assertEquals("john.doe@foo.com", user.getEmail());
		Assert.assertEquals("qwerty", user.getPassword());
		Assert.assertEquals(new DateTime("1980-01-01T00:00:00").toDate(), user.getBirthday());

	}

	private void assertAndrewBlack(User user) {
		Assert.assertNotNull(user);
		Assert.assertEquals(TestConstants.ANDREW_BLACK_ID, user.getId());
		Assert.assertEquals("Andrew", user.getFirstName());
		Assert.assertEquals("Black", user.getLastName());
		Assert.assertEquals("andrew.black@foo.com", user.getEmail());
		Assert.assertEquals("abcde", user.getPassword());
		Assert.assertEquals(new DateTime("1981-01-01T00:00:00").toDate(), user.getBirthday());

	}
}
