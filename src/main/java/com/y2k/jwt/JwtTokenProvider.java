package com.y2k.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.y2k.member.MemberModel;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final long tokenValidityInSecond;

    JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
            @Value("${jwt.token-validity-in-second}") long tokenValidityInSecond) {
        
        this.secretKey = secretKey;
        this.tokenValidityInSecond = tokenValidityInSecond;
    }

    public String createToken(MemberModel member) {
        Date now = new Date();
        
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setIssuer("System")
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidityInSecond))
            .claim("id", member.getMemberId())
            .claim("name", member.getMemberName())
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public Claims decodeToken(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody();
    }
}
