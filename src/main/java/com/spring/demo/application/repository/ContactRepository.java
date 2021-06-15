package com.spring.demo.application.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.demo.application.entities.Contact;
import com.spring.demo.application.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	
	// Pageable - It has two info.
	//1. Current page
	//2. Contacts per page
	@Query("from Contact as c where c.user.id =:userId and c.name =:cName")
	public Page<Contact> findContactByUser(@Param("userId") int userId, @Param("cName") String cName, Pageable pageable);
	
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);
	
	//Search
	public List<Contact> findByNameContainingAndUser(String name, User user);
	
	
	@Query("from Contact as c where c.user.id =:userId")
	public List<Contact> findContactByUserId(@Param("userId") int userId);
	

}
