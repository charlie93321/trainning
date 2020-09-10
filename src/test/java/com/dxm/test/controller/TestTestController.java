package com.dxm.test.controller;

import com.dxm.test.utils.JsoupUtils;
import net.minidev.json.JSONUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-root.xml", "classpath:spring/spring-mvc.xml"})
@ComponentScan(basePackages = {"com.dxm.test.controller", "com.dxm.test.service"})
@WebAppConfiguration
public class TestTestController {

    @Autowired
    private WebApplicationContext webApplicationContext;
    protected MockMvc mockMvc;
    private  Long id;

    /**
     * 初始化SpringmvcController类测试环境
     */
    @Before
    public void setup() throws Exception {
        //加载web容器上下文
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Transactional
    @Rollback()
    public void testAddData() throws Exception {

        // 构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/test/test5/add")

                .param("name","道玄真人")
                .param("age","110")
                .param("phone","1111111111111")
                .param("remark","他是一个好学生!!!")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8");

        // 发送请求，获取请求结果
        ResultActions perform = mockMvc.perform(request);
        // 请求结果校验
        perform.andExpect(status().isOk());

        perform.andExpect(jsonPath("$.code").value("200"));


    }


    @Test
    @Transactional
    @Rollback() // 事务自动回滚，默认是true。可以不写
    public void testDeleteData() throws Exception {

        // 构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/test/test5/del")
                .param("id","7")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8");

        // 发送请求，获取请求结果
        ResultActions perform = mockMvc.perform(request);
        // 请求结果校验
        perform.andExpect(status().isOk());

        perform.andExpect(jsonPath("$.code").value("200"));


    }


    @Test
    public void testQueryData() throws Exception {
        // 构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/test/test5/query").param("id", "7")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8");

        // 发送请求，获取请求结果
        ResultActions perform = mockMvc.perform(request);
        // 请求结果校验
        perform.andExpect(status().isOk());

        perform.andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.data.id").value("7"))
                .andExpect(jsonPath("$.data.name").value("道玄真人"));


    }


    @Test
    @Transactional
    @Rollback()
    public void testEditData() throws Exception {
        // 构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/test/test5/edit")
                .param("id", "7")
                .param("name","道玄真人")
                .param("age","110")
                .param("phone","1111111111111")
                .param("remark","他是一个好学生!!!")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON).characterEncoding("utf-8");

        // 发送请求，获取请求结果
        ResultActions perform = mockMvc.perform(request);
        // 请求结果校验
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value("0"));
        System.out.println(perform.andReturn().getResponse().getContentAsString());
    }




}
