package com.cai.stock.security.filters;

import com.cai.stock.constant.StockConstant;
import com.cai.stock.security.user.LoginUserDetail;
import com.cai.stock.security.utils.JwtTokenUtil;
import com.cai.stock.vo.req.LoginReqVo;
import com.cai.stock.vo.resp.R;
import com.cai.stock.vo.resp.ResponseCode;
import com.cai.stock.vo.resp.accessTokenLoginRespVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class JwtLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * 认证过滤器，核心作用：认证用户信息，并颁发jwt的票据
     * 设置构造，传入自定义登录url地址
     *
     * @param defaultFilterProcessesUrl
     */
    private RedisTemplate redisTemplate;

    public JwtLoginAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试去认证的方法
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        //判断请求方法必须是post提交，且提交的数据的内容必须是application/json格式的数据
        if (!request.getMethod().equalsIgnoreCase("POST") ||
        !(request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE) ||
        request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_UTF8_VALUE))) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        //获取请求参数
        //获取reqeust请求对象的发送过来的数据流
        //将数据流中的数据反序列化成Map
        LoginReqVo reqVo = new ObjectMapper().readValue(request.getInputStream(), LoginReqVo.class);
        //校验输入的验证码是否正确
        //判断redis中保存的验证码是否与输入的验证码相同（比较时忽略大小写）
        String redisCode = (String) redisTemplate.opsForValue().get(StockConstant.CHECK_PREFIX + reqVo.getSessionId());
        //设置响应格式和编码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        if (StringUtils.isBlank(redisCode) || !redisCode.equalsIgnoreCase(reqVo.getCode())) {
            //验证码输入错误
            R<Object> error = R.error(ResponseCode.CHECK_CODE_ERROR.getMessage());
            String jsonData = new ObjectMapper().writeValueAsString(error);
            response.getWriter().write(jsonData);
            return null;
        }

        String username = reqVo.getUsername();
        username = (username != null) ? username : "";
        username = username.trim();
        String password = reqVo.getPassword();
        password = (password != null) ? password : "";
        //将用户名和密码信息封装到认证票据对象下
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        //交给认证管理器认证票据对象
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 认证成功后执行的方法
     *
     * @param request
     * @param response
     * @param chain      处理器链
     * @param authResult 认证对象信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        LoginUserDetail principal = ((LoginUserDetail) authResult.getPrincipal());
        String username = principal.getUsername();
        Collection<GrantedAuthority> authorities = principal.getAuthorities();
        //生成票据
        String tokenStr = JwtTokenUtil.createToken(username, authorities.toString());
        //设置编码格式
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //设置编码
        response.setCharacterEncoding("UTF-8");
        //构建响应实体对象
        accessTokenLoginRespVo respVo = new accessTokenLoginRespVo();
        BeanUtils.copyProperties(principal, respVo);
        respVo.setAccessToken(tokenStr);
        R<accessTokenLoginRespVo> ok = R.ok(respVo);
        //响应
        response.getWriter().write(new ObjectMapper().writeValueAsString(ok));
    }

    /**
     * 认证失败后执行的方法
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        //设置编码格式
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //设置编码
        response.setCharacterEncoding("UTF-8");
        R<Object> error = R.error(ResponseCode.ERROR);
        //响应
        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
    }


}
