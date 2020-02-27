package nl.tudelft.oopp.group39.auth.filters;

import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.tudelft.oopp.group39.auth.services.JwtService;
import nl.tudelft.oopp.group39.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {
    public static String headerAuthorization = "Authorization";
    public static String headerBearer = "Bearer ";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(headerAuthorization);

            String username = null;
            String jwt = null;

            if (authHeader != null && authHeader.startsWith(headerBearer)) {
                jwt = authHeader.substring(7);
                username = jwtService.decryptUsername(jwt);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userService.loadUserByUsername(username);

                    if (jwtService.validate(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                            );
                        usernamePasswordAuthenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext()
                            .setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expired");
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
