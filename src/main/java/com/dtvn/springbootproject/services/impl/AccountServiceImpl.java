package com.dtvn.springbootproject.services.impl;
import com.dtvn.springbootproject.dto.responsedtos.Account.AccountDTO;
import com.dtvn.springbootproject.entities.Role;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.RoleRepository;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.dto.responsedtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.utils.validators.AccountValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AccountValidator accountValidator;
    private final ModelMapper mapper = new ModelMapper();
    @Override
    public AccountResponseDTO registerAnAccount(AccountRegisterRequestDTO request) {
        accountValidator.validateRegisterRequest(request);
        //validate role
        if (request.getRole() == null) {
            throw new ErrorException(ERROR_ROLE_REQUIRED,HTTP_BAD_REQUEST);
        }
        Role role = roleRepository.findByRoleName(request.getRole())
                .orElseThrow(() -> new ErrorException(ERROR_ROLE_NOT_FOUND, HTTP_NOT_FOUND));
        Account createdBy = getAuthenticatedAccount();
        Account updatedBy = getAuthenticatedAccount();
        var account = Account.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .address(request.getAddress())
                .phone(request.getPhone())
                .createdBy(createdBy)
                .updatedBy(updatedBy)
                .build();
        try {
            accountRepository.save(account);
        }
        catch (Exception e) {
            throw new ErrorException(ERROR_SAVE_ACCOUNT, HTTP_INTERNAL_SERVER_ERROR);
        }

        return AccountResponseDTO.builder()
                .accountId(account.getAccountId())
                .firstname(account.getFirstname())
                .lastname(account.getLastname())
                .email(account.getEmail())
                .role(account.getRole().getRoleName())
                .address(account.getAddress())
                .phone(account.getPhone())
                .build();
    }

    //get who has just created an account
    private Account getAuthenticatedAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ErrorException(ERROR_CANNOT_RETRIEVE_AUTHENTICATED_USER,HTTP_INTERNAL_SERVER_ERROR);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return (Account) principal;
        }
        throw new ErrorException(USER_NOT_USER_DETAILS,HTTP_FORBIDDEN);
    }
    @Override
    public Page<AccountDTO> getAccountByEmailOrName(String emailOrName, Pageable pageable) {

        if(emailOrName == null || emailOrName.isEmpty()){
            Page<Account> allAccount = accountRepository.getAllAccount(pageable);
            return allAccount.map(account -> mapper.map(account, AccountDTO.class));
        } else {
            Page<Account> listAccount = accountRepository.findAccountByEmailOrName(emailOrName,pageable);
            return listAccount.map(account -> mapper.map(account,AccountDTO.class));
        }
    }
    @Override
    @Transactional
    public void deleteAccount(Integer id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(AppConstants.ACCOUNT_NOT_FOUND));
        existingAccount.setDeleteFlag(true);
        existingAccount.setUpdatedBy(getAuthenticatedAccount());
        accountRepository.save(existingAccount);
    }
    @Override
    public AccountDTO updatedAccount(Integer id, AccountDTO updatedAccount) {
        Optional<Account> optionalOldAccount = accountRepository.findById(id);
        if(optionalOldAccount.isPresent()){
            Account oldAccount  =  optionalOldAccount.get();
            oldAccount.setEmail(updatedAccount.getEmail());
            oldAccount.setFirstname(updatedAccount.getFirstname());
            oldAccount.setLastname(updatedAccount.getLastname());
            oldAccount.setAddress(updatedAccount.getAddress());
            oldAccount.setPhone(updatedAccount.getPhone());
            oldAccount.setUpdatedBy(getAuthenticatedAccount());
            //find role id by role name
            Optional<Role> roleUpdate = roleRepository.findByRoleName(updatedAccount.getRole());
            if(roleUpdate.isPresent()){
                oldAccount.setRole(roleUpdate.get());
            }else{
                throw new ErrorException(ERROR_ROLE_NOT_FOUND, HTTP_NOT_FOUND);
            }
            return mapper.map(accountRepository.save(oldAccount),AccountDTO.class);
        }else {
            throw new ErrorException(AppConstants.ACCOUNT_NOT_FOUND, HTTP_NOT_FOUND);
        }
    }

    @Override
    public boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
