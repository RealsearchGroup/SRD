package edu.sjsu.cmpe295b.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import java.util.Collection;

public class AuthUtil {
    public static String getUserName() {
        return getLdapDetails().getUsername();
    }

    public static Collection getUserAuthorities() {
        return getLdapDetails().getAuthorities();
    }

    private static LdapUserDetails getLdapDetails() {
        return (LdapUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
