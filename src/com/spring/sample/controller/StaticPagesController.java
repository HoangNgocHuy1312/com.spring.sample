package com.spring.sample.controller;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.sample.interceptor.Flash;
import com.spring.sample.model.CustomUserDetails;
import com.spring.sample.model.MicropostModel;
import com.spring.sample.model.UserModel;
import com.spring.sample.service.MicropostService;
import com.spring.sample.service.UserService;
import com.spring.sample.uploader.ImageUploader;

@Controller
public class StaticPagesController {

	private static final Logger logger = LoggerFactory.getLogger(StaticPagesController.class);

	@Resource
	private Flash flash;

	@Autowired
	@Qualifier("userService")
	UserService userService;
	
	@Autowired
	@Qualifier("micropostService")
	MicropostService micropostService;

	@Autowired
	@Qualifier("imageUploader")
	ImageUploader imageUploader;

	@RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
	public String home(@RequestParam(name = "page", required = false) Optional<Integer> page, Locale locale,
			Model model, Authentication authentication, HttpServletRequest request) {
		logger.info("Home Page Requested, locale = " + locale);
		if (authentication != null && authentication.isAuthenticated()) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			UserModel condition = new UserModel();
			condition.setId(userDetails.getUser().getId());
			UserModel userModel = userService.getUserInfo(condition);
			model.addAttribute("user", userModel);
			userDetails.getUser().setTotalMicropost(userModel.getTotalMicropost());
			
			MicropostModel micropostModel = new MicropostModel();
			micropostModel.setPage(page.orElse(1));
			micropostModel.setUserId(userDetails.getUser().getId());
			Page<MicropostModel> microposts = micropostService.paginate(micropostModel);
			model.addAttribute("microposts", microposts);
			
			model.addAttribute("micropost", new MicropostModel());
			
		}
		return "static_pages/home";
	}

	@RequestMapping(value = "/help", method = RequestMethod.GET)
	public String help(Locale locale, Model model) {
		logger.info("Home Page Requested, locale = " + locale);
		return "static_pages/help";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Locale locale, Model model) {
		logger.info("Home Page Requested, locale = " + locale);
		return "static_pages/about";
	}

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contact(Locale locale, Model model) {
		logger.info("Home Page Requested, locale = " + locale);
		return "static_pages/contact";
	}

	@RequestMapping(value = { "/access_denied" }, method = RequestMethod.GET)
	public String accessDenied() {
		logger.info("Access denied");
		return "access_denied";
	}
}
