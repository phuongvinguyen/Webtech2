package de.tudortmund.webtech2.security;

import de.tudortmund.webtech2.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FitterRealm extends AuthorizingRealm implements Realm {

    final static String REALM = "FITTER";

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;

        String username = usernamePasswordToken.getUsername();
        String password = new String(usernamePasswordToken.getPassword());

        if(!userService.exists(username)){
            throw new AuthenticationException("The user does not exist!");
        }

        if(!userService.checkPassword(username, password)){
            throw new AuthenticationException("Password is wrong!");
        }

        return new SimpleAuthenticationInfo(username, password, getName());
    }
}
