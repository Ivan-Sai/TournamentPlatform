package server.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import server.config.security.JwtService;
import server.exception.ExpiredJwtException;
import server.persistance.entity.token.Token;
import server.persistance.repository.token.TokenRepository;
import server.util.SecurityUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().contains("api/open")){
            filterChain.doFilter(request,response);
            return;
        }
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        String jwt = authorization.substring(7);
        try {
            String username = jwtService.extractUsername(jwt);
            if (username != null && SecurityUtil.getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                Token token = tokenRepository
                        .findByToken(jwt)
                        .orElseThrow(() -> new RuntimeException("Invalid token"));
                if (!jwtService.isTokenExpired(jwt)) {
                    var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityUtil.setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException("Token expired");
        }

    }
}
