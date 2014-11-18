package com.jastt.business.services.jira.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.atlassian.jira.rest.client.api.MetadataRestClient;
import com.atlassian.jira.rest.client.api.domain.ServerInfo;

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
		//TODO: Autowire
    	JIRAConnectionService jcs = new JIRAConnectionServiceImpl();
    	JiraConnection conn = jcs.getJiraConnection(serverUrl, username, password);
    	
    	try {
    		MetadataRestClient metaClient = conn.restClient.getMetadataClient();
	    	ServerInfo info = metaClient.getServerInfo().claim();
	    	assertEquals(URI.create(serverUrl), info.getBaseUri());
    	} finally {
    		conn.close();
    	}
	}

}
