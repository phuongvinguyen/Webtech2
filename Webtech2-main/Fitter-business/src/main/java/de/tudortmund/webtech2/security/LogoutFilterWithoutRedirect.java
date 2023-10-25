package de.tudortmund.webtech2.security;

import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LogoutFilterWithoutRedirect extends LogoutFilter {

    // From example 06

    @Override
    protected void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) {
        // do not redirect
    }
}
