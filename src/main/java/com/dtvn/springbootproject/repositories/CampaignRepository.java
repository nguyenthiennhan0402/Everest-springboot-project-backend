package com.dtvn.springbootproject.repositories;
import com.dtvn.springbootproject.entities.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false " +
        "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND ((:startdate IS NULL AND :enddate IS NULL) OR " +
        "(:startdate IS NOT NULL AND :enddate IS NULL AND c.startDate >= :startdate) OR " +
        "(:startdate IS NULL AND :enddate IS NOT NULL AND c.startDate <= :enddate) OR " +
        "(c.startDate BETWEEN :startdate AND :enddate)) " +
        "ORDER BY c.status DESC")
        Page<Campaign> getCampaign(
                @Param("name") String name,
                @Param("startdate") Timestamp startdate,
                @Param("enddate") Timestamp enddate,
                Pageable pageable);
        @Query("SELECT c FROM Campaign c WHERE c.deleteFlag = false AND c.campaignId = :id")
        Optional<Campaign> findByIdAndDeleteFlagIsFalse(@Param("id") Integer id);

        boolean existsByNameAndDeleteFlagIsFalse(String name);

        @Query("SELECT c FROM Campaign c " +
                "WHERE c.status = true AND c.deleteFlag = false AND c.bidAmount != 0 AND c.bidAmount <= (c.budget - c.usedAmount) " +
                "GROUP BY c.campaignId, c.status, c.bidAmount, c.deleteFlag " +
                "ORDER BY c.bidAmount DESC")
        List<Campaign> findTop5Campaigns(Pageable pageable);
}
