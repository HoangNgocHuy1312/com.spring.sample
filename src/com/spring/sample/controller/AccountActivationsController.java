package com.spring.sample.controller;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.spring.sample.entity.Role;
import com.spring.sample.interceptor.Flash;
import com.spring.sample.model.CustomUserDetails;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.UserService;

@Controller
@EnableWebMvc
public class AccountActivationsController {
	private static final Logger logger = LoggerFactory.getLogger(AccountActivationsController.class);

	@Autowired
	MessageSource messageSource;

	@Autowired
	@Qualifier("userService")
	UserService userService;

	@Resource
	Flash flash;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	}

	@GetMapping(value = "/account_activations/{token}/edit")
	public String edit(@PathVariable String token, @RequestParam String email, Locale locale, Model model,
			HttpServletRequest request) throws Exception {
		logger.info("User is being in activation");
		Optional<UserModel> userModelOptional = userService.findUserByEmail(email);
		if (userModelOptional.isEmpty() || !userModelOptional.get().getActivationDigest().equals(token)) {
			flash.success("account_activation.token.invalid");
			flash.keep();
			return "redirect: " + request.getContextPath() + "/home";
		}
		UserModel userModel = userModelOptional.get();
		userService.active(userModel);
		authWithoutPassword(userModel);
		flash.success("account_activation.token.valid");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/users/" + userModel.getId();
	}

	private void authWithoutPassword(UserModel userModel) {
		CustomUserDetails userDetails = new CustomUserDetails(userModel);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				Role.mapRolesToAuthorities(userModel.getRole()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
