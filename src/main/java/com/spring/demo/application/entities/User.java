package com.spring.demo.application.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(unique = true)
	@Size(min=2, max=20, message="Name must be between 2 to 20 characters")
	private String name;
	private String role;
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message="Please enter valid email")
	private String email;
	private String password;
	private String imageUrl;
	@Column(length = 500)
	@Size(max=200, message="Write about yourself within 200 characters")
	private String about;
	private boolean enabled;
	
	@Transient
	private String captcha;
	
	@Transient
	private String hiddenCaptcha;
	
	@Transient
	private String realCaptcha;
	
	
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public String getCaptcha() {
		return captcha;
	}


	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}


	public String getHiddenCaptcha() {
		return hiddenCaptcha;
	}


	public void setHiddenCaptcha(String hiddenCaptcha) {
		this.hiddenCaptcha = hiddenCaptcha;
	}


	public String getRealCaptcha() {
		return realCaptcha;
	}


	public void setRealCaptcha(String realCaptcha) {
		this.realCaptcha = realCaptcha;
	}


	public List<Contact> getContacts() {
		return contacts;
	}


	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", role=" + role + ", email=" + email + ", password=" + password
				+ ", imageUrl=" + imageUrl + ", about=" + about + ", enabled=" + enabled + ", captcha=" + captcha
				+ ", hiddenCaptcha=" + hiddenCaptcha + ", realCaptcha=" + realCaptcha + ", contacts=" + contacts + "]";
	}

	

	
	



	


	
	
	
	
}
