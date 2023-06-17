package com.r2s.demo.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.r2s.demo.model.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
//	// cach 1, cac ban tach cau query
	@Transactional(readOnly = true)
	List<Education> findByDeletedTrue(Pageable pageable);

	List<Education> findByDeletedFalse(Pageable pageable);

	// cach 2, su dung field
	List<Education> findAllByDeleted(boolean isDeleted);
}
