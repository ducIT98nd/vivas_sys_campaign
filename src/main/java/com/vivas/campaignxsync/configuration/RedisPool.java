package com.vivas.campaignxsync.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class RedisPool {
    private static final Logger logger = LoggerFactory.getLogger(RedisPool.class);

    @Value("${redis-config.ip.redis-server-ip}")
    private String ip;

    @Value("${redis-config.port.redis-server-port}")
    private int port;

    @Value("${redis-config.max-total}")
    private int maxTotal;

    @Value("${redis-config.max-idle}")
    private int maxIdle;

    @Value("${redis-config.min-idle}")
    private int minIdle;

    @Value("${redis-config.timeout}")
    private int timeout;

    @Value("${redis-config.password}")
    private String password;

    private static JedisPool jedisPool;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getTimeout() {
        return timeout;
    }

    public Jedis getRedisConnection() {
        if(jedisPool == null){
            synchronized (RedisPool.class) {
                if(jedisPool == null) {
                    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
                    jedisPoolConfig.setMaxTotal(maxTotal);
                    jedisPoolConfig.setMaxIdle(maxIdle);
                    jedisPoolConfig.setMinIdle(minIdle);
                    jedisPoolConfig.setBlockWhenExhausted(true);
                    jedisPoolConfig.setTestOnBorrow(true);
                    jedisPoolConfig.setTestOnReturn(true);
                    jedisPoolConfig.setTestWhileIdle(true);
                    if (password == null || password.isEmpty()){
                        jedisPool = new JedisPool(jedisPoolConfig, ip,port,timeout);
                    } else {
                        jedisPool = new JedisPool(jedisPoolConfig, ip,port,timeout, password);
                    }
                }
            }
        }

        return jedisPool.getResource();
    }
}
