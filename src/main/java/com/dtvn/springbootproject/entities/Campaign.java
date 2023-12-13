package com.dtvn.springbootproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "campaigns")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_id")
    private Integer campaignId;

    @Column(name = "campaign_name")
    private String name;

    @Column(name = "startdate")
    private Timestamp startDate;

    @Column(name = "enddate")
    private Timestamp endDate;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "bid_amount")
    private Long bidAmount;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account accountId;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "delete_flag", nullable = false)
    private boolean deleteFlag;

    @Column(name = "usage_rate", columnDefinition = "FLOAT DEFAULT 0.0")
    private Float usageRate;

    @Column(name = "used_amount", columnDefinition = "INT DEFAULT 0")
    private Integer usedAmount;

}
