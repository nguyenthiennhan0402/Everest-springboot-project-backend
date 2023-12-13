package com.dtvn.springbootproject.services;

import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccountService {
    AccountResponseDTO registerAnAccount(AccountRegisterRequestDTO request);
    Page<AccountDTO> getAccountByEmailOrName(String emailOrName, Pageable pageable);
    void deleteAccount(Integer id);
    public AccountDTO updatedAccount(Integer id, AccountDTO account);
    boolean isInteger(String number);
}
