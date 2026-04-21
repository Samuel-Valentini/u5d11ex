package samuelvalentini.u5d11ex.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.exception.UnauthorizedException;
import samuelvalentini.u5d11ex.service.EmployeeService;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTool tokenTools;
    private final EmployeeService employeeService;

    public TokenFilter(TokenTool tokenTool, EmployeeService employeeService) {
        this.tokenTools = tokenTool;
        this.employeeService = employeeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Incorrect token");

        String accessToken = authHeader.substring(7);

        tokenTools.verifyToken(accessToken);

        //1. estraiamo id dal token
        Long userId = this.tokenTools.extractIdFromToken(accessToken);
        // 2. find
        Employee authenticatedEmployee = this.employeeService.findById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedEmployee, null, authenticatedEmployee.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {


        return new AntPathMatcher().match("/auth/**", request.getServletPath());


    }
}