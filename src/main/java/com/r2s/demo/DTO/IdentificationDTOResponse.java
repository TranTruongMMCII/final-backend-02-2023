package com.r2s.demo.DTO;

import java.util.Date;

import org.springframework.util.ObjectUtils;

import com.r2s.demo.model.Identification;
import com.r2s.demo.model.User;

import lombok.Data;

@Data
public class IdentificationDTOResponse {
	private Long id;
	private Date expiredDate;
	private Long userId;
	private String userName;
	private String userAddress;

	public IdentificationDTOResponse(Identification identification) {
		this.id = identification.getId();
		this.expiredDate = identification.getExpiredDate();
		if (!ObjectUtils.isEmpty(identification.getUser())) {
			User user = identification.getUser();
			this.userId = user.getId();
			this.userName = user.getName();
			this.userAddress = user.getAddress();
		}
	}
}
