package com.dxm.test.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestJdbcTemplate {
    public static void main(String[] args) {

        JdbcTemplate template = new JdbcTemplate();
        BasicDataSource dateSource = new BasicDataSource();
        dateSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dateSource.setUsername("testu1");
        dateSource.setPassword("testpwd");
        dateSource.setDriverClassName("com.mysql.jdbc.Driver");
        template.setDataSource(dateSource);

      template.queryForObject("select count(1) from t_stu", Integer.class);


        //System.out.println(total);
    }
}
