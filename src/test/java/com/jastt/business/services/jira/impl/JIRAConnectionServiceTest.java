package com.jastt.business.services.jira.impl;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
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
	public void testConnection() {
		//TODO: Autowire
    	JIRAConnectionService jcs = new JIRAConnectionServiceImpl();
    	JIRAConnection conn = jcs.getJiraConnection(serverUrl, username, password);
    	
    	MetadataRestClient metaClient = conn.restClient.getMetadataClient();
    	
    	try {
	    	ServerInfo info = metaClient.getServerInfo().claim();
	    	assertEquals(URI.create(serverUrl), info.getBaseUri());
    	} finally {
    		conn.close();
    	}
	}

}
