package com.eduglasses.frontflip.controllers;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eduglasses.frontflip.controllers.form.ForgetPasswordForm;
import com.eduglasses.frontflip.controllers.form.LoginForm;
import com.eduglasses.frontflip.domain.User;
import com.eduglasses.frontflip.service.UserService;
import com.eduglasses.frontflip.util.FrontFlipUtil;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(ModelMap map) {
		LoginForm form = new LoginForm();
		map.addAttribute("loginForm", form);
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String registerUser(@Valid LoginForm loginForm,
			BindingResult result, ModelMap map, HttpServletRequest request) {

		if (result.hasErrors()) {
			return "login";
		}

		boolean isUserExists = true;
		String email = loginForm.getEmail();

		String roleType = "user";
		loginForm.setRole(roleType);

		User user = userService.getUserByEmail(email);

		if (user == null || !loginForm.getPassword().equals(user.getPassword())) {
			map.addAttribute("loginFailed", "loginFailed");
			return "login";
		}

		HttpSession session = request.getSession();
		session.setAttribute("email", email);
		session.setAttribute("roleType", roleType);

		return "redirect:/home.htm";

		/*
		 * if(roleType.equals("security")) {
		 * 
		 * }
		 */
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.GET)
	public String getForgetPasswordPage(ModelMap map) {
		ForgetPasswordForm form = new ForgetPasswordForm();
		map.addAttribute("forgetPasswordForm", form);
		return "forgetPassword";
	}

	@RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
	public String forgetPassword(@Valid ForgetPasswordForm forgetPasswordForm,
			BindingResult result, ModelMap map, HttpServletRequest request) {

		if (result.hasErrors()) {
			return "forgetPassword";
		}

		String email = forgetPasswordForm.getEmail();
		User user = userService.getUserByEmail(email);

		if (user == null) {
			map.addAttribute("passwordNotSent", "passwordNotSent");
			return "forgetPassword";
		} else {
			// email password here
			String password = user.getPassword();
			try {
				FrontFlipUtil.sendEmailToUser(email, password);
				map.addAttribute("passwordSent", "passwordSent");
			} catch (MessagingException e) {
				map.addAttribute("passwordNotSent", "passwordNotSent");
				e.printStackTrace();
			}
			return "forgetPassword";
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
