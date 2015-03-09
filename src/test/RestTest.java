package test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rest.ftp.FTPSession;

public class RestTest {

	private FTPSession session;
	
	@Before
	public void setUp() {
		session = new FTPSession();
		try {
			session.login("user_test", "user_test_password");
			session.connect();
		} catch (Exception e) {
			Assert.fail("Test failed : " + e.getMessage());
		}
	}

	@After
	public void tearDown() {
		try {
			session.close();
		} catch (IOException e) {
			Assert.fail("Test failed : " + e.getMessage());
		}
	}

}
