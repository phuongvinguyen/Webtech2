package de.tudortmund.webtech2;

import de.tudortmund.webtech2.security.FitterRealm;
import de.tudortmund.webtech2.security.FormAuthenticationFilterWithoutRedirect;
import de.tudortmund.webtech2.security.LogoutFilterWithoutRedirect;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

     @Bean
     public Realm realm(){
    return new FitterRealm();
     }

     @Bean
     public DefaultWebSecurityManager securityManager(Realm realm){
         return new DefaultWebSecurityManager(realm);
     }

     @Bean
     public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
         ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
         shiroFilterFactoryBean.setSecurityManager(securityManager);
         Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();

         // Put filters for session based auth
         filters.put("loginFilter", new FormAuthenticationFilterWithoutRedirect());
         filters.put("logoutFilter", new LogoutFilterWithoutRedirect());

         final Map<String, String> chainDefinition = new LinkedHashMap<>();
         // Put definitions

         // configuration for using session based authentication
         chainDefinition.put("/login.jsp", "loginFilter");
         chainDefinition.put("/logout", "logoutFilter");

         // Allow api calls
         chainDefinition.put("/api/**", "anon");

         // make static Angular resources globally available
         chainDefinition.put("/**", "anon");


         shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition);
         return shiroFilterFactoryBean;
     }


}
