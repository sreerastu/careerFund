package com.example.Foundation.config;

import com.example.Foundation.service.AdminServiceImpl;
import com.example.Foundation.service.DonorServiceImpl;
import com.example.Foundation.service.StudentServiceImpl;
import com.example.Foundation.service.TrainerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private DonorServiceImpl donorService;
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private TrainerServiceImpl trainerService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminService);
        auth.userDetailsService(donorService);
        auth.userDetailsService(studentService);
        auth.userDetailsService(trainerService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .disable().authorizeRequests().antMatchers("api/**")
                .permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}