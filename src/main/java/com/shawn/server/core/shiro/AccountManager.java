package com.shawn.server.core.shiro;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;

public interface AccountManager<T extends AuthenticationUser> {

	public T findByUsername(String username);

	boolean login(String username, String password) throws UnknownAccountException, ExcessiveAttemptsException,
			LockedAccountException, IncorrectCredentialsException;

}
