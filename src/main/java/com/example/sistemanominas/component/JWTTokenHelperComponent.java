package com.example.sistemanominas.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parser;

@Component
public class JWTTokenHelperComponent {

    @Value("${jwt.auth.app}")
    private String appName;

    @Value("${jwt.auth.secret_key}")
    private String secretKey;

    @Value("${jwt.auth.expires_in}")
    private int expiresIn;

    private static final SignatureAlgorithm ALGORITHM;

    static {
        ALGORITHM = SignatureAlgorithm.HS512;
    }

    private Claims getAllClaimsFromToken(final String token) {
        return parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public String getLoginFromToken(final String token) {
        String login;
        final Claims claims = this.getAllClaimsFromToken(token);
        login = claims.getSubject();
        return login;
    }

    public String generateToken(final String login) {
        return builder().setIssuer(appName).setSubject(login).setIssuedAt(
                new Date()).setExpiration(generateExpirationDate()).signWith(ALGORITHM, secretKey).compact();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn * 100_000L);
    }

    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String login = getLoginFromToken(token);
        return login != null && login.equals(userDetails.getUsername())
                && !isTokenExpired(token) && userDetails.isEnabled();
    }

    public boolean isTokenExpired(final String token) {
        return getExpirationDate(token).before(new Date());
    }

    private Date getExpirationDate(final String token) {
        Date expireDate;
        final Claims claims = this.getAllClaimsFromToken(token);
        expireDate = claims.getExpiration();
        return expireDate;
    }

    public String getToken(final HttpServletRequest request) {
        final String authHeader = getAuthHeaderFromHeader(request);
        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    public String getAuthHeaderFromHeader(final HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
