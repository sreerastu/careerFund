package com.example.Foundation.config;

import com.example.Foundation.Enum.UserType;
import com.example.Foundation.service.AdminServiceImpl;
import com.example.Foundation.service.DonorServiceImpl;
import com.example.Foundation.service.StudentServiceImpl;
import com.example.Foundation.service.TrainerServiceImpl;
import com.example.Foundation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private DonorServiceImpl donorService;

    @Autowired
    private TrainerServiceImpl trainerService;

    @Autowired
    private AdminServiceImpl adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            UserType userType = studentService.determineUserTypeBasedOnToken(token); // Implement this method to determine user type.

            if (userType == UserType.STUDENT) {
                userDetails = studentService.loadUserByUsername(userName);
            } else if (userType == UserType.DONOR) {
                userDetails = donorService.loadUserByUsername(userName);
            } else if (userType == UserType.ADMIN) {
                userDetails = adminService.loadUserByUsername(userName);
            }
            else {
                userDetails = this.trainerService.loadUserByUsername(userName);
            }
        if (jwtUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
        }

filterChain.doFilter(request,response);
    }
}
