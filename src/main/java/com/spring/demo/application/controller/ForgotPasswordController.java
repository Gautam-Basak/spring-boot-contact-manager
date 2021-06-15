package com.spring.demo.application.controller;

import java.util.Random;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.demo.application.Service.EmailService;

@Controller
public class ForgotPasswordController {

	@Autowired
	private EmailService emailService;

	// Generate random 5 digit number
	Random random = new Random(1000);

	// To open Forget Password Form
	@RequestMapping("/forgot-pass")
	public String openForgotPassForm() {

		return "forgot_pass_form";
	}

	// Handler to Send OTP
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, HttpSession session) {

		System.out.println("Email " + email);

		// Generate random 5 digits next number
		long otp = random.nextInt(99999);

		System.out.println("OTP " + otp);

		String subject = "OTP from Contact Manager";
		String message = "Your One Time Password: " + otp;
		String to = email;

		boolean flag = this.emailService.sendEmail(subject, message, to);

		if (flag) {

			session.setAttribute("myOtp", otp);
			session.setAttribute("email", email);
			return "verify_otp_form";
		} else {

			session.setAttribute("message", "Please check your email id..!");
			return "forgot_pass_form";
		}

	}

	// Verify OTP
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") long otp, HttpSession session) {

		long myOtp = (long) session.getAttribute("myOtp");
		String email = (String) session.getAttribute("email");

		if (myOtp == otp) {

			// Return Change Password Page
			return "change_password_form";
		}

		session.setAttribute("message", "OTP is wromg, please check.");
		return "verify_otp_form";
	}
	
	
}
