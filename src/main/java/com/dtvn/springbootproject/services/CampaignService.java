package com.dtvn.springbootproject.services;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.BannerDTO;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.CampaignAndImgDTO;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public interface CampaignService {
    public Page<CampaignAndImgDTO> getCampaign(String name, Timestamp startDate, Timestamp endDate, Pageable pageable);
    public void deleteCampaign(int campaignId);
    public CampaignAndCreativesDTO updateCampaign(Integer campaignId, CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file);
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, Account account);
    public boolean isInteger(String number);
    public List<BannerDTO> listBannerUrl();
    public void minusBudget(Integer campaignId);
}
