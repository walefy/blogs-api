package org.walefy.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.walefy.service.TokenService;
import org.walefy.service.UserService;

@Component
public class JwtFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final UserService userService;

  @Autowired
  public JwtFilter(TokenService tokenService, UserService userService) {
    this.tokenService = tokenService;
    this.userService = userService;
  }


  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    Optional<String> token = this.extractToken(request);

    if (token.isPresent()) {
      String subject = this.tokenService.validateToken(token.get());
      UserDetails userDetails = this.userService.loadUserByUsername(subject);
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
          userDetails, null, userDetails.getAuthorities()
      );
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private Optional<String> extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null) {
      return Optional.empty();
    }

    return Optional.of(authHeader.replace("Bearer ", ""));
  }
}
