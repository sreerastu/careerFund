package com.example.Foundation.config;

import com.example.Foundation.service.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private CustomUserDetails userService;
    private JwtFilter jwtFilter;

    public SecurityConfiguration(CustomUserDetails userService, JwtFilter jwtFilter) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/authenticate", "/api/verify/authenticate", "/api/register/**", "/api/donors", "/api/stories",
                        "/api/technologies/all", "/api/blogs/all", "/api/payments", "/api/topPayments", "/api/articles/all",
                        "/clients/all", "/api/students", "/api/trainers", "/contactus/add", "/contactus/all", "/api/admins",
                        "/api/token/all", "/api/makeDefaultPayment", "/api/verification/**", "/api/resetPassword/**", "api/token/all", "/api/logout", "/blacklist/check", "/blacklist/add")
                .permitAll().antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


    }

    private static final String[] AUTH_WHITELIST = {

            "/api/vi/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/api/swagger-ui/**",
            "/api/swagger-ui.html"

    };
}