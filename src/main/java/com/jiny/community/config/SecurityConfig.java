package com.jiny.community.config;

import com.jiny.community.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        AccountService accountService;

        public SecurityExpressionHandler expressionHandler () {
            RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
            roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

            DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
            handler.setRoleHierarchy(roleHierarchy);
            return handler;

        }

        @Override
        protected void configure (HttpSecurity http) throws Exception {
            http
                    .antMatcher("/**")
                    .authorizeRequests()
                    .mvcMatchers( "/post/list", "/","/index", "/signup").permitAll()
                    .mvcMatchers("/admin").hasRole("ADMIN")
                    .mvcMatchers("/user","/post/add").hasRole("USER")
                    .anyRequest().permitAll()  //.authenticated()
                    .expressionHandler(expressionHandler()); //accessDecisionManager의 voter가 사용하는 핸들러만 바꿔뀌움


            http.formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/", true);
            /*
            * Http Basic 은 세션방식의 인증이 아닌 서버로부터 요청받은 인증방식대로 구성한 다음 헤더에 기술해서 서버로 보내는 방식을 취한다는 점입니다.
            form 인증방식은 서버에 해당 사용자의 session 상태가 유효한지를 판단해서 인증처리를 한다는 점입니다.
            **/
            http.logout()
                    .logoutSuccessUrl("/");


        }
    }