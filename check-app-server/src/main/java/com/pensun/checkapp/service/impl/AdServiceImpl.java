package com.pensun.checkapp.service.impl;

import com.pensun.checkapp.exception.AdAuthenticationException;
import com.pensun.checkapp.service.AdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * AD域认证服务实现
 */
@Slf4j
@Service
public class AdServiceImpl implements AdService {

    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${ldap.base}")
    private String ldapBase;

    @Value("${ldap.user-search-base}")
    private String userSearchBase;

    @Override
    public boolean authenticate(String username, String password) {
        try {
            // 1. 构建用户DN
            String userDn = String.format("CN=%s,%s", username, userSearchBase);

            // 2. 构建LDAP环境配置
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, userDn);
            env.put(Context.SECURITY_CREDENTIALS, password);

            // 3. 尝试连接（验证密码）
            DirContext ctx = new InitialDirContext(env);
            ctx.close();

            log.info("AD域认证成功: {}", username);
            return true;
        } catch (NamingException e) {
            log.error("AD域认证失败: {}, 原因: {}", username, e.getMessage());
            return false;
        }
    }
} 