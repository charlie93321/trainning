package com.dxm.test.controller;

import com.dxm.test.entity.EntityContent;
import com.dxm.test.entity.Result;
import com.dxm.test.entity.ShipProduct;
import com.dxm.test.service.AsynService;
import com.dxm.test.service.JedisCacheClient;
import com.dxm.test.service.TestService;
import com.dxm.test.utils.JsoupUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/second")
public class SecondTestController {

    private static final Logger LOGGER = Logger.getLogger(SecondTestController.class);
    @Autowired
    private TestService testService;
    @Autowired
    private AsynService asynService;
    @Autowired
    private JedisCacheClient jedisCacheClient;


    @RequestMapping("/test6")
    public ModelAndView justTest3() {
        LOGGER.info("进入请求。。。。。");
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("test6");
        LOGGER.info("将数据放入modelView中");
        return modelAndView;
    }

    @RequestMapping("/test6/catchData")
    public @ResponseBody
    Result<EntityContent> catchData(String url) {
        LOGGER.info("进入请求。。。。。,需要抓取url:" + url);
        try {
            EntityContent content = JsoupUtils.getContent(url);
/*
            content.setTitle("Intel 999AC6 酷睿 i9-9920X X X 系列处理器 12 核高达 4.4 GHz 涡轮未锁 LGA2066 X299 系列 165 W 处理器");
            content.setPrice("￥4,848.35");
            content.setItems("i3-8100 Prozessor ￥848.10||i5-8600K Prozessor ￥2,098.70||i7-9700K Prozessor ￥2,591.10||i3-8300 Prozessor ￥1,376.47||i7-8700K Prozessor ￥2,946.00||Pentium Gold G5600 Prozessor ￥743.88||i3-8350K Prozessor ￥1,404.93||i5-8400 Prozessor ￥1,292.80||i5-8500 Prozessor ￥1,930.20||i5-9400 Prozessor ￥1,363.16||i3-9100F Prozessor ￥616.31||i5-8600 Prozessor ￥1,784.39||i9-9920X X-series Prozessor ￥4,848.35||i9-9940X X-series Prozessor ￥5,556.87||i9-9960X X-series Processor ￥8,081.66||i5-9600K Prozessor ￥1,735.79||i7-8700 Prozessor ￥2,154.15||i7-9700KF Prozessor ￥2,244.89||i9-9900K Prozessor ￥3,111.51||i9-9900KF Prozessor ￥2,979.35");
            */
            return Result.success(content);
        } catch (Exception e) {
            LOGGER.error("数据抓取失败", e);
            return Result.failure("数据抓取失败");
        }
    }


    @RequestMapping("/test7/saveData")
    public @ResponseBody
    Result<String> saveData(String url) {
        LOGGER.info("进入请求。。。");
        try {
            List<ShipProduct> list = JsoupUtils.getData("http://123.57.17.48:8082/getProductList.htm");
            testService.saveProductList(list);
            return Result.success("");
        } catch (Exception e) {
            LOGGER.error("数据保存失败", e);
            return Result.failure("数据保存失败");
        }
    }


    @RequestMapping("/test7/asynDetach")
    public @ResponseBody
    Result<String> asynDetach(String key) {
        LOGGER.info("进入请求。。。/test7/asynDetach");
        try {
            String uuid = UUID.randomUUID().toString();
            jedisCacheClient.set(uuid, "0");
            asynService.PermutationAndcombination(uuid);
            LOGGER.info("finish 请求。。。/test7/asynDetach");
            return Result.success(uuid);
        } catch (Exception e) {
            LOGGER.error("数据保存失败", e);
            return Result.failure("数据保存失败");
        }
    }

    @RequestMapping("/test7/getProcess")
    public @ResponseBody
    Result<String> getProcess(String uuid) {
        LOGGER.info("进入请求。。。/test7/getProcess");
        try {
            String data = jedisCacheClient.get(uuid);
            LOGGER.info("finish 请求。。。/test7/asynDetach");
            return Result.success(data);
        } catch (Exception e) {
            LOGGER.error("getProcess", e);
            return Result.failure("getProcess");
        }
    }
}
