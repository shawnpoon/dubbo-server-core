package com.shawn.server.core.shiro;

import java.util.Set;

public interface PermissionManager {

	public Set<String> getRoles(String username);

	public Set<String> getStringPermissions(String username);

}
