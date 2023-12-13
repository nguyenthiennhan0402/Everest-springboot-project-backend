package com.dtvn.springbootproject.repositories;

import com.dtvn.springbootproject.dto.responseDtos.Account.AccountDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT a FROM Account a WHERE LOWER(a.lastname) LIKE LOWER(concat('%', :lastName, '%'))")
    Page<AccountDTO> findByName(String lastName, Pageable pageable);
    Page<AccountDTO> findByEmail(String email, Pageable  pageable);
    @Query("SELECT a FROM Account a WHERE a.deleteFlag = false")
    Page<Account> getAllAccount(Pageable pageable);
    @Query("SELECT a FROM Account a WHERE LOWER(a.lastname) " +
            "LIKE LOWER(concat('%', :lastName, '%')) or LOWER(a.email) LIKE LOWER(concat('%', :lastName, '%'))")
    Page<Account> findAccountByEmailOrName(String lastName,Pageable  pageable);
}
