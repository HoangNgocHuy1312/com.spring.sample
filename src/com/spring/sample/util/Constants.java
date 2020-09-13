package com.spring.sample.util;

public class Constants {
	public enum AuthenticationException {
		AUTHENTICATION_EXCEPTION(0), BADCREDENTIALS_EXCEPTION(1), DISABLED_EXCEPTION(2);

		public final int value;

		AuthenticationException(int value) {
			this.value = value;
		}
	}

	public enum Expiration {
		REMEMBER_TOKEN_EXPIRY(24 * 60 * 60);

		public final int value;

		Expiration(int value) {
			this.value = value;
		}
	}

	public static final String ACCOUNT_ACTIVATION_QUEUE = "account-activation-queue";
	public static final String PASSWORD_RESET_QUEUE = "password-reset-queue";
}
