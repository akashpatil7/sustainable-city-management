package com.tcd.ase.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;

public class JWTokenHelper {
	private static Properties securityProperties;
	private static final String SECURITY_CONFIG = "security.properties";
	private static final String TOKEN_ENC_KEY = "token_key";
	private static final String TOKEN_EXPIRY = "token_expiry_time";

	public JWTokenHelper() {
		if (securityProperties == null) {
			securityProperties = new Properties();
			InputStream properties = this.getClass().getClassLoader().getResourceAsStream(SECURITY_CONFIG);
			try {
				securityProperties.load(properties);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static byte[] getKey() {
		String key = (String) securityProperties.get(TOKEN_ENC_KEY);
		return Base64.getDecoder().decode(key);
	}

	public boolean isValid(String token) {
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			if (signedJWT != null && signedJWT.verify(new MACVerifier(getKey()))
					&& new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())) {
				return true;
			}
		} catch (ParseException | JOSEException e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getUser(String token) {
		String user = null;
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWTClaimsSet claimSet = signedJWT.getJWTClaimsSet();
			user = claimSet.getSubject();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return user;
	}

	public String generateToken(String username) {
		String jweString = new String();
		int expTime = Integer.valueOf((String)securityProperties.get(TOKEN_EXPIRY));
		Long expiryTime = new Date().getTime() + expTime * 1000;
		SignedJWT signedJWT = null;
		try {
			JWSSigner signer = new MACSigner(getKey());
			Builder builder = new JWTClaimsSet.Builder().issuer("Smart City Org").subject(username)
					.issueTime(new Date()).expirationTime(new Date(expiryTime));
			JWTClaimsSet claimSet = builder.build();
			signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimSet);
			signedJWT.sign(signer);
			jweString = signedJWT.serialize();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
         return jweString;
	}
}
