package com.r2s.demo.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.r2s.demo.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Set<Role> findByRoleName(String roleName);
}
