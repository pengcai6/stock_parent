package com.cai.stock.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@Component
public class permission {

    public static   List<Map<String, Object>>  findAllChildren(Long pid, List<Map<String, Object>> x) {
        List<Map<String, Object>> mapList = null;
        try {
            mapList = new ArrayList<>();
            for (Map<String, Object> map : x) {
                // 将 mPid 转换为 int 类型以进行比较
                Long mPid = (Long) map.get("pid");
                if (mPid != null && mPid.equals(pid)) {
                    mapList.add(map);
                    Long id = (Long) map.get("id");
                    Integer  type= (Integer) map.get("type");
                    if(type<3){
                        // 递归调用以找到所有子元素
                        List<Map<String, Object>> children = findAllChildren(id, x);
                        map.put("children", children); // 将子节点列表放入父节点
                    }else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapList;
        }

}
