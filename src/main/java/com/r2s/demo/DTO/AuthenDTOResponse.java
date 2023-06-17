package com.r2s.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenDTOResponse {
	private String token;
	private String message;
}
