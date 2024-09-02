package com.cai.stock.security.filters;

import com.cai.stock.security.utils.JwtTokenUtil;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定义授权过滤器，本质就是一切请求，获取请求头的token,然后进行校验
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    /**
     * 过滤执行方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain 过滤器链
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    //1.从请求头中获取token字符串
        String tokenStr = httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER);
        //2.合法性判断
        //2.1非空判断
        if (StringUtils.isBlank(tokenStr)) {
            //如果票据为空，则放行，但是此时安全上下文中肯定没有认证成功的票据，后续的过滤器如果得不到这个票据，则自动拒绝
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
          }

        //2.2检查票据是否合法
        Claims claims = JwtTokenUtil.checkJWT(tokenStr);
        //设置编码格式
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //设置编码
        httpServletResponse.setCharacterEncoding("UTF-8");

        if (claims==null) {
            //票据不合法，过滤器终止
            R<Object> error = R.error(ResponseCode.INVALID_TOKEN);
            //响应
            httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(error));
            return;
        }
        //2.3从票据字符串中获取用户名和权限信息，并组装UsernamePasswordAuXXXToken对象
        String username = JwtTokenUtil.getUsername(tokenStr);
        //["P5","ROLE_ADMIN"]
        String Roles = JwtTokenUtil.getUserRole(tokenStr);
        String stripStr = StringUtils.strip(Roles, "[]");
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stripStr);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        //3将封装的认证票据存入security的安全上下文，这样后续的过滤器可直接从安全上下文中获取用户相关的权限信息
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //4.发行请求，后续的过滤器，比如：认证过滤器发现如果上下文中存在token对象的话，无需认证
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }
}
