package com.example.redispub.handler;

import com.example.redispub.utils.AuthenticationUtils;
import org.apache.tomcat.websocket.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class SocketAuthenticationInterceptor implements HandshakeInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(SocketAuthenticationInterceptor.class);
    private final static String AUTHENTICATION_TOKEN = "authentication";

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String query = request.getURI().getQuery();
        if (!StringUtils.hasText(query)) {
            throw new AuthenticationException("인증 토큰이 존재하지 않습니다");
        }

        try {
            String[] queryParams = query.split("&");
            Optional<String> findAccessToken = Arrays.stream(queryParams)
                    .filter(q -> q.startsWith(AUTHENTICATION_TOKEN + "="))
                    .map(q -> q.substring(q.lastIndexOf(AUTHENTICATION_TOKEN + "=")))
                    .findAny();
            String accessToken = findAccessToken.orElseThrow(() -> new AuthenticationException("인증 토큰이 존재하지 않습니다"));

            return AuthenticationUtils.valid(accessToken);
        }
        catch (AuthenticationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new AuthenticationException("잘못된 쿼리 파라미터 정보입니다");
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
