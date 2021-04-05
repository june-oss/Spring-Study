package com.june.tobyspring.service;

import com.june.tobyspring.dao.UserDao;
import com.june.tobyspring.domain.Level;
import com.june.tobyspring.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

public class UserServiceImpl implements UserService{
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;
    UserDao userDao;

    private MailSender mailSender;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    public void deleteAll() { userDao.deleteAll(); }
    public User get(String id) { return userDao.get(id); }
    public List<User> getAll() { return userDao.getAll(); }
    public void update(User user) { userDao.update(user); }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for(User user: users){
            if(canUpgradeLevel(user))
                upgradeLevel(user);
        }
    }

    //Test에서 오버라이딩해서 쓰기위하여 private->protected로 변경(좋은 방법이 아니다.....)
    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEMail(user);
    }

    private void sendUpgradeEMail(User user) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드되었습니다.");

        this.mailSender.send(mailMessage);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER: return (user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }
}
