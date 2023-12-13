package com.dtvn.springbootproject.dto.responseDtos.Creative;//package com.dtvn.springbootproject.dto.responseDtos.Creative;


import com.dtvn.springbootproject.entities.Campaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeDTO {
    private String title;
    private String description;
    private String imageUrl;
    private String finalUrl;
}
