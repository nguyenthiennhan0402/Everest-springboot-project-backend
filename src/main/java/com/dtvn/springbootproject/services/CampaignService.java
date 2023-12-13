package com.dtvn.springbootproject.services;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaginAndImgDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public interface CampaignService {
    public Page<CampaginAndImgDTO> getCampaign(String name, Timestamp startDate, Timestamp endDate, Pageable pageable);
    public void deleteCampaign(int campaignId);
    public CampaignAndCreativesDTO updateCampagin(Integer campaginId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file);
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, Account account);
    public boolean isInteger(String number);
    public Campaign maptoEntity(CampaignDTO campaignDTO);
    public List<String> listBannerUrl();
}
