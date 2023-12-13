package com.dtvn.springbootproject.dto.responseDtos.Campaign;

import com.dtvn.springbootproject.dto.responseDtos.Creative.CreativeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignAndCreativesDTO {
    private CampaignDTO campaignDTO;
    private CreativeDTO creativesDTO;
}
