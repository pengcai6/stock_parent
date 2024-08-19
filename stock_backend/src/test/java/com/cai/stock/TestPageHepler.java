package com.cai.stock;

import com.cai.stock.mapper.SysUserMapper;
import com.cai.stock.pojo.entity.SysUser;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestPageHepler {
@Autowired
    private SysUserMapper sysUserMapper;
@Test
public void Test01(){
    Integer page=2;
    Integer pageSize=5;
    PageHelper.startPage(2,5);
    List<SysUser> all = sysUserMapper.FindAll();
    //将查询后的数据封装到pageInfo中
    PageInfo<SysUser> PageInfo = new PageInfo<>(all);
    //获取分页的详情数据
    int pageNum = PageInfo.getPageNum();//获取当前页
    int pages = PageInfo.getPages();//获取总页数
    int pageSize1 = PageInfo.getPageSize(); //获取每一页的大小
    int size = PageInfo.getSize();//当前页的记录数
    long total = PageInfo.getTotal();//总记录数
    List<SysUser> list = PageInfo.getList();//当前页的列表
    System.out.println("all = " + all);

}

}
