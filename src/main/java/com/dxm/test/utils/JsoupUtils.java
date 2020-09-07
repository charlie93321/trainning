package com.dxm.test.utils;

import com.dxm.test.entity.EntityContent;
import com.dxm.test.entity.ShipProduct;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class JsoupUtils {

    private static final Logger LOGGER = Logger.getLogger(JsoupUtils.class);

    public static void main(String[] args) throws Exception {
        String url = "http://123.57.17.48:8082/getProductList.htm";
        List<ShipProduct> content = getData(url);
        System.out.println(content);
    }

    public static EntityContent getContent(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);

        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");


        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            LOGGER.info(httpGet.getURI() + "  响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());

                String result = EntityUtils.toString(responseEntity);


                Document document = Jsoup.parse(result);


                String title = document.getElementById("productTitle").text();
                String price = document.getElementById("priceblock_ourprice").text();
                List<Node> nodes = document.getElementById("native_dropdown_selected_processor_description").childNodes();


                //进行异步任务列表
                List<FutureTask<MutilTask>> futureTasks = new ArrayList<FutureTask<MutilTask>>();
                //线程池 初始化十个线程 和JDBC连接池是一个意思 实现重用
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                //类似与run方法的实现 Callable是一个接口，在call中手写逻辑代码
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    if (node instanceof Element) {
                        Element element = (Element) node;
                        String value = element.attr("value");
                        String text = element.text();
                        if (StringUtils.isNotEmpty(value)) {
                            int index = Integer.parseInt(value.split(",")[0].trim());
                            value = value.split(",")[1];
                            String ItemPrice = getItem(value);
                            //builder.append(text).append(" ").append(ItemPrice).append("||");
                            FutureTask<MutilTask> mutilTaskFutureTask = new FutureTask<>(new MutilTask(index, text, value));
                            futureTasks.add(mutilTaskFutureTask);
                            executorService.submit(mutilTaskFutureTask);
                        }
                    }
                }
                String[] buffers = new String[futureTasks.size()];
                for (FutureTask<MutilTask> futureTask : futureTasks) {
                    MutilTask task = futureTask.get();
                    buffers[task.getIndex()] = task.getText() + "  " + task.getPrice();
                }

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < buffers.length; i++) {
                    if (i != buffers.length - 1) {
                        builder.append(buffers[i]).append("||");
                    } else {
                        builder.append(buffers[i]);
                    }
                }
                EntityContent content = new EntityContent();
                content.setTitle(title);
                content.setPrice(price);
                content.setItems(builder.toString());
                return content;
            }
        } catch (Exception e) {
            LOGGER.error("解析页面数据异常", e);
            throw e;
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGGER.error("资源释放失败", e);
            }
        }
        return null;
    }

    public static List<ShipProduct> getData(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
        // 响应模型
        CloseableHttpResponse response = null;

        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            LOGGER.info(httpGet.getURI() + "  响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                Gson gson = new Gson();
                Type type = new TypeToken<List<ShipProduct>>() {
                }.getType();
                List<ShipProduct> products = gson.fromJson(result, type);
                return products;
            } else {
                throw new Exception(httpGet.getURI() + "  响应状态为:" + response.getStatusLine());
            }
        } catch (Exception e) {
            LOGGER.error("解析页面数据异常:url=" + url, e);
            throw e;
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGGER.error("资源释放失败", e);
            }
        }
    }

    @Data
    public static class MutilTask implements Callable<MutilTask> {
        private String keyUrl;
        private Integer index;
        private String text;
        private String price;

        public MutilTask(Integer index, String text, String keyUrl) {
            this.index = index;
            this.text = text;
            this.keyUrl = keyUrl;
        }

        @Override
        public MutilTask call() throws Exception {
            this.price = getItem(this.keyUrl);
            return this;
        }
    }


    private static String getItem(String value) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 创建Get请求
        String url = String.format("https://www.amazon.cn/dp/%s/ref=twister_dp_update?_encoding=UTF8&th=1", value);
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
        // 响应模型
        CloseableHttpResponse response = null;

        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            LOGGER.info(httpGet.getURI() + "  响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                String result = EntityUtils.toString(responseEntity);
                Document document = Jsoup.parse(result);
                Element priceItem = document.getElementById("priceblock_ourprice");
                if (priceItem == null) {
                    priceItem = document.getElementById("priceblock_dealprice");
                }
                String price = priceItem.text();
                return price;
            } else {
                throw new Exception(httpGet.getURI() + "  响应状态为:" + response.getStatusLine());
            }
        } catch (Exception e) {
            LOGGER.error("解析页面数据异常:url=" + url, e);
            throw e;
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGGER.error("资源释放失败", e);
            }
        }

    }
}
