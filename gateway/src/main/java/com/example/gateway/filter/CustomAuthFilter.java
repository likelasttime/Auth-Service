package com.example.gateway.filter;

import com.example.gateway.service.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {
    @Autowired
    private JwtUtil jwtUtil;

    public CustomAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String[] cookies = exchange.getRequest().getHeaders().get("Cookie").get(0).split(";");
            Map<String, String> map = new HashMap<>();
            for(int i=0; i<cookies.length; i++){
                String[] splitted = cookies[i].split("=");
                map.put(splitted[0], splitted[1]);
                if(splitted[0] == " accessToken")
                    break;
            }

            String token = map.get("accessToken");
            //String token = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
            String userName = jwtUtil.getUsername(token);

            addAuthorizationHeaders(exchange.getRequest(), userName);

            return chain.filter(exchange);
        };
    }

    private void addAuthorizationHeaders(ServerHttpRequest request, String userName) {
        request.mutate()
                .header("X-Authorization-Id", userName)
                .build();
    }

    @Bean
    public ErrorWebExceptionHandler tokenValidation() {
        return new JwtTokenExceptionHandler();
    }

    public class JwtTokenExceptionHandler implements ErrorWebExceptionHandler {
        private String getErrorCode(int errorCode) {
            log.info("JwtTokenExceptionHandler errorCode: " + errorCode);
            return String.format("errorCode: %d", errorCode);
        }

        @Override
        public Mono<Void> handle(
                ServerWebExchange exchange, Throwable ex) {
            int errorCode = 500;
            if (ex.getClass() == NullPointerException.class) {
                errorCode = 100;
            } else if (ex.getClass() == ExpiredJwtException.class) {
                errorCode = 200;
            }

            byte[] bytes = getErrorCode(errorCode).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Flux.just(buffer));
        }
    }

    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;

        public Config(String baseMessage, boolean preLogger, boolean postLogger) {
            this.baseMessage = baseMessage;
            this.preLogger = preLogger;
            this.postLogger = postLogger;
        }
    }
}
