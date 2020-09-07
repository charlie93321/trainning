package com.dxm.test.service;

import com.dxm.test.controller.PageInfo;
import com.dxm.test.controller.Student;
import com.dxm.test.entity.ShipProduct;
import com.google.gson.Gson;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private static final Logger LOGGER = Logger.getLogger(TestServiceImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Student> getPageList(PageInfo<Student> pageCondition) {
        RowMapper<Student> rowMapper = new BeanPropertyRowMapper<Student>(Student.class);

        String sql = "select id ,name ,age,phone ,remark  from t_stu " +
                (StringUtils.isNotEmpty(pageCondition.getKeys()) ? String.format(" where name like %s", "?  ") : "  ")
                + " limit ? ,? ";
        Object[] params = null;
        if (StringUtils.isNoneEmpty(pageCondition.getKeys())) {
            params = new Object[]{
                    "%" + pageCondition.getKeys().trim() + "%"
                    , pageCondition.getStartRow(), pageCondition.getPageSize()};
        } else {
            params = new Object[]{pageCondition.getStartRow(), pageCondition.getPageSize()};
        }

        return jdbcTemplate.query(sql, params
                , rowMapper);
    }

    @Override
    public Integer getPageTotal(PageInfo<Student> pageCondition) {

        String sql = "select count(1) from t_stu " + (StringUtils.isNotEmpty(pageCondition.getKeys()) ? String.format(" where name like %s", " ?  ") : "  ");
        if (StringUtils.isNotEmpty(pageCondition.getKeys())) {
            Object[] params = new Object[]{"%" + pageCondition.getKeys().trim() + "%"};
            return jdbcTemplate.queryForObject(sql, params,
                    Integer.class);
        } else {
            return jdbcTemplate.queryForObject(sql,
                    Integer.class);
        }

    }

    @Override
    public void delData(Long id) {
        jdbcTemplate.update("delete from t_stu where id = ?", new Object[]{id});
    }

    @Override
    public void addData(Student student) {
        jdbcTemplate.update("insert into  t_stu(id,name,age,phone,remark) values(null,?,?,?,?) ",
                new Object[]{student.getName(), student.getAge(), student.getPhone(), student.getRemark()});
    }

    @Override
    public void editData(Student student) {
        jdbcTemplate.update("update   t_stu set name =? , age =? , phone =? ,remark = ? where id = ? ",
                new Object[]{student.getName(), student.getAge(), student.getPhone(), student.getRemark(), student.getId()});
    }

    @Override
    public void saveProductList(List<ShipProduct> list) {
        for (int i = 0; i < list.size(); i++) {
            ShipProduct shipProduct = list.get(i);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            PreparedStatementCreator preparedStatementCreator = con -> {
                PreparedStatement ps = con.prepareStatement("INSERT  INTO t_ship_product ( information) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, new Gson().toJson(shipProduct));
                return ps;
            };

            jdbcTemplate.update(preparedStatementCreator, keyHolder);
            LOGGER.info("数据保存成功,主键为:" + keyHolder.getKey().longValue());
        }
    }


    @Override
    public Student queryData(Long id) {
        List<Student> list = jdbcTemplate.query("select * from t_stu where id = ? ",
                new Object[]{id}, new BeanPropertyRowMapper<Student>(Student.class));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }
}
