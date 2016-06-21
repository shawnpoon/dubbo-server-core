package com.shawn.server.core.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class UserRealm extends AuthorizingRealm {

	private PermissionManager permissionManager;
	private AccountManager<AuthenticationUser> accountManager;

	/**
	 * 取得权限及认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		String username = (String) principals.getPrimaryPrincipal();
		authorizationInfo.setRoles(permissionManager.getRoles(username));
		authorizationInfo.setStringPermissions(permissionManager.getStringPermissions(username));
		return authorizationInfo;
	}

	/**
	 * 取得身份认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		AuthenticationUser user = accountManager.findByUsername(username);

		if (user == null) {
			throw new UnknownAccountException();// 没找到帐号
		}

		if (user.getState() == null || user.getState() == 1) {
			throw new LockedAccountException(); // 帐号锁定
		}

		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(),
				user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
		
		return authenticationInfo;
	}

	public void setPermissionManager(PermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

	public void setAccountManager(AccountManager<AuthenticationUser> accountManager) {
		this.accountManager = accountManager;
	}

}
