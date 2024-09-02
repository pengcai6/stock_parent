package com.cai.stock.security.handler;

import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户无权限访问拒绝的处理器
 */
@Slf4j
public class StockAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     *没有认证的用户访问资源时拒绝的处理器
      * @param httpServletRequest
     * @param httpServletResponse
     * @param e 拒绝时抛出的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.warn("匿名用户访问资源拒绝，原因：{}",e.getMessage());
        //设置编码格式
        //设置编码格式
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //设置编码
        httpServletResponse.setCharacterEncoding("UTF-8");
        R<Object> error = R.error(ResponseCode.ANONMOUSE_NOT_PERMISSION);
        //响应
        httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));

    }
}
