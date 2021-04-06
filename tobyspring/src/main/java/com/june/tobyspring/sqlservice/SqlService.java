package com.june.tobyspring.sqlservice;

import java.util.Map;

public interface SqlService {
    String getSql(String key) throws  SqlRetrievalFailureException;
}
