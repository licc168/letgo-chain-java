package com.licc.letsgo.res.list;

import lombok.Data;

@Data
 public  class PetLetsDataList {
    String id;
    String petId;
    Integer rareDegree;
    Integer generation;
    String petName;
    String petUrl;
    String bgColor;
    String coolingInterval;
    Integer shelfStatus;
    Integer chainStatus;
    String amount;
    String lastBreedTime;
    Boolean newBreeded;
    Integer lockStatus;
    Boolean isCooling;
}
