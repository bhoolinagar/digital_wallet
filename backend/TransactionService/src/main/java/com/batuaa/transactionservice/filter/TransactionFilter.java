package com.batuaa.transactionservice.filter;

import com.batuaa.transactionservice.exception.JwtTokenMissingException;
import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;

import java.io.IOException;

public class TransactionFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        // Handle preflight OPTIONS request
        if (httpRequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"JWT Token is missing\"}");
            return;
        }

        String token = authHeader.substring(7);

        try {
            JwtParser jwtParser = Jwts.parser().setSigningKey("mysecretkey");
            Jwt jwtObj = jwtParser.parse(token);
            Claims claims = (Claims) jwtObj.getBody();

            HttpSession session = httpRequest.getSession();
            session.setAttribute("userloggedin", claims.getAudience());

            // Token is valid → continue the request
            chain.doFilter(httpRequest, httpResponse);

        } catch (SignatureException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Signature mismatch\"}");
        } catch (MalformedJwtException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Token modified or invalid\"}");
        } catch (ExpiredJwtException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Token expired\"}");
        } catch (Exception e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\": \"Invalid token\"}");
        }
    }


}
