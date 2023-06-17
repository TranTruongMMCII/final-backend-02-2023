package com.r2s.demo.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r2s.demo.DTO.AccountDTOResponse;
import com.r2s.demo.constants.ResponseCode;
import com.r2s.demo.model.Account;
import com.r2s.demo.model.User;
import com.r2s.demo.repository.AccountRepository;
import com.r2s.demo.repository.UserRepository;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController extends BaseRestController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@PostMapping("")
	public ResponseEntity<?> createAccount(@RequestBody Map<String, Object> account) {
		try {
			if (ObjectUtils.isEmpty(account) || ObjectUtils.isEmpty(account.get("userId"))
					|| ObjectUtils.isEmpty("balance")) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			long userId = Long.parseLong(account.get("userId").toString());
			User foundUser = this.userRepository.findById(userId).orElse(null);
			if (ObjectUtils.isEmpty(foundUser)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			// khoi tao obj account
			Account newAccount = new Account();
			newAccount.setBalance(Double.parseDouble(account.get("balance").toString()));
			newAccount.setUser(foundUser);

			// c1. luu ben 1
//			foundUser.getAccounts().add(newAccount);
//			this.userRepository.save(foundUser);

			// c2. luu ben nhieu
			this.accountRepository.save(newAccount);

			return super.success(newAccount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateAccount(@PathVariable long id, @RequestBody Map<String, Object> account) {
		try {
			if (ObjectUtils.isEmpty(account) || ObjectUtils.isEmpty("balance")) {
				return super.error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			Account foundAccount = this.accountRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundAccount)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			foundAccount.setBalance(Double.parseDouble(account.get("balance").toString()));
			this.accountRepository.save(foundAccount);
			return super.success(account);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeAccount(@PathVariable long id) {
		try {
			Account foundAccount = this.accountRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundAccount)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			this.accountRepository.deleteById(id);
			return super.success(foundAccount);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@GetMapping
	public ResponseEntity<?> getAccounts() {
		try {
			List<Account> accounts = this.accountRepository.findAll();
			return super.success(accounts.stream().map(AccountDTOResponse::new).toList());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
