package com.sp.filter;

import com.alibaba.fastjson.JSON;

import com.sp.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class AuthFilter implements GlobalFilter {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

//        定义一个白名单集合，然后获取访问的路由，如果在白名单内就放行
//        如果不在就去redis里面检查一下，查看是否放行。
        String[] whiteList = {"/user/login","user/ok","my-user/user/ok","user/test"};
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        URI uri = request.getURI();
        System.out.println(uri);

        for (String s : whiteList) {
            if(uri.getPath().contains(s)){
                System.out.println("我被放行了");
                return chain.filter(exchange);
            }
        }



        String token = request.getHeaders().getFirst("X-Token");


        if (token != null){
            token = token.replace("X-Token", "");
            Boolean b = redisTemplate.hasKey(token);

            if (b){
                System.out.println("我被放行了");
                return chain.filter(exchange);
            }
        }


        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ResponseDTO error = ResponseDTO.error(401, "登录已过期，请重新登录");
        String jsonString = JSON.toJSONString(error);
        DataBuffer wrap = response.bufferFactory().wrap(jsonString.getBytes());


        return response.writeWith(Mono.just(wrap));
    }
}
