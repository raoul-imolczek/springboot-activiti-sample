package com.imolczek.school.banking.activiti;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	
	private Logger LOG = Logger.getLogger(JWTAuthorizationFilter.class);

    private static final String HEADER_STRING = "Authorization";
	private static final String TOKEN_PREFIX = "Bearer";
	
	public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }
	
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        
    	// Retrieve Authorization HTTP header
    	String header = req.getHeader(HEADER_STRING);
    	
    	// Verify whether no access token has been provided
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        
        // Read user authentication information from token
        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        
    	// Retrieve the Access Token from the HTTP header
    	String token = request.getHeader(HEADER_STRING);
		String accessToken = token.replace(TOKEN_PREFIX, "");
    	
    	try {
        	// Setting up a JWT Token processor
    		ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
    		
    		// This Key source is provided by the Keycloak server
			JWKSource keySource = new RemoteJWKSet(new URL("http://localhost:8080/auth/realms/loan/protocol/openid-connect/certs"));
			
			// Algorithm used by Keycloak to sign tokens
			JWSAlgorithm expectedJWSAlg = JWSAlgorithm.RS256;
			
			// Configuration of the JWT Processor to validate tokens against the Keycloak server
			JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
			jwtProcessor.setJWSKeySelector(keySelector);
			
			// Retrieve claims from the JWT Access Token (but if validation fails, an exception will be raised)
			JWTClaimsSet jwtClaimsSet = jwtProcessor.process(accessToken, null);
			
			/*
			 * Use this JWT parser instead of the one just above if you wish to bypass JWT Access Token signature validation
			 */
        	//JWTClaimsSet jwtClaimsSet = JWTParser.parse(accessToken).getJWTClaimsSet();
			
			// Get the user's name from the claims
			String name = jwtClaimsSet.getStringClaim("name");
			
			// Get the user's roles from the claims (considering they are all prefixed with ROLE_)
			List<String> roleList = jwtClaimsSet.getStringListClaim("role");
        	
			// Create the user's authentication context based on the claims gather in the Access Token 
            if (name != null) {
                return createUserAuthenticationContext(name, roleList);
            }
		} catch (ParseException e) {
			LOG.error("Failed to parse JWT Token", e);
			return null;
		} catch (MalformedURLException e) {
			LOG.error("Malformed JWKS URL", e);
			return null;
		} catch (BadJOSEException e) {
			LOG.error("Failed to validate the JWT Access Token", e);
			return null;
		} catch (JOSEException e) {
			LOG.error("JOSE Exception while validating the JWT Access Token", e);
			return null;
		}
        return null;
    }

	private UsernamePasswordAuthenticationToken createUserAuthenticationContext(String name, List<String> roleList) {
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		if(roleList == null) {
			LOG.info("Found authenticated user: " + name + " with NO ROLES");
		    return new UsernamePasswordAuthenticationToken(name, null, null);
		} else {
			LOG.info("Found authenticated user: " + name);
		    Iterator<String> roleIterator = roleList.iterator();
		    while(roleIterator.hasNext()) {
		    	String role = roleIterator.next();
		    	LOG.info("User " + name + " has got role: " + role);
				list.add(new SimpleGrantedAuthority(role));
		    }
		    return new UsernamePasswordAuthenticationToken(name, null, list);
		}
	}
}