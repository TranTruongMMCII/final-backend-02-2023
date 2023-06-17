package com.r2s.demo.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.r2s.demo.DTO.EducationDTOResponse;
import com.r2s.demo.constants.ResponseCode;
import com.r2s.demo.model.Education;
import com.r2s.demo.model.Subject;
import com.r2s.demo.model.User;
import com.r2s.demo.repository.EducationRepository;
import com.r2s.demo.repository.SubjectRepository;
import com.r2s.demo.repository.UserRepository;

@RestController
@RequestMapping(path = "/educations")
public class EducationController extends BaseRestController {
	@Autowired
	private EducationRepository educationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@GetMapping
	public ResponseEntity<?> getAllEducations(@RequestParam(defaultValue = "-1") Integer status,
			@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "20") Integer limit,
			@RequestParam(defaultValue = "") String sortByUserId,
			@RequestParam(defaultValue = "") String sortBySemester) {
		try {
			// status = 0 => get not deleted
			// status = 1 => get deleted
			// status = -1 => get all
			if (!Arrays.asList(-1, 0, 1).contains(status)) {
				return error(ResponseCode.INVALID_VALUE.getCode(), ResponseCode.INVALID_VALUE.getMessage());
			}

			// Tao pageable
			Pageable pageable = PageRequest.of(offset, limit);
			List<Order> orders = new ArrayList<>();
			if (!ObjectUtils.isEmpty(sortByUserId)) {
				orders.add(new Order(convertDirection(sortByUserId), "userId"));
			}
			if (!ObjectUtils.isEmpty(sortBySemester)) {
				orders.add(new Order(convertDirection(sortBySemester), "semester"));
			}
			if (!orders.isEmpty()) {
				pageable = PageRequest.of(offset, limit, Sort.by(orders));
			}

			List<Education> educations;
			if (status == -1) {
				educations = this.educationRepository.findAll(pageable).stream().map(x -> x).toList();
			} else if (status == 0) {
//				educations = this.educationRepository.findAllByDeleted(false);
				educations = this.educationRepository.findByDeletedFalse(pageable);
			} else {
//				educations = this.educationRepository.findAllByDeleted(true);
				educations = this.educationRepository.findByDeletedTrue(pageable);
			}

			List<EducationDTOResponse> responses = educations.stream().map(EducationDTOResponse::new).toList();
			return success(responses);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	private static Direction convertDirection(String direction) {
		return direction.equalsIgnoreCase("ASC") ? Direction.ASC : Direction.DESC;
	}

	@PostMapping
	@Transactional(noRollbackFor = { Exception.class })
	public ResponseEntity<?> addEducation(@RequestBody Map<String, Object> education) {
		try {
			if (ObjectUtils.isEmpty(education) || ObjectUtils.isEmpty(education.get("userId"))
					|| ObjectUtils.isEmpty(education.get("subjectId"))) {
				return error(ResponseCode.NO_PARAM.getCode(), ResponseCode.NO_PARAM.getMessage());
			}

			Long userId = Long.parseLong(education.get("userId").toString());
			User foundUser = this.userRepository.findById(userId).orElse(null);
			if (ObjectUtils.isEmpty(foundUser)) {
				return error(ResponseCode.USER_NOT_FOUND.getCode(), ResponseCode.USER_NOT_FOUND.getMessage());
			}

			Long subjectId = Long.parseLong(education.get("subjectId").toString());
			Subject foundSubject = this.subjectRepository.findById(subjectId).orElse(null);
			if (ObjectUtils.isEmpty(foundSubject)) {
				return error(ResponseCode.SUBJECT_NOT_FOUND.getCode(), ResponseCode.SUBJECT_NOT_FOUND.getMessage());
			}

			Education newEducation = new Education();
			newEducation.setUser(foundUser);
			newEducation.setSubject(foundSubject);
			newEducation.setSemester(education.get("semester").toString());

			// 1. luu ben 1 cua user
//			foundUser.getEducations().add(newEducation);
//			this.userRepository.save(foundUser);
			// 2. luu ben 1 cua subject
			// 3. luu ben education
			this.educationRepository.save(newEducation);
			return success(education);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteEducation(@PathVariable long id) {
		try {
			Education foundEducation = this.educationRepository.findById(id).orElse(null);
			if (ObjectUtils.isEmpty(foundEducation)) {
				return error(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getMessage());
			}

			foundEducation.setDeleted(true);
			this.educationRepository.save(foundEducation);
			return success(foundEducation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return error(ResponseCode.NO_CONTENT.getCode(), ResponseCode.NO_CONTENT.getMessage());
	}
}
