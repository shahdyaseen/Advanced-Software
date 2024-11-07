package com.example.Rental.Services.UserServices;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.crypto.Data;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {
    private String secretKey="";

    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
        SecretKey sk=keyGen.generateKey();
        secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());

    }

    public String generateToken(String email) {

        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 120))
                .signWith(getKey())
                .compact();

       // return "token here ";
    }

    private Key getKey() {

        byte[] keyByte= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }
}
