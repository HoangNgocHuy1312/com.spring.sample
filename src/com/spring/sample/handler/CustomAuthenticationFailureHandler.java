package com.spring.sample.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.spring.sample.util.Constants;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException {

		super.onAuthenticationFailure(request, response, exception);

		int errorCode = 0;
		if (exception instanceof BadCredentialsException) {
			errorCode = Constants.AuthenticationException.BADCREDENTIALS_EXCEPTION.value;
		} else if (exception instanceof DisabledException) {
			errorCode = Constants.AuthenticationException.DISABLED_EXCEPTION.value;
		} else {
			errorCode = Constants.AuthenticationException.AUTHENTICATION_EXCEPTION.value;
		}
		setDefaultFailureUrl("/login?error=true&code=" + errorCode);
	}
}
