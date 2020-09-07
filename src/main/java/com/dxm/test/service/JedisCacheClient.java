package com.dxm.test.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service //把当前类放入到spring的IOC容器中
public class JedisCacheClient {

    private static final Logger LOGGER = Logger.getLogger(JedisCacheClient.class);

    @Autowired //自动注入redis连接池
    private JedisPool jedisPool;

    /**
     * setVExpire(设置key值，同时设置失效时间 秒
     */
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);

        } catch (Exception e) {
            LOGGER.error("数据设置失败", e);
        } finally {
            this.close(jedis);
        }

    }

    /**
     * (存入redis数据)
     */
    public void expire(String key, String value, Integer times) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, times);

        } catch (Exception e) {
            LOGGER.error("数据设置失败", e);
        } finally {
            this.close(jedis);
        }
    }

    /**
     * 删除redis数据
     */
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);

        } catch (Exception e) {
            LOGGER.error("数据删除失败", e);
        } finally {
            this.close(jedis);
        }
    }

    /**
     * 获取key的值
     */
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String s = jedis.get(key);
            return s;
        } catch (Exception e) {
            LOGGER.error("数据获取失败", e);
        } finally {
            this.close(jedis);
        }
        return null;
    }

    /**
     * 释放连接
     *
     * @param jedis
     */
    public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
            if (jedis.isConnected()) {
                try {
                    jedis.disconnect();
                } catch (Exception e) {
                    LOGGER.error("资源释放置失败", e);
                }
            }
        }
    }
}