package com.spring.demo.application.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "contacts")
public class Contact {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cId;
	private String name;
	private String secondName;
	@Column(unique = true)
	private String email;
	private String work;
	private String cImage;
	private String phone;
	@Column(length = 1000)
	private String description;
	
	@ManyToOne
	@JsonIgnore
	private User user;

	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}


	

	public int getcId() {
		return cId;
	}



	public void setcId(int cId) {
		this.cId = cId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSecondName() {
		return secondName;
	}



	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getWork() {
		return work;
	}



	public void setWork(String work) {
		this.work = work;
	}



	public String getImage() {
		return cImage;
	}



	public void setImage(String cImage) {
		this.cImage = cImage;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	

}
