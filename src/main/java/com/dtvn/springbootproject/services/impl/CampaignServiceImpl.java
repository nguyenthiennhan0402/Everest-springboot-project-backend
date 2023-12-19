package com.dtvn.springbootproject.services.impl;
import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.BannerDTO;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.CampaignAndImgDTO;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.dto.responsedtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.dto.responsedtos.Creative.CreativeDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.entities.Creatives;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.CampaignRepository;
import com.dtvn.springbootproject.repositories.CreativeRepository;
import com.dtvn.springbootproject.services.CampaignService;
import com.dtvn.springbootproject.services.FirebaseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class CampaignServiceImpl implements CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private FirebaseService  firebaseService;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public Page<CampaignAndImgDTO> getCampaign(String name, Timestamp startDate, Timestamp endDate, Pageable pageable) {
        Page<Campaign> listCampaign = campaignRepository.getCampaign(name,startDate,endDate, pageable);
        List<CampaignAndImgDTO> listCampaignAndCreativesDTO = new ArrayList<>();
        listCampaign.forEach(campaign -> {
                    CampaignAndImgDTO campaignAndImgDTO = new CampaignAndImgDTO();
                    Optional<Creatives>  creatives =  creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(Optional.ofNullable(campaign));
                    campaignAndImgDTO = mapper.map(campaign, CampaignAndImgDTO.class);
                    campaignAndImgDTO.setImgUrl(creatives.get().getImageUrl());
                    campaignAndImgDTO.setTitle(creatives.get().getTitle());
                    campaignAndImgDTO.setDescription(creatives.get().getDescription());
                    campaignAndImgDTO.setFinalUrl(creatives.get().getFinalUrl());
                    listCampaignAndCreativesDTO.add(campaignAndImgDTO);
                }
        );
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), listCampaign.getSort());
        Page<CampaignAndImgDTO> page = new PageImpl<>(listCampaignAndCreativesDTO, newPageable, listCampaign.getTotalElements());
        return page;
    }
    @Override
    public void deleteCampaign(int campaignId) {
        Optional<Campaign> campaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
        Optional<Creatives> creatives = creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(campaign);
        if(creatives.isPresent())
            creatives.get().setDeleteFlag(true);
        else {
            throw new RuntimeException(AppConstants.CREATIVES_NOT_FOUND);
        }
        campaign.get().setDeleteFlag(true);
        campaignRepository.save(campaign.get());
    }

    @Override
    public CampaignAndCreativesDTO updateCampaign(Integer campaignId,CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file ) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        try{
            Optional<Campaign> oldCampaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaignId);
            Optional<Creatives> oldCreate = creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(oldCampaign)  ;
        if(oldCampaign.isPresent()){
            oldCampaign.get().setName(campaignDTO.getName());
            //update campaign
            oldCampaign.get().setStatus(campaignDTO.getStatus());
            oldCampaign.get().setBudget(campaignDTO.getBudget());
            oldCampaign.get().setBidAmount(campaignDTO.getBidAmount());
            oldCampaign.get().setStartDate(campaignDTO.getStartDate());
            oldCampaign.get().setEndDate(campaignDTO.getEndDate());

            //update creative
            oldCreate.get().setTitle(creativeDTO.getTitle());
            oldCreate.get().setDescription(creativeDTO.getDescription());
            //check if img is change
            if(!file.isEmpty()){
                String imgUrl = firebaseService.uploadFile(file);
                oldCreate.get().setImageUrl(imgUrl);
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgUrl);
            } else {
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(oldCreate.get().getImageUrl());
            }
            oldCreate.get().setFinalUrl(creativeDTO.getFinalUrl());
            campaignRepository.save(oldCampaign.get());
            creativeRepository.save(oldCreate.get());
            return campaignAndCreativesDTO;
        } else throw new ErrorException(AppConstants.CAMPAIGN_UPDATE_FAILED, HttpConstants.HTTP_FORBIDDEN);

        } catch (Exception e){
                throw new ErrorException(AppConstants.CAMPAIGN_UPDATE_FAILED, HttpConstants.HTTP_FORBIDDEN);
        }
    }
    @Override
    public CampaignAndCreativesDTO createCampaign(CampaignAndCreativesDTO campaignAndCreativesDTO, Account account) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        Campaign campaignCreated =  new Campaign();

        campaignDTO.setUsageRate((float)0.0);
        campaignDTO.setUsedAmount(0);
        if(campaignDTO.getBidAmount() == null){
            campaignDTO.setBidAmount(0L);
        }
        if(campaignDTO.getBudget() == null){
            campaignDTO.setBudget(0L);
        }
        if(campaignDTO.getStatus() == null){
            campaignDTO.setStatus(true);
        }
        campaignCreated = mapper.map(campaignDTO, Campaign.class);
        campaignCreated.setAccountId(account);
        campaignRepository.save(campaignCreated);
        Creatives creatives = new Creatives();
        creatives = mapper.map(creativeDTO, Creatives.class);
        System.out.println(campaignCreated);
        campaignRepository.save(campaignCreated);
        creatives.setCampaignId(campaignCreated);
        creatives.setDeleteFlag(false);
        Creatives creativesCreated = creativeRepository.save(creatives);
        return campaignAndCreativesDTO;
    }
    @Override
    public List<BannerDTO> listBannerUrl() {
        List<Campaign> campaigns = null;
        try {
            campaigns = campaignRepository.findTop5Campaigns(PageRequest.of(0, 5));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        List<BannerDTO> imgUrl = new ArrayList<>();
        for(int i = 0; i < campaigns.size(); i++){
           Optional<Creatives>  creatives =  creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(Optional.ofNullable(campaigns.get(i)));
           if(creatives.isPresent()){
               BannerDTO bannerDTO = new BannerDTO(creatives.get().getCreativeId(),creatives.get().getImageUrl());
               imgUrl.add(bannerDTO);
               campaignRepository.save(campaigns.get(i));
           }
        }
        return imgUrl;
    }
    @Override
    public void minusBudget(Integer campaignId) {
        Optional<Campaign> campaign = campaignRepository.findById(campaignId);
        if(campaign.isPresent()){
            int usedAmount = campaign.get().getUsedAmount();
            campaign.get().setUsedAmount((int)(usedAmount + campaign.get().getBidAmount()));
            campaign.get().setUsageRate(((float)((float) campaign.get().getUsedAmount() / (float) campaign.get().getBudget())) * 100);
            campaignRepository.save(campaign.get());
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
