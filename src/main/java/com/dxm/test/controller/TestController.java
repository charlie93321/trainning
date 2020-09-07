package com.dxm.test.controller;

import com.dxm.test.entity.Result;
import com.dxm.test.service.TestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    private static final Logger LOGGER = Logger.getLogger(TestController.class);

    @RequestMapping("/hello")
    public String justHello() {
        LOGGER.info("进入请求，开始.......");
        HashMap<String, Object> resultMap = new HashMap<>();

        resultMap.put("hello", "world");
        LOGGER.info("请求结束，返回结果.......");
        return "HelloWorld";

    }


    @RequestMapping("/test2")
    public ModelAndView justTest2() {
        LOGGER.info("进入请求。。。。。");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("test2");

        List<Student> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Student student = new Student();
            student.setId(1L + i);
            student.setName(String.format("tom_%d", i + 1));
            student.setAge(12 + i);
            student.setRemark("描述xxx");
            list.add(student);
        }
        modelAndView.addObject("contents", list);
        LOGGER.info("将数据放入modelView中");
        return modelAndView;

    }

    @RequestMapping("/test3")
    public ModelAndView justTest3() {
        LOGGER.info("进入请求。。。。。");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("test3");
        LOGGER.info("将数据放入modelView中");
        return modelAndView;
    }


    @RequestMapping("/test3/data")
    public @ResponseBody
    Object justTest3Data(PageInfo<Student> pageCondition) {
        LOGGER.info("进入请求。。。。。");

        List<Student> list = new ArrayList<>();

        Integer total = 30;
        Integer pageSize = pageCondition.getPageSize();
        Integer currentPage = pageCondition.getPage();
        Integer startRow = (currentPage - 1) * pageSize;
        startRow = startRow == null ? 0 : startRow;
        pageSize = pageSize == null ? 10 : pageSize;

        for (int i = Math.min(startRow, total); i < Math.min(pageSize + startRow, total); i++) {
            Student student = new Student();
            student.setId(1L + i);
            student.setName(String.format("tom_%d", i + 1));
            student.setAge(12 + i);
            student.setPhone(UUID.randomUUID().toString());
            student.setSex(i % 2 == 0 ? "男" : "女");
            student.setRemark("描述xxx");
            list.add(student);
        }

        pageCondition.setRows(list);
        pageCondition.setTotal(total);
        pageCondition.setTotalPages(getPages(total, pageSize));

        return pageCondition;
    }

    public static int getPages(int total, int pageSize) {
        int pages = (int) (total / pageSize);
        if (total % pageSize == 0) {
            return pages;
        }
        return pages + 1;
    }


    @RequestMapping("/test4/data")
    public @ResponseBody
    Object justTest4Data(PageInfo<Student> pageCondition) {
        LOGGER.info("进入请求。。。。。");


        Integer pageSize = pageCondition.getPageSize();
        Integer currentPage = pageCondition.getPage();
        Integer startRow = (currentPage - 1) * pageSize;
        pageCondition.setStartRow(startRow);
        pageCondition.setPageSize(pageSize == null ? 10 : pageSize);


        Integer total = testService.getPageTotal(pageCondition);

        List<Student> list = testService.getPageList(pageCondition);

        pageCondition.setRows(list);
        pageCondition.setTotal(total);
        pageCondition.setTotalPages(getPages(total, pageCondition.getPageSize()));

        return pageCondition;
    }

    //
    @RequestMapping("/test5/del")
    public @ResponseBody
    Result<String> justTest5Del(Long id) {
        LOGGER.info("进入请求。。。。。/test5/del");

        testService.delData(id);

        return Result.success("ok");
    }

    @RequestMapping("/test5/add")
    public @ResponseBody
    Result<String> justTest5Add(Student student, HttpServletRequest request) {
        LOGGER.info("进入请求。。。。。/test5/add" + student);

        testService.addData(student);

        return Result.success("ok");
    }


    @RequestMapping("/test5/edit")
    public @ResponseBody
    Result<String> justTest5Edit(@RequestBody  Student student) {
        LOGGER.info("进入请求。。。。。/test5/edit" + student);

        testService.editData(student);

        return Result.success("ok");
    }

    @RequestMapping("/test5/query")
    public @ResponseBody
    Result<Student> justTest5Query(Long id) {
        LOGGER.info("进入请求。。。。。/test5/query");

        return Result.success(testService.queryData(id));
    }


}
