package com.tsfn.loaders.bean;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "marketing")
public class MarketingData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long accountId;
    private String timestamp;
    private String postID;
    private String contentType;
    private double impressions;
    private int views;
    private int clicks;
    private double ctr;
    private int likes;
    private int comments;
    private int shares;
    private double engagementRate;
    private String fileUploaded;
    @Column(length = 1000)
    private String description;
}
