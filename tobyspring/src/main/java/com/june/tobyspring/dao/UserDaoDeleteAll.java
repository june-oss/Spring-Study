package com.june.tobyspring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//TODO 2. 템플릿 메소드 패턴 - 상속을 통해 기능을 확장해서 사용
// 변하지 않는 부분은 슈퍼클래스에 두고 변하는 부분은 추상 메소드로 정의해둬서 서브클래스에서 오버라이드하여 새롭게 정의해 쓰도록 하는 것
public class UserDaoDeleteAll extends UserDAO { //
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("delete from users");
        return ps;
    }
}
// 단점 : 접근제한이 많아지고 가장 큰 문제는 DAO로직마다 상속을 통해 새로운 클래스를 만들어야 한다는 점
// ex : JDBC 메소드가 4개일경우 rodml 서브클래스를 만들어야한다.
