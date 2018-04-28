package com.licc.letsgo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.licc.letsgo.res.petdata.PetsOnSale;
import java.io.IOException;

/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/3/27 15:17
 * @see
 */
public class JacksonUtil {
  public static ObjectMapper objectMapper;

  /**
   * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
   * (1)转换为普通JavaBean：readValue(json,Student.class)
   * (2)转换为List,如List<Student>,将第二个参数传递为Student
   * [].class.然后使用Arrays.asList();方法把得到的数组转换为特定类型的List
   *
   * @param jsonStr
   * @param valueType
   * @return
   */
  public static <T> T readValue(String jsonStr, Class<T> valueType) {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }

    try {
      return objectMapper.readValue(jsonStr, valueType);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }



  /**
   * 把JavaBean转换为json字符串
   *
   * @param object
   * @return
   */
  public static String toJSon(Object object) {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper();
    }

    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static void main(String arge[]) throws IOException {
  String s ="{\"id\": \"4666579\", \"petId\": \"1881206395891183128\", \"birthType\": 0, \"mutation\": 0, \"generation\": 0, \"rareDegree\": 1, \"desc\": \"\\u5c0f\\u83b1\", \"petType\": 0, \"amount\": \"8888.00\", \"bgColor\": \"#E1BEE7\", \"petUrl\": \"https://blockchain-pet-online.cdn.bcebos.com/PET_SVG_6431d316b3a59fbbf5d36b97cf3a54b7\", \"validCode\": \"2f620b8f720f6d90ade7f57c36e46ae4\", \"incubateTime\": null, \"coolingInterval\": \"0\\u5206\\u949f\", \"isCooling\": false, \"createtime\": \"2018-03-30 15:56:11\"}";
    PetsOnSale petsOnSale = new ObjectMapper().readValue(s, PetsOnSale.class);
    System.out.println(petsOnSale);
  }
}
