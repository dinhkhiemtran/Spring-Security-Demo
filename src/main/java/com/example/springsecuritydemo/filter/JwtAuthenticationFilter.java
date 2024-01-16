package com.example.springsecuritydemo.filter;

import com.example.springsecuritydemo.provider.JwtTokenProvider;
import com.example.springsecuritydemo.security.UserDetailServiceImp;
import com.example.springsecuritydemo.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String AUTHORIZATION = "Authorization";
  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailServiceImp userDetailServiceImp;

  public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailServiceImp userDetailServiceImp) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDetailServiceImp = userDetailServiceImp;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String jwt = getJwtToken(request);
    try {
      if (StringUtils.hasText(jwt) && jwtTokenProvider.validateJwt(jwt)) {
        Claims claims = jwtTokenProvider.getClaimsJwt(jwt);
        UserDetails principal = userDetailServiceImp.loadUserById(Long.parseLong(claims.getSubject()));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(context);
      }
    } catch (Exception ex) {
      logger.error("Could not set user authentication in security context", ex);
    }
    filterChain.doFilter(request, response);
  }

  private String getJwtToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(AUTHORIZATION);
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      int indexOfJwt = bearerToken.indexOf(" ");
      return bearerToken.substring(indexOfJwt + 1);
    }
    return null;
  }
}
