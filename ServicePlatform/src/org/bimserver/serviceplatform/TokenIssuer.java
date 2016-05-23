package org.bimserver.serviceplatform;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class TokenIssuer {

	// TODO link to user
	private final Set<String> tokens = new HashSet<>();
	
	// TODO create random tokens
	private final AtomicLong counter = new AtomicLong();
	
	public String authorizationCode() {
		String token = "t" + counter.incrementAndGet();
		tokens.add(token);
		return token;
	}
}
