package com.cai.stock.pojo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExterPermissionsDomain {
    Long id;
    String title;
    String icon;
    String path;
    String name;
   List<ExterPermissionsDomain> children;
}
