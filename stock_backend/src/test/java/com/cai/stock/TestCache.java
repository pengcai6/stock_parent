package com.cai.stock;

import com.cai.stock.service.RolesService;
import com.cai.stock.vo.resp.R;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestCache {

    @Autowired
    private RolesService rolesService;


@Test
    public void test(){
    R<List<String>> ownPermission = rolesService.getOwnPermission("1237258113002901512");
}

}
