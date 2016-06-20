package com.shawn.server.core.shiro;

public interface AccountManager {

	public AuthenticationUser findByUsername(String username);

}
