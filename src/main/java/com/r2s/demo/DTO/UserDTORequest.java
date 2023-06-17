package com.r2s.demo.DTO;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTORequest {
	private Long id;

	private String name;

	private String password;

	private String address;

	public UserDTORequest(Map<String, Object> user) {
//		this.id = Long.parseLong(user.get("id").toString());
		this.name = user.get("name").toString();
		this.password = user.get("password").toString();
		this.address = user.get("address").toString();
	}
}
