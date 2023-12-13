package com.dtvn.springbootproject.dto.responseDtos.Account;

import com.dtvn.springbootproject.entities.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountGetResponseDTO {
    private List<Account> content;
    private int pageNo;
    private int pageSize;
    private Long totalElements;

    private int totalPages;
    private boolean last;
}
