package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.service.AdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.directory.DirContext;

/**
 * AD域认证服务实现
 */
@Slf4j
@Service
public class AdServiceImpl implements AdService {

    @Resource
    private LdapTemplate ldapTemplate;

    @Value("${ldap.user-search-base}")
    private String userSearchBase;

    @Value("${ldap.user-search-filter}")
    private String userSearchFilter;

    @Override
    public boolean authenticate(String username, String password) {
        try {
            // 1. 构建用户搜索过滤器
            Filter filter = new EqualsFilter("sAMAccountName", username);
            String userDn = ldapTemplate.searchForObject(
                    userSearchBase,
                    filter.encode(),
                    ctx -> ctx.getNameInNamespace()
            );

            // 2. 尝试绑定（验证密码）
            DirContext ctx = ldapTemplate.getContextSource().getContext(userDn, password);
            ctx.close();

            log.info("AD域认证成功: {}", username);
            return true;
        } catch (Exception e) {
            log.error("AD域认证失败: {}, 原因: {}", username, e.getMessage());
            return false;
        }
    }
} 