package com.cai.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class TestPasswordEncoder {
@Autowired PasswordEncoder passwordEncoder;

    /**
     * 测试密码加密
     */
    @Test
    public  void test01() {
        String pwd ="123456";
        String encode = passwordEncoder.encode(pwd);
        System.out.println("encode = " + encode);
    }

    /**
     * 测试密码匹配
     */
    @Test
    public  void test02() {
    String pwd ="123456";
    String enPwd="$2a$10$gotuVxq.EVO/jOdxArmXVO6rNXza3J3uakleInMkDrMHhEyo846C2";
        boolean isSuccess = passwordEncoder.matches(pwd, enPwd);
        System.out.println(isSuccess?"匹配成功":"匹配失败");
    }
}
