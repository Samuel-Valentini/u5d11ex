package samuelvalentini.u5d11ex.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import samuelvalentini.u5d11ex.entity.Employee;
import samuelvalentini.u5d11ex.exception.UnauthorizedException;

import java.util.Date;

@Component
public class TokenTool {
    private final String secret;


    public TokenTool(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(Employee employee) {
        Date date = new Date(System.currentTimeMillis());
        return Jwts.builder().issuedAt(date).expiration(new Date(date.getTime() + 1000L * 60 * 60 * 24 * 7)).subject(String.valueOf(employee.getEmployeeId())).signWith(Keys.hmacShaKeyFor(secret.getBytes())).compact();
    }

    public void verifyToken(String token) {
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("Unauthorized User");
        }
    }

    public Long extractIdFromToken(String token){
      return Long.valueOf(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(token).getPayload().getSubject());
    }
}
