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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.sample.interceptor.Flash;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.UserService;

@Controller
@EnableWebMvc
public class PasswordResetsController {
	private static final Logger logger = LoggerFactory.getLogger(PasswordResetsController.class);

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

	@GetMapping(value = "/password_resets/add")
	public String add(Locale locale, Model model, HttpServletRequest request) throws Exception {
		logger.info("User is requesting for forgot password");
		return "password_resets/add";
	}

	@PostMapping(value = "/password_resets")
	public String create(@RequestParam String email, Model model, final RedirectAttributes redirectAttributes,
			HttpServletRequest request) throws Exception {
		logger.info("User is being in forgot password");
		UserModel userModel = new UserModel();
		userModel.setEmail(email);
		userService.createPasswordReset(userModel);
		// Add message to flash scope
		flash.success("password_reset.create.success");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/home";
	}

	@GetMapping(value = "/password_resets/{token}/edit")
	public String edit(@PathVariable String token, @RequestParam String email, Locale locale, Model model,
			HttpServletRequest request) throws Exception {
		logger.info("User is requesting for reset password");
		Optional<UserModel> userModel = userService.findUserByEmail(email);
		if (userModel.isEmpty() || !userModel.get().getResetDigest().equals(token)) {
			flash.success("account_activation.token.invalid");
			flash.keep();
			return "redirect: " + request.getContextPath() + "/home";
		}
		model.addAttribute("token", token);
		model.addAttribute("email", email);
		return "password_resets/edit";
	}

	@PutMapping(value = "/password_resets/{token}")
	public String update(@PathVariable String token, @RequestParam String email, @RequestParam String password,
			@RequestParam String confirmation, Locale locale, Model model, HttpServletRequest request)
			throws Exception {
		logger.info("User is being in reset password");
		Optional<UserModel> userModelOptional = userService.findUserByEmail(email);
		if (userModelOptional.isEmpty() || !userModelOptional.get().getResetDigest().equals(token)) {
			flash.success("password_reset.token.invalid");
			flash.keep();
			return "redirect: " + request.getContextPath() + "/home";
		}
		if (password == null || confirmation == null || !password.equals(confirmation)) {
			flash.success("account_activation.password.invalid");
			flash.keep();
			return "password_resets/edit";
		}
		UserModel userModel = userModelOptional.get();
		userModel.setPassword(password);
		userService.changeUserPassword(userModel);
		flash.success("password_reset.token.valid");
		flash.keep();
		return "redirect: " + request.getContextPath() + "/users/" + userModel.getId();
	}

}
