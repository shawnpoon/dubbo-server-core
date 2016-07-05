package com.shawn.server.core.shiro;

public interface AccountManager<T extends AuthenticationUser> {

	public T findByUsername(String username);

}
