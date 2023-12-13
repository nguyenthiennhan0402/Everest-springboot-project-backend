package com.dtvn.springbootproject.controllers;

import com.dtvn.springbootproject.config.JwtService;
import com.dtvn.springbootproject.constants.AuthConstants;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountDTO;
import com.dtvn.springbootproject.dto.responseDtos.Account.AccountResponseDTO;
import com.dtvn.springbootproject.dto.requestDtos.Account.AccountRegisterRequestDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Role;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.exceptions.ResponseMessage;
import com.dtvn.springbootproject.repositories.AccountRepository;
import com.dtvn.springbootproject.repositories.RoleRepository;
import com.dtvn.springbootproject.services.AccountService;
import com.dtvn.springbootproject.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.dtvn.springbootproject.constants.AppConstants.*;
import static com.dtvn.springbootproject.constants.ErrorConstants.*;
import static com.dtvn.springbootproject.constants.HttpConstants.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {


    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    @PostMapping("/registerAccount")
    public ResponseEntity<ResponseMessage<AccountResponseDTO>> registerAnAccount(
            @RequestBody AccountRegisterRequestDTO request
    ) {
        try {
            if (accountRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(ERROR_EMAIL_ALREADY_EXISTS, HTTP_BAD_REQUEST));
            }
            AccountResponseDTO addedAccount = accountService.registerAnAccount(request);
            if (addedAccount != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(ACCOUNT_REGISTER_SUCCESS, HTTP_OK, addedAccount));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(ACCOUNT_REGISTER_FAILED, HTTP_INTERNAL_SERVER_ERROR));
            }
        } catch (ErrorException e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(e.getMessage(), HTTP_BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ERROR_UNKNOWN, HTTP_INTERNAL_SERVER_ERROR));
        }
    }


    @GetMapping("/getAllAccount")
    public ResponseEntity<ResponseMessage<Page<AccountDTO>>> getAccounts(@RequestParam(value = "emailOrName", required = false) String emailOrName,
                                                                         @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) String strPageNo,
                                                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) String strPageSize) {
        try {
            if (!accountService.isInteger(strPageNo))
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.PAGENO_INVALID, HTTP_BAD_REQUEST));
            else if (!accountService.isInteger(strPageSize)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(AppConstants.PAGESIZE_INVALID, HTTP_BAD_REQUEST));
            }
            int pageNo = Integer.parseInt(strPageNo);
            int pageSize = Integer.parseInt(strPageSize);
            Pageable pageable = PageRequest.of(pageNo, pageSize);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<Page<AccountDTO>>(AppConstants.ACCOUNT_GET_ALL_SUCCESS, HTTP_OK,
                            accountService.getAccountByEmailOrName(emailOrName, pageable)));
        } catch (ErrorException e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(e.getMessage(), HTTP_BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ERROR_UNKNOWN, HTTP_INTERNAL_SERVER_ERROR));
        }

    }

    @PatchMapping("/deleteAccount")
    public ResponseEntity<ResponseMessage<AccountDTO>> deleteAccount(
            @RequestParam(value = "id", required = true) String AccountId,
            @RequestHeader("Authorization") String bearerToken) {

        //Delete "bearer" in token
        bearerToken = bearerToken.replace(AuthConstants.BEARER_PREFIX, "");
        final String currentUserEmail = jwtService.extractUsername(bearerToken);

        try {
            Integer id = Integer.parseInt(AccountId);
            Optional<Account> accountDelete = accountRepository.findById(id);
            //Check if account has been deleted
            if (accountDelete.get().isDeleteFlag())
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(ACCOUNT_IS_DELETED, HTTP_OK));

            //If the account is deleted, it is the current account
            if (accountDelete.get().getEmail().equals(currentUserEmail)) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage<>(ACCOUNT_DELETE_FAILD, HTTP_BAD_REQUEST));
            }

            //delete account
            accountService.deleteAccount(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_DELETE_SUCCESS, HTTP_OK));


        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(AppConstants.ACCOUNT_ID_INVALID, HTTP_BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ERROR_UNKNOWN, HTTP_INTERNAL_SERVER_ERROR));
        }
    }

    @PutMapping("/update")

    public ResponseEntity<ResponseMessage<AccountDTO>> updateAccount(@RequestParam(value = "id", required = true) Integer accountId,
                                                                     @RequestBody AccountDTO updatedAccount) {
        try {
            AccountDTO accountUpdated = accountService.updatedAccount(accountId, updatedAccount);
            if (accountUpdated != null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(AppConstants.ACCOUNT_UPDATE_SUCCESS, HTTP_OK, accountUpdated));
            } else {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(AppConstants.ACCOUNT_NOT_FOUND, HTTP_NOT_FOUND));
            }
        } catch (ErrorException e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(e.getMessage(), HTTP_BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ERROR_UNKNOWN, HTTP_BAD_REQUEST));
        }

    }

    @GetMapping("/getRoles")
    public ResponseEntity<ResponseMessage<List<Role>>> getAllRole() {
        try {
            List<Role> listRole;
            try {
                listRole = roleRepository.findAll();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseMessage(AppConstants.ROLES_GET_ALL_FAILED, HTTP_NOT_FOUND));
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage(AppConstants.ROLES_GET_ALL_SUCCESS, HTTP_OK, listRole));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseMessage<>(ERROR_UNKNOWN, HTTP_BAD_REQUEST));
        }

    }
}
