package com.runners.security.jwt;

import com.runners.exception.message.ErrorMessage;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${eLibrary.app.jwtSecret}") //yml dosyasına tanımladıgımız secretkey
    private  String jwtSecret;
    @Value("${eLibrary.app.jwtExpirationMs}")
    private Long jwtExpirationMs;


    //token generate
    public String jwtTokenGenerator(UserDetails userDetails){
        return Jwts.builder().
                setSubject(userDetails.getUsername()).
                setIssuedAt(new Date()).setExpiration(new Date(new Date().getTime()+ jwtExpirationMs)).
                signWith(SignatureAlgorithm.HS512,jwtSecret).
                compact();
    }

    //Tokenden emaile ulaşmak
    public String getEmailFromToken(String token){
        return Jwts.parser().
                setSigningKey(jwtSecret).
                parseClaimsJws(token).
                getBody().
                getSubject();
    }

    //validate token
    public boolean validatedJwtToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
                 IllegalArgumentException e) {
            logger.error(String.format(
                    ErrorMessage.JWT_TOKEN_ERROR_MESSAGE, e.getMessage()));
        }
        return false;
    }







}
