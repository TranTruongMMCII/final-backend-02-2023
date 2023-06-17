package com.r2s.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.r2s.demo.model.Identification;
import com.r2s.demo.model.User;
import com.r2s.demo.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUser() {
		return this.userRepository.findAll();
	}

	public User findUserById(long id) {
		// optional: kieu du lieu rat tien dung, tu java 8
		// khi muon lay gia tri, su dung get
		// or else: neu optional co gia tri, se lay gia tri do ra, neu khong, thi se tra
		// ve gia tri ben trong orElse

		return this.userRepository.findById(id).orElse(null);
	}

	public User addNewUser(Map<String, Object> newUser) {
		User user = generateUserClass(newUser);

		Identification identification = new Identification();
		identification.setExpiredDate(new Date());
		user.setIdentification(identification);

		user = this.userRepository.save(user);
		return user;
	}

	public User updateUser(long id, Map<String, Object> newUser) {
		User user = generateUserClass(newUser);
		user.setId(id);

		user = this.userRepository.save(user);
		return user;
	}

	public void removeUser(long id) {
		this.userRepository.deleteById(id);
	}

	/**
	 * @param newUser
	 * @return
	 */
	private User generateUserClass(Map<String, Object> newUser) {
		User user = new User();
		user.setName(newUser.get("name").toString());
		user.setPassword(newUser.get("password").toString());
		user.setAddress(newUser.get("address").toString());
		return user;
	}

	public List<User> findByName(String name) {
		return this.userRepository.findByNameSQL(name);
	}
}
