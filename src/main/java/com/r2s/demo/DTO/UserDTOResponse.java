package com.r2s.demo.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.r2s.demo.model.Account;
import com.r2s.demo.model.User;

import lombok.Data;

@Data
public class UserDTOResponse {
	private Long id;
	private String name;
	private String address;
	private Date expiredDate;
	private List<Map<String, Object>> accounts;

	public UserDTOResponse(User user) {
		this.id = user.getId();
		this.address = user.getAddress();
		this.name = user.getName();
		if (!ObjectUtils.isEmpty(user.getIdentification())) {
			this.expiredDate = user.getIdentification().getExpiredDate();
		}
		this.accounts = new ArrayList<>();
		for (Account account : user.getAccounts()) {
			this.accounts.add(Map.of("blance", account.getBalance()));
		}
	}
}
