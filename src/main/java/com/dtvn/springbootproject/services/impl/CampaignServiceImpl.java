package com.dtvn.springbootproject.services.impl;
import com.dtvn.springbootproject.constants.AppConstants;
import com.dtvn.springbootproject.constants.HttpConstants;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaginAndImgDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignAndCreativesDTO;
import com.dtvn.springbootproject.dto.responseDtos.Campaign.CampaignDTO;
import com.dtvn.springbootproject.dto.responseDtos.Creative.CreativeDTO;
import com.dtvn.springbootproject.entities.Account;
import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.entities.Creatives;
import com.dtvn.springbootproject.exceptions.ErrorException;
import com.dtvn.springbootproject.repositories.AccountRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@EnableScheduling
public class CampaignServiceImpl implements CampaignService {
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private FirebaseService  firebaseService;
    private final ModelMapper mapper = new ModelMapper();
//    private List<String> listTopUrl = new ArrayList<>();

    @Override
    public Page<CampaginAndImgDTO> getCampaign(String name, Timestamp startDate, Timestamp endDate, Pageable pageable) {
        Page<Campaign> listCampaign = campaignRepository.getCampaign(name,startDate,endDate, pageable);
        List<CampaginAndImgDTO> listCampaignAndCreativesDTO = new ArrayList<>();
        listCampaign.forEach(campaign -> {
                    CampaginAndImgDTO campaginAndImgDTO = new CampaginAndImgDTO();
                    Optional<Creatives>  creatives =  creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(Optional.ofNullable(campaign));
                    campaginAndImgDTO = mapper.map(campaign, CampaginAndImgDTO.class);
                    campaginAndImgDTO.setImgUrl(creatives.get().getImageUrl());
                    campaginAndImgDTO.setTitle(creatives.get().getTitle());
                    campaginAndImgDTO.setDescription(creatives.get().getDescription());
                    campaginAndImgDTO.setFinalUrl(creatives.get().getFinalUrl());
                    listCampaignAndCreativesDTO.add(campaginAndImgDTO);
                }
        );
        // Tạo một đối tượng Pageable mới với tổng số phần tử tính toán
        Pageable newPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), listCampaign.getSort());

        // Tạo đối tượng PageImpl với danh sách và Pageable mới
        Page<CampaginAndImgDTO> page = new PageImpl<>(listCampaignAndCreativesDTO, newPageable, listCampaign.getTotalElements());
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
    public CampaignAndCreativesDTO updateCampagin(Integer campaginId,CampaignAndCreativesDTO campaignAndCreativesDTO, MultipartFile file ) {
        CampaignDTO campaignDTO = campaignAndCreativesDTO.getCampaignDTO();
        CreativeDTO creativeDTO = campaignAndCreativesDTO.getCreativesDTO();
        try{
            Optional<Campaign> oldCampaign = campaignRepository.findByIdAndDeleteFlagIsFalse(campaginId);
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
                String imgurl = firebaseService.uploadFile(file);
                oldCreate.get().setImageUrl(imgurl);
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(imgurl);
            } else {
                campaignAndCreativesDTO.getCreativesDTO().setImageUrl(oldCreate.get().getImageUrl());
            }
            oldCreate.get().setFinalUrl(creativeDTO.getFinalUrl());
            campaignRepository.save(oldCampaign.get());
            creativeRepository.save(oldCreate.get());
            return campaignAndCreativesDTO;
        } else throw new ErrorException(AppConstants.CAMPAGIGN_UPDATE_FAILED, HttpConstants.HTTP_FORBIDDEN);

        } catch (Exception e){
                throw new ErrorException(AppConstants.CAMPAGIGN_UPDATE_FAILED, HttpConstants.HTTP_FORBIDDEN);
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
    public List<String> listBannerUrl() {
        List<Campaign> campaigns = null;
        try {
            campaigns = campaignRepository.findTop5Campaigns(PageRequest.of(0, 5));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        List<String> imgUrl = new ArrayList<>();
        for(int i = 0; i < campaigns.size(); i++){
           Optional<Creatives>  creatives =  creativeRepository.findByCampaignIdAndDeleteFlagIsFalse(Optional.ofNullable(campaigns.get(i)));
           if(creatives.isPresent()){
               imgUrl.add(creatives.get().getImageUrl());
               creatives.get().setIsDisplay(true);
               //tru tien
               int useedAmount = campaigns.get(i).getUsedAmount();
               campaigns.get(i).setUsedAmount((int) (useedAmount + campaigns.get(i).getBidAmount()));
               campaigns.get(i).setUsageRate((float) (campaigns.get(i).getUsedAmount() / campaigns.get(i).getBudget()));
               campaignRepository.save(campaigns.get(i));
           }
        }
        return imgUrl;
    }

//    @Scheduled(fixedRate = 25000)
//    public void scheduledMethod() {
//        setListTopUrl(listBannerUrl());
//    }
//    public List<String> getListTopUrl(){
//        return listTopUrl;
//    }
//    public void setListTopUrl(List<String> listTopUrl){
//        this.listTopUrl = listTopUrl;
//    }
    @Override
    public boolean isInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Campaign maptoEntity(CampaignDTO campaignDTO) {
        return null;
    }


}
