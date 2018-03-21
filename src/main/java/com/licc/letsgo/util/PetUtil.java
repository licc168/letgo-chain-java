package com.licc.letsgo.util;


public class PetUtil {
   public static String transRareDegree(int code){
     switch (code){
       case 0:return  "普通";
       case 1:return  "稀有";
       case 2:return  "卓越";
       case 3:return  "史诗";
       case 4:return  "神话";
       case 5:return  "传说";
       default: return "";
     }
   }
}
