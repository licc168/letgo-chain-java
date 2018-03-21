package com.licc.letsgo;

import com.licc.letsgo.model.User;
import java.util.ArrayList;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="licc")
@Data
public class LiccProps {
  private List<User> users = new ArrayList<>();
  private String petUrl;
}


