package com.jastt.business.services.jira.impl;

import java.io.Closeable;

interface JIRAConnection extends Closeable {

	void open(String serverURL, String user, String password);
}
