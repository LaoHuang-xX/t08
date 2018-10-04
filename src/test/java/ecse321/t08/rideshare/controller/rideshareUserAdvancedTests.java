package ecse321.t08.rideshare.controller;

import ecse321.t08.rideshare.entity.User;
import ecse321.t08.rideshare.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class rideshareUserAdvancedTests  {
    private static final String findall = "User.findAll";
    private static final String findUser = "User.findUsername";

    private static final String USER_KEY = "username";
    private static final String USER_EMAIL = "testemail@testemail.com";
    private static final String USER_FULLNAME = "testuserfullname";
    private static final String USER_FULLNAME_UPDATED = "testuserfullnameupdated";
    private static final String NONEXISTING_USER_KEY = "nonusername";
    private static final String USER_PASSWORD = "password";
    private static final boolean USER_STATUS = false;

    @Mock
    EntityManager entityManager;

    @Mock
    Query query;

    @Mock
    UserRepository userDao;

    @InjectMocks
    UserController userController;

    // it looks like you are testing user controller so move the userDao part under ecse321.t08.rideshare.repository

    @Before
    public void setMockOutput() {
        System.out.println("Setting Up Test For User Query Found");
        when(userDao.updateUser(anyString(), anyBoolean(), anyString(), anyString(), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(3).equals(USER_FULLNAME_UPDATED)) {
                User user = new User();
                user.setUsername(USER_KEY);
                user.setFullName(USER_FULLNAME_UPDATED);
                user.setEmailAddress(USER_EMAIL);
                user.setStatus(USER_STATUS);
                user.setPassword(USER_PASSWORD);
                return user;
            } else {
                return null;
            }
        });
        when(userDao.findUser(anyString(), anyString(), anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(USER_KEY)) {
                User user = new User();
                List<User> userList = new ArrayList<User>();
                user.setUsername(USER_KEY);
                user.setFullName(USER_FULLNAME);
                user.setEmailAddress(USER_EMAIL);
                user.setStatus(USER_STATUS);
                user.setPassword(USER_PASSWORD);
                userList.add(user);
                return userList;
            } else {
                return null;
            }
        });
    }


    @Test
    public void testAdvancedUserQuery() {
        System.out.println("Testing Advanced User Query Found");
        List<User> userList = userDao.findUser(USER_KEY, USER_EMAIL, USER_FULLNAME);

        assertNotNull(userList);
        assertEquals(1, userList.size());
        assertEquals(USER_FULLNAME, userList.get(0).getFullName());
    }

    @Test
    public void testAdvancedUserQueryNotFound() {
        System.out.println("Testing Advanced User Query Not Found");
        List<User> userList = userDao.findUser(NONEXISTING_USER_KEY, USER_EMAIL, USER_FULLNAME);

        assertNull(userList);
    }

    @Test
    public void testAdvancedUserUpdateQuery() {
        System.out.println("Testing Advanced User Update Query Found");
        User user = userController.updateUser(USER_KEY, USER_STATUS, USER_EMAIL, USER_FULLNAME_UPDATED, USER_PASSWORD);

        assertEquals(USER_FULLNAME_UPDATED, user.getFullName());
    }

    @Test
    public void testAdvancedUserUpdateQueryNotFound() {
        System.out.println("Testing Advanced User Update Query Not Found");
        User user = userController.updateUser(NONEXISTING_USER_KEY, USER_STATUS, USER_EMAIL, USER_FULLNAME, USER_PASSWORD);

        assertNull(user);
    }

    @Test
    public void testUserCreatePasswordIncorrectLength() {
        System.out.println("Testing Create User With Incorrect Password Length");
        String result = userController.createUser(USER_KEY, USER_STATUS, USER_EMAIL, USER_FULLNAME, "test");
        String expectedResult = "User " + USER_KEY + " could not be created, select a new username and make sure your email has not been used before.";

        assertEquals(expectedResult, result);
    }
}