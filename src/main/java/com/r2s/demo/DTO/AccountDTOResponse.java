package com.r2s.demo.DTO;

import org.springframework.util.ObjectUtils;

import com.r2s.demo.model.Account;
import com.r2s.demo.model.User;

import lombok.Data;

@Data
public class AccountDTOResponse {
	private Long userId;
	private String userName;
	private String userAddress;
	private Long id;
	private Double balance;

	public AccountDTOResponse(Account account) {
		this.id = account.getId();
		this.balance = account.getBalance();
		if (!ObjectUtils.isEmpty(account.getUser())) {
			User user = account.getUser();
			this.userId = user.getId();
			this.userName = user.getName();
			this.userAddress = user.getAddress();
		}
	}
}
