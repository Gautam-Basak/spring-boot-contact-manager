package com.spring.demo.application.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.demo.application.captcha.CaptchaUtils;
import com.spring.demo.application.entities.User;
import com.spring.demo.application.helper.Message;
import com.spring.demo.application.repository.UserRepository;

import cn.apiclub.captcha.Captcha;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title", "Home");
		return "home";
	}

	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About");
		return "about";
	}

	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Get Register");

		User user = new User();
		getCaptcha(user);
		model.addAttribute("user", user);
		return "signup";
	}

	@PostMapping("/do_register")
	public String registration(@Valid @ModelAttribute("user") User user, BindingResult results,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {

			if (!agreement) {
				throw new Exception("You have not agreed the Terms and Conditions");
			}

			if (results.hasErrors()) {

				getCaptcha(user);
				model.addAttribute("user", user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			if (user.getCaptcha().equals(user.getHiddenCaptcha())) {

				userRepository.save(user);

				model.addAttribute("user", new User());

				session.setAttribute("message", new Message("Successfully Registered", "alert-success"));
				return "signup";
			} else {
				getCaptcha(user);
				model.addAttribute("user", user);
				session.setAttribute("message", new Message("Invalid Captcha", "alert-danger"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			getCaptcha(user);
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong..! " + e.getMessage(), "alert-danger"));

		}

		return "signup";

	}

	/*
	 * @PostMapping("/do_register") public ResponseEntity<User>
	 * registration(@RequestBody User user) {
	 * 
	 * sendMail(user);
	 * 
	 * user.setRole("ROLE_USER"); user.setEnabled(true);
	 * user.setPassword(passwordEncoder.encode(user.getPassword()));
	 * 
	 * userRepository.save(user);
	 * 
	 * return new ResponseEntity<User>(HttpStatus.OK);
	 * 
	 * }
	 * 
	 * private void sendMail(User user) {
	 * 
	 * final String emailToRecipient = user.getEmail(); final String emailSubject =
	 * "User Successfully Register";
	 * 
	 * SimpleMailMessage message = new SimpleMailMessage();
	 * 
	 * 
	 * message.setTo(emailToRecipient); message.setSubject(emailSubject);
	 * javaMailSender.send(message); }
	 */

	@RequestMapping("/signin")
	public String signin(Model model) {
		model.addAttribute("title", "Contact Manager-Log In");
		return "login";
	}
	
	private void getCaptcha(User user) {
		Captcha captcha = CaptchaUtils.createCaptcha(240, 70);
		user.setHiddenCaptcha(captcha.getAnswer());
		user.setCaptcha(""); // value entered by the User
		user.setRealCaptcha(CaptchaUtils.encodeCaptcha(captcha));

	}

}
