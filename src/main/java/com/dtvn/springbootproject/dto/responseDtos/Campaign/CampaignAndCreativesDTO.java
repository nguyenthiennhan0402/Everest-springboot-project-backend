package com.dtvn.springbootproject.dto.responsedtos.Campaign;

import com.dtvn.springbootproject.dto.responsedtos.Creative.CreativeDTO;
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
