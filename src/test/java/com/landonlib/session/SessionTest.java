package com.landonlib.session;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionTest {
    private static String TEST_USER = "TEST";
    private static String TEST_PWD = "TEST";

    private static UserSession session;

    @Before
    public void initUserSession() {
        if (session == null) {
            session = SessionFactory.logIn(TEST_USER, TEST_PWD);
        }
    }

    @Test
    public void testUserSession () {

        Assert.assertNotNull("Session should not be NULL", session);
        Assert.assertTrue("Session is not valid", session.isValid());

    }

    @Test
    public void testLogin() {

        Assert.assertEquals("User id does not match. ", session.getUserInfo().getUserId(), "123456");

    }

    @Test
    public void testToken () {

        /* Test to make sure we get the same token and user after we have logged in. */
        UserSession newUserSession = SessionFactory.getSession(session.getUserInfo().getUserId(), session.getToken());

        Assert.assertEquals("Token does not match original token.", session.getToken(), newUserSession.getToken());
        Assert.assertEquals("User Name does not match original user.", session.getUserInfo().getUserName(), newUserSession.getUserInfo().getUserName());
        Assert.assertEquals("User Id does not match original user", session.getUserInfo().getUserId(), newUserSession.getUserInfo().getUserId());

    }
}
