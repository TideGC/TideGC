package com.example.auth.config;


import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SimpleUserDetailsService implements UserDetailsService {

    private static final String tommy_authority_string = "ROLE_ADMIN, ROLE_USER, user:read, user:write, admin:read, admin:write";
    private static final String jerry_authority_string = "ROLE_USER, user:read, user:write";

    private static final List<GrantedAuthority> tommy_authority_list = AuthorityUtils.commaSeparatedStringToAuthorityList(tommy_authority_string);
    private static final List<GrantedAuthority> jerry_authorty_list = AuthorityUtils.commaSeparatedStringToAuthorityList(jerry_authority_string);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails userDetails = null;

        if (Objects.equals(username, "tommy")) {
            userDetails = new User(username, "123456", tommy_authority_list);
        } else if (Objects.equals(username, "jerry")) {
            userDetails = new User(username, "123456", jerry_authorty_list);
        } else {
            throw new UsernameNotFoundException("用户 " + username + " 不存在");
        }

        return userDetails;
    }
}
