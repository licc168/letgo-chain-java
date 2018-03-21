package com.licc.letsgo.res.petdetail;

import java.util.List;

import lombok.Data;

@Data
public class PetDetailData {
    private String              id;
    private String              name;
    private String              petId;
    private Integer             generation;
    private String              rareDegree;
    private List<PetDetailAttr> attributes;
    private String              desc;
    private String              amount;
    private String              selfStatus;
    private String              faterId;
    private String              motherId;
    private boolean             isOnChain;
    private String              bgColor;
    private String              headIcon;
    private boolean             onChain;

}
