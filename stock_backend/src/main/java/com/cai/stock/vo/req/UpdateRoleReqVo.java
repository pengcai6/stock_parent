package com.cai.stock.vo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleReqVo {
    String userId;
    List<String> roleIds;
}
