package com.blog.blog.security;

import com.blog.blog.exception.SpringBlogException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init(){
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream inputStream = getClass().getResourceAsStream("/springstore.jks");
            keyStore.load(inputStream, "nice2have".toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {

            throw new SpringBlogException("init key problem");
        }

    }

    public String generateToken(Authentication authentication){

        User user = (User) authentication.getPrincipal();
        return Jwts.builder().
                setSubject(user.getUsername()).
                signWith(getPrivateKey()).
                compact();
    }

    private Key getPrivateKey()  {


        try {
            return (PrivateKey) keyStore.getKey("springblog", "nice2have".toCharArray());
        } catch (KeyStoreException e) {
            throw new SpringBlogException("private key problem");
        } catch (NoSuchAlgorithmException e) {
            throw new SpringBlogException("private key problem");
        } catch (UnrecoverableKeyException e) {
            throw new SpringBlogException("private key problem");
        }


    }

    public boolean validateToken(String jwt){
        Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() {
        try {
            return  keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e) {
            throw new SpringBlogException("public key problem");
        }
    }

    public String getUsernameFromJWT(String jwt) {
        Claims claims = Jwts.parser()
                        .setSigningKey(getPublicKey())
                        .parseClaimsJws(jwt)
                        .getBody();

        return claims.getSubject();
    }
}
