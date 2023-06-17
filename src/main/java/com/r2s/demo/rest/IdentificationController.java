package com.r2s.demo.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.r2s.demo.DTO.IdentificationDTOResponse;
import com.r2s.demo.constants.ResponseCode;
import com.r2s.demo.model.Identification;
import com.r2s.demo.repository.IdentificationRepository;

@RestController
@RequestMapping(path = "/identitication")
public class IdentificationController extends BaseRestController {
	@Autowired
	private IdentificationRepository identificationRepository;

	@GetMapping
	public ResponseEntity<?> getAllIdentifications() {
		try {
			List<Identification> identifications = this.identificationRepository.findAll();
			List<IdentificationDTOResponse> responses = identifications.stream().map(IdentificationDTOResponse::new)
					.toList();
			return super.success(responses);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateIdentification(@PathVariable long id) {
		try {
			Identification foundIdentification = this.identificationRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundIdentification)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			foundIdentification.setExpiredDate(new Date());
			this.identificationRepository.save(foundIdentification);
			return super.success(foundIdentification);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeIdentification(@PathVariable long id) {
		try {
			Identification foundIdentification = this.identificationRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundIdentification)) {
				return super.error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			this.identificationRepository.deleteById(id);
			return super.success(foundIdentification);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
