package com.jastt.business.services.jira.impl;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class JIRAConnectionServiceTest {

	private String serverUrl;
	private String username;
	private String password;
	
	@Before
	public void setUp() throws Exception {
		
		Properties prop = new Properties();
		
		InputStream in = getClass().getClassLoader().getResourceAsStream("jira_connection.properties");
		if (in == null) throw new FileNotFoundException("Property file not found");
		try {
			prop.load(in);
		} finally {
			in.close();
		}
		
		serverUrl = prop.getProperty("jira.url");
		username = prop.getProperty("username");
		password = prop.getProperty("password");
	}

	@Test
	@Ignore
	public void getJiraConnection_ExistingServerAndUser_ConnectionIsEstablished() throws IOException {
		fail("Not yet implemented");
	}

}
