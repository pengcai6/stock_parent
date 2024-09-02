package com.cai.stock.service;

import com.cai.stock.pojo.domain.menusPermDomain;
import com.cai.stock.pojo.entity.SysPermission;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class permissionService {
    public List<menusPermDomain> getTree(List<SysPermission> perms, long pid) {
        ArrayList<menusPermDomain> list = new ArrayList<>();
        for (SysPermission p : perms) {
            if(p.getType()<3 ){
                if (p.getPid().equals(pid)) {
                    list.add(new menusPermDomain(p.getId(), p.getTitle(), p.getIcon(), p.getUrl(), p.getName(), getTree(perms, p.getId())));
                }
            }
        }
        return list;
    }
}
