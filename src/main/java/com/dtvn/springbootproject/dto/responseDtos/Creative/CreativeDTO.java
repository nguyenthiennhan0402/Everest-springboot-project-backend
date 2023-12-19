package com.dtvn.springbootproject.dto.responsedtos.Creative;//package com.dtvn.springbootproject.dto.responseDtos.Creative;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreativeDTO {
    private String title;
    private String description;
    private String imageUrl;
    private String finalUrl;
}
