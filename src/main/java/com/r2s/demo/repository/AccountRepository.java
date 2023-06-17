package com.r2s.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.r2s.demo.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
