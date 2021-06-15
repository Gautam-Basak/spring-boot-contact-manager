package com.spring.demo.application.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.DocumentException;
import com.spring.demo.application.entities.Contact;
import com.spring.demo.application.entities.User;
import com.spring.demo.application.helper.Message;
import com.spring.demo.application.report.ContactsPdfReport;
import com.spring.demo.application.repository.ContactRepository;
import com.spring.demo.application.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("UserName " + username);
		User user = userRepository.getUserByUserName(username);
		model.addAttribute("use", user);
	}

	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) {

		String username = principal.getName();
		User user = userRepository.getUserByUserName(username);

		List<Contact> contacts = this.contactRepository.findContactByUserId(user.getId());
		int count = contacts.size();

		model.addAttribute("count", count);
		model.addAttribute("title", "Private Dashboard");
		return "normal/user-dashboard";
	}

	// To open the contact form -- Handler
	@RequestMapping("/add_contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title", "Add Contact");
		return "normal/add-contact-form";

	}

	// Process individual contact i.e save
	@PostMapping("/process_contact")
	public String saveContact(@ModelAttribute("contact") Contact contact, @RequestParam("cImage") MultipartFile file,
			Model model, Principal principal) {
		try {

			String username = principal.getName();
			User user = userRepository.getUserByUserName(username);

			if (file.isEmpty()) {

				contact.setImage("default_contact.png");

			} else {

				// Set the name in contact
				contact.setImage(file.getOriginalFilename());

				// Where to upload so here we decide the path
				File saveFile = new ClassPathResource("static/img").getFile();

				// Copy the file to the folder in the mentioned path --- From getAbsolutePath we
				// will get upto "static/img"
				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			contact.setUser(user);

			contactRepository.save(contact);

			model.addAttribute("contact", new Contact());

			return "normal/add-contact-form";

		} catch (Exception e) {
			e.printStackTrace();

		}
		return "normal/add-contact-form";
	}

	// Display Contacts and Search Contact with Keyword = cName.
	@GetMapping("/show_contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, @Param("cName") String cName, Model model,
			Principal principal) {
		model.addAttribute("title", "List of Contacts");

		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		Pageable pageable = PageRequest.of(page, 5);

		if (cName != null) {

			// To find the contact with Keyword
			Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), cName, pageable);

			model.addAttribute("cName", cName);
			model.addAttribute("contacts", contacts);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", contacts.getTotalPages());

		} else {

			// To find the contact with Keyword
			Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(), pageable);

			model.addAttribute("cName", cName);
			model.addAttribute("contacts", contacts);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", contacts.getTotalPages());

		}

		return "normal/view-contacts";

	}

	// Handler for Delete Contact
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, Principal principal,
			HttpSession sesion) {

		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		if (user.getId() == contact.getUser().getId()) {

			this.contactRepository.delete(contact);

		} else {

			sesion.setAttribute("message", new Message("Something went wrong..!", "alert-danger"));
		}

		return "redirect:/user/show_contacts/0";
	}

	// To open update form
	@RequestMapping("/update_contact/{cId}")
	public String openUpdateContactForm(@PathVariable("cId") Integer cId, Model model) {

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact con = contactOptional.get();
		model.addAttribute("con", con);

		return "normal/upate-contact-form";
	}

	// Handler to update contact
	@PostMapping("/process_update")
	public String updateContact(@ModelAttribute Contact contact, @RequestParam("cImage") MultipartFile file,
			Model model, HttpSession session, Principal principal) {

		try {

			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();

			if (!file.isEmpty()) {

				/* If old file is not empty then: */

				/* 1. Delete old photo */

				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();

				/* 2. Upload new photo */

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());

			} else {

				// We will retain the old photo itself
				contact.setImage(oldContactDetail.getImage());
			}

			User user = this.userRepository.getUserByUserName(principal.getName());
			contact.setUser(user);

			this.contactRepository.save(contact);
			return "redirect:/user/show_contacts/0";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/show_contacts/0";

	}

	// PDF Report
	@RequestMapping("/pdf-report")
	public void exportToPdf(HttpServletResponse responce, Principal principal) throws DocumentException, IOException {
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);

		responce.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDate = dateFormat.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=User_List_" + currentDate + ".pdf";
		responce.setHeader(headerKey, headerValue);
		
		 List<Contact> contactList = this.contactRepository.findContactByUserId(user.getId());
		 
		 ContactsPdfReport exporter = new ContactsPdfReport(contactList);
		 exporter.export(responce);

	}

}
