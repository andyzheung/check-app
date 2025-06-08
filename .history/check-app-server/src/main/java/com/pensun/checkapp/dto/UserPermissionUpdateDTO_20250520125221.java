package com.pensun.checkapp.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户权限更新DTO
 */
@Data
public class UserPermissionUpdateDTO {
    
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 权限代码列表
     */
    private List<String> permissions;
} 