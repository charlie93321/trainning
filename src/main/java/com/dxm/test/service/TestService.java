package com.dxm.test.service;

import com.dxm.test.controller.PageInfo;
import com.dxm.test.controller.Student;
import com.dxm.test.entity.ShipProduct;

import java.util.List;

public interface TestService {
    List<Student> getPageList(PageInfo<Student> pageCondition);

    Integer getPageTotal(PageInfo<Student> pageCondition);

    void delData(Long id);

    void addData(Student student);

    public Student queryData(Long id);

    public void editData(Student student);


    void saveProductList(List<ShipProduct> list);
}
