package samuelvalentini.u5d11ex.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import samuelvalentini.u5d11ex.exception.UnauthorizedException;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTool tokenTools;

    public TokenFilter(TokenTool tokenTool) {
        this.tokenTools = tokenTool;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Incorrect token");

        String accessToken = authHeader.substring(7);

        tokenTools.verifyToken(accessToken);


        filterChain.doFilter(request, response);
    }

    @Override

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        

        return new AntPathMatcher().match("/auth/**", request.getServletPath());


    }
}