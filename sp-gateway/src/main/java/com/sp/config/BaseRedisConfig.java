package com.kotoha.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Description: Redis 基础配置类
 */
public class BaseRedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {

        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<String,Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //开始进行序列化
        FastJsonRedisSerializer<String> stringFastJsonRedisSerializer = new FastJsonRedisSerializer<String>(String.class);
        //对value值进行序列化
        redisTemplate.setValueSerializer(stringFastJsonRedisSerializer);
        //对key值进行序列化
        redisTemplate.setKeySerializer(stringFastJsonRedisSerializer);

        redisTemplate.setHashValueSerializer(stringFastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringFastJsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;







//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String,Object>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        //进行序列化的操作
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        redisTemplate.setValueSerializer(redisSerializer);
//        //进行hash的序列化
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(redisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
    }

    @Bean
    public RedisSerializer<Object> redisSerializer() {
        //创建JSON序列化器
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //必须设置，否则无法将JSON转化为对象，会转化成Map类型
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

//    @Bean
//    public IRedisService redisService(RedisTemplate<String, Object> redisTemplate) {
//        return new RedisServiceImpl(redisTemplate);
//    }
}
