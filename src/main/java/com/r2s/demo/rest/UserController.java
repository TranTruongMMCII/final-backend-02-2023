package com.r2s.demo.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.r2s.demo.DTO.UserDTORequest;
import com.r2s.demo.DTO.UserDTOResponse;
import com.r2s.demo.constants.ResponseCode;
import com.r2s.demo.model.Identification;
import com.r2s.demo.model.User;
import com.r2s.demo.repository.RoleRepository;
import com.r2s.demo.repository.UserRepository;
import com.r2s.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseRestController {
	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;

	@GetMapping("")
	public ResponseEntity<?> getAllUsers() {
		try {
			log.error("hehlo wolrd");
			// goi toi service de lay list user
			List<User> users = this.userService.getAllUser();
//			return super.success(users);
//			List<UserDTOResponse> responses = users.stream().map(user -> new UserDTOResponse(user))
//					.collect(Collectors.toList());
			List<UserDTOResponse> responses = new ArrayList<>();

			for (User user : users) {
				responses.add(new UserDTOResponse(user));
			}
			log.info("");
			log.trace("");

			return super.success(responses);
		} catch (Exception e) {
			log.error("[UserController][getAllUsers] get exception {0}", e);
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@GetMapping("/getById")
	public ResponseEntity<User> getById(@RequestParam(name = "id", required = false, defaultValue = "2") long id) {
		User foundUser = this.userService.findUserById(id);
		if (ObjectUtils.isEmpty(foundUser)) {
			return new ResponseEntity<>(foundUser, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(foundUser, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable long id) {
		User foundUser = this.userService.findUserById(id);
		if (ObjectUtils.isEmpty(foundUser)) {
			return new ResponseEntity<>(foundUser, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(foundUser, HttpStatus.OK);
	}

//
//	// pathvariable: su dung theo 1 template bat buoc, /{id}/product/{idProd}
//	// requestparam: su dung de lay param, khong yeu thu tu
//
//	@PutMapping("/{id}")
//	public User updateUser(@PathVariable long id, @RequestBody User newUser) {
//		int foundIndex = -1;
//		for (int i = 0; i < users.size(); i++) {
//			if (users.get(i).getId().equals(id)) {
//				foundIndex = i;
//				break;
//			}
//		}
//
//		if (foundIndex == -1) {
//			return null;
//		} else {
//			User foundUser = users.get(foundIndex);
//			foundUser.setName(newUser.getName());
//			foundUser.setAddress(newUser.getAddress());
//			users.set(foundIndex, foundUser);
//			return foundUser;
//		}
//	}
//
	// update user
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PutMapping("/{id}")
	public ResponseEntity<?> updateUserByMap(@PathVariable long id,
			@RequestBody(required = false) Map<String, Object> newUser) {
		try {
			if (ObjectUtils.isEmpty(newUser)) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			UserDTORequest userDTORequest = new UserDTORequest(newUser);

			if (ObjectUtils.isEmpty(userDTORequest.getName()) || ObjectUtils.isEmpty(newUser.get("address"))
					|| ObjectUtils.isEmpty(newUser.get("password"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			User foundUser = this.userService.findUserById(id);
			if (ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			foundUser.setAddress(userDTORequest.getAddress());
			foundUser.setName(userDTORequest.getName());
			foundUser.setPassword(this.passwordEncoder.encode(userDTORequest.getPassword()));
			this.userRepository.save(foundUser);
			return super.success(foundUser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("")
	public ResponseEntity<?> addUser(@RequestBody(required = true) Map<String, Object> newUser) {
		try {
			if (ObjectUtils.isEmpty(newUser)) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			if (ObjectUtils.isEmpty(newUser.get("name")) || ObjectUtils.isEmpty(newUser.get("address"))
					|| ObjectUtils.isEmpty(newUser.get("password"))
					|| ObjectUtils.isEmpty(newUser.get("displayName"))) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			User foundUser = this.userRepository.findByName(newUser.get("name").toString()).orElse(null);
			if (!ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.DATA_ALREADY_EXISTS.getCode(),
						ResponseCode.DATA_ALREADY_EXISTS.getMessage());
			}

//			User insertedUser = this.userService.addNewUser(newUser);
			User insertedUser = new User();
			insertedUser.setName(newUser.get("name").toString());
			insertedUser.setAddress(newUser.get("address").toString());
			insertedUser.setDisplayName(newUser.get("displayName").toString());
			insertedUser.setPassword(this.passwordEncoder.encode(newUser.get("password").toString()));
			insertedUser.setRoles(this.roleRepository.findByRoleName("USER"));
			Identification identification = new Identification();
			identification.setExpiredDate(new Date());
			insertedUser.setIdentification(identification);
			this.userRepository.save(insertedUser);
			if (!ObjectUtils.isEmpty(insertedUser)) {
				return super.success(new UserDTOResponse(insertedUser));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeUser(@PathVariable long id) {
		User foundUser = this.userService.findUserById(id);
		if (ObjectUtils.isEmpty(foundUser)) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		this.userService.removeUser(id);
		return new ResponseEntity<>(foundUser, HttpStatus.OK);
	}

	@GetMapping("/getUsersByName")
	public ResponseEntity<?> getUserByName(@RequestParam String name) {
		return new ResponseEntity<>(this.userService.findByName("%" + name + "%"), HttpStatus.OK);
	}
}
