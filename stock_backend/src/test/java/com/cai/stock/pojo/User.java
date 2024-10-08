package com.cai.stock.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author by itheima
 * @Date 2021/12/19
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@HeadRowHeight(value = 35) // 表头行高
@ContentRowHeight(value = 25) // 内容行高
@ColumnWidth(value = 50) // 列宽
public class User implements Serializable {
    @ExcelProperty(value = {"用户名","用户基本信息"},index = 0)
    private String userName;
    @ExcelProperty(value = {"年龄"},index = 2)
    private Integer age;
    @ExcelProperty(value = {"地址","用户基本信息"},index = 1)
    private String address;
    @ExcelProperty(value = {"生日"},index = 3)
    @DateTimeFormat("yyyy/MM/dd")
    private Date birthday;
}