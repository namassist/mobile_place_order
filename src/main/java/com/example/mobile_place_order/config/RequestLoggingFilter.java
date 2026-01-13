package com.example.mobile_place_order.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * HTTP request/response logging filter.
 * Logs method, URI, status code, and execution time for all API requests.
 */
@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            
            String fullPath = queryString != null ? uri + "?" + queryString : uri;
            
            if (status >= 400) {
                log.warn("HTTP {} {} - Status: {} - Duration: {}ms", method, fullPath, status, duration);
            } else {
                log.info("HTTP {} {} - Status: {} - Duration: {}ms", method, fullPath, status, duration);
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip logging for actuator and swagger endpoints
        return path.startsWith("/actuator") || 
               path.startsWith("/swagger") || 
               path.startsWith("/v3/api-docs");
    }
}
