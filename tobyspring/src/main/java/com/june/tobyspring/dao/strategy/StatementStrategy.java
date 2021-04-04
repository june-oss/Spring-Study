package com.june.tobyspring.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//TODO  3. 전략 패턴(Strategy pattern) - 오브젝트를 아예 분리하고 클래스 레벨에서 인터페이스를 통해 위임하는 방식
public interface StatementStrategy {
    PreparedStatement makePreparedStatement(Connection c) throws SQLException;
}
