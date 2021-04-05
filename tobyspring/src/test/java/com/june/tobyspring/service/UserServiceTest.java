package com.june.tobyspring.service;

import com.june.tobyspring.config.AppConfiguration;
import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.june.tobyspring.dao.UserDao;
import com.june.tobyspring.dao.UserDaoJdbc;
import com.june.tobyspring.domain.Level;
import com.june.tobyspring.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringJUnitConfig(AppConfiguration.class)
public class UserServiceTest {
    @Autowired private UserService userService;
    @Autowired private UserDaoJdbc userDao;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired MailSender mailSender;
    @Autowired
    ApplicationContext context;
    List<User> users;

    @BeforeEach
    public void setUp(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", "hj1@wafour.com", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER-1, 0),
                new User("joytouch", "강명성", "p2", "hj2@wafour.com", Level.BASIC, UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", "hj3@wafour.com", Level.SILVER, 60, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD-1),
                new User("madnite1", "이상호", "p4", "hj4@wafour.com", Level.SILVER, 60, UserServiceImpl.MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", "hj5@wafour.com", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @Test
    public void bean(){
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));
        checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size(), is(2));
        assertThat(request.get(0), is(users.get(1).getEmail()));
        assertThat(request.get(1), is(users.get(3).getEmail()));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    private void checkLevel(User user, Boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded)
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        else
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel(), is(userWithLevelRead.getLevel()));
        assertThat(userWithoutLevel.getLevel(), is(Level.BASIC));

    }

    static class TestUserService extends UserServiceImpl {
        private  String id;

        private TestUserService(String id){
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id))
                throw new TestUserServiceException();
            super.upgradeLevel(user);
        }

        static class TestUserServiceException extends RuntimeException {
        }
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();


        userDao.deleteAll();
        for(User user : users) userDao.add(user);

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch (TestUserService.TestUserServiceException e){}

        checkLevel(users.get(1),false);
    }

    //Use Mockito
    @Test
    public void mockUpgradeLevels() throws Exception{
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();


        verify(mockUserDao, times(2)).update(any());
        verify(mockUserDao, times(2)).update(any());
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));
    }

    @Test
    public void transactionSync() {
        userDao.deleteAll();
        assertEquals(0, userDao.getCount());

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        userService.deleteAll();

        userService.add(users.get(0));
        userService.add(users.get(1));
        assertEquals(2, userDao.getCount());

        transactionManager.rollback(txStatus);

        assertEquals(0, userDao.getCount());
    }

    static class MockMailSender implements MailSender{
        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException { }
    }

    static class MockUserDao implements UserDao{
        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users){
            this.users = users;
        }
        public List<User> getUpdated(){
            return this.updated;
        }
        @Override
        public List<User> getAll() {
            return this.users;
        }
        @Override
        public void update(User user) {
            updated.add(user);
        }
        @Override public void add(User user) { throw new UnsupportedOperationException(); }
        @Override public User get(String id) { throw new UnsupportedOperationException(); }
        @Override public void deleteAll() { throw new UnsupportedOperationException(); }
        @Override public int getCount() { throw new UnsupportedOperationException(); }

    }

}

