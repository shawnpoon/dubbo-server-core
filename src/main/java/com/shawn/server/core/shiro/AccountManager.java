package com.shawn.server.core.shiro;

public interface AccountManager<T extends AuthenticationUser> {

	public boolean login(String username, String password);

	public T findByUsername(String username);

}
