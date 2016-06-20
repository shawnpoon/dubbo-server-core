package com.shawn.server.core.shiro;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordEncrypter<T extends AuthenticationUser> {

	/* Password encrypt 用户密码加密配置 */
	private String ALGORITHM_NAME = "md5";
	private int HASH_ITERATIONS = 2;

	/* Shiro权限模块 - 用户密码加密 */
	private static final RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	public void encryptPassword(T t) {
		t.setSalt(randomNumberGenerator.nextBytes().toHex());

		String newPassword = new SimpleHash(ALGORITHM_NAME, t.getPassword(),
				ByteSource.Util.bytes(t.getCredentialsSalt()), HASH_ITERATIONS).toHex();

		t.setPassword(newPassword);
	}

	public void setALGORITHM_NAME(String aLGORITHM_NAME) {
		ALGORITHM_NAME = aLGORITHM_NAME;
	}

	public void setHASH_ITERATIONS(int hASH_ITERATIONS) {
		HASH_ITERATIONS = hASH_ITERATIONS;
	}

}
