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
@Table(name = "creatives")
public class Creatives {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "creative_id")
    private Integer creativeId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "final_url")
    private String finalUrl;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaignId;

    @CreationTimestamp
    @Column(name = "create_at")
    private Timestamp createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Timestamp updateAt;

    @Column(name = "delete_flag", nullable = false, columnDefinition = "boolean default false")
    private Boolean deleteFlag;
    @Column(name = "is_display", columnDefinition = "BOOLEAN DEFAULT False")
    private Boolean isDisplay;

}
