package com.example.sistemanominas.conf.security;

import com.example.sistemanominas.component.JWTTokenHelperComponent;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Data
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final UserDetailsService user;

    @Autowired
    private final JWTTokenHelperComponent tokenHelper;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final  HttpServletResponse response, final  FilterChain filterChain) throws ServletException, IOException {
        final String authToken = tokenHelper.getToken(request);
        final var userDetails = (null == authToken) ? null : user.loadUserByUsername(tokenHelper.getLoginFromToken(authToken));
        final var validateToken = userDetails != null && tokenHelper.validateToken(authToken, userDetails);
        if (validateToken) {
            final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetails(request));
            getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
