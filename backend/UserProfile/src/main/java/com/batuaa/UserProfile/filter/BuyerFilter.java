package com.batuaa.userprofile.filter;

import java.io.IOException;

import org.springframework.http.HttpMethod;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BuyerFilter extends GenericFilter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httprequest = (HttpServletRequest) request;
		HttpServletResponse httpresonse = (HttpServletResponse) response;

		httpresonse.setHeader("Access-Control-Allow-Origin", "*");
		httpresonse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE,OPTIONS");
		httpresonse.setHeader("Access-Control-Allow-Crendentials", "true");
		httpresonse.setHeader("Access-Control-Allow-Headers", "*");

		// to handle preflight request for the first time which is raised by web browser
		// , when ui is based on javascript , to check the availability of server
		if (httprequest.getMethod().equals(HttpMethod.OPTIONS.name())) {
			chain.doFilter(httprequest, httpresonse);
		} else {
			String authheader = httprequest.getHeader("Authorization");

			if ((authheader == null) || (!authheader.startsWith("Bearer"))) {
				throw new ServletException("JWT Token is missing");
			}

			String mytoken = authheader.substring(7);
			// System.out.println(mytoken);

			try {

				JwtParser jwtparser = Jwts.parser().setSigningKey("mysecretkey");

				// parse the JWT token

				Jwt jwtobj = jwtparser.parse(mytoken);

				Claims claim = (Claims) jwtobj.getBody();
				HttpSession httpsession = httprequest.getSession();
				// Object obj=claim.get("object")

				httpsession.setAttribute("userloggedin", claim.getAudience());
				// System.out.println (claim.getAudience() + " user " + claim.getSubject());

			} catch (SignatureException sign) {
				throw new ServletException("signature mismatch");

			}

			catch (MalformedJwtException malforn) {
				throw new ServletException("Some one modified token");
			}
		}

		chain.doFilter(httprequest, httpresonse);

	}

}
