package com.dtvn.springbootproject.repositories;

import com.dtvn.springbootproject.entities.Campaign;
import com.dtvn.springbootproject.entities.Creatives;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreativeRepository extends JpaRepository<Creatives, Integer> {
//    @Query("SELECT c FROM Creatives c WHERE c.title = :title AND c.deleteFlag = false")
    boolean existsByTitleAndDeleteFlagIsFalse(String title);
    @Query("SELECT c FROM Creatives c WHERE c.title = :title AND c.deleteFlag = false")
    Optional<Creatives> findByName(@Param("title") String title);
    Optional<Creatives>  findByCampaignIdAndDeleteFlagIsFalse(Optional<Campaign> campaign);
}
