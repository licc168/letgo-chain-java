package com.licc.letsgo.rule;


import com.licc.letsgo.model.Rule;
import com.licc.letsgo.model.User;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.SpringContext;
import org.drools.core.marshalling.impl.ProtobufMessages;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;


public class BuyRule {
  static final Logger logger = LoggerFactory.getLogger(BuyRule.class);
  @Async
  public static void executeRuleRareDegree(Rule rule,User user,LetsgoService letsgoService) {
    executeRule("rule-raredegreeKS",rule,user,letsgoService);
  }
  @Async
  public static void executeRuleLiangHao(Rule rule,User user,LetsgoService letsgoService) {
    executeRule("rule-lianghaoKS",rule,user,letsgoService);
  }
  @Async
  public static void executeRuleBirth(Rule rule,User user,LetsgoService letsgoService) {
    executeRule("rule-birthKS",rule,user,letsgoService);
  }

  public static void executeRuleAttr(Rule rule,User user,LetsgoService letsgoService) {
    executeRule("rule-attrKS",rule,user,letsgoService);
  }


  public  static void executeRule(String ruleKS,Rule rule,User user,LetsgoService letsgoService){
    KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
    KieSession ksession = kc.newKieSession(ruleKS);
    ksession.setGlobal("letsgoService",letsgoService);
    ksession.setGlobal("user",user);
    ksession.setGlobal("logger",logger);
    FactHandle handle =ksession.insert(rule);
    ksession.fireAllRules();
    ksession.delete(handle);

    ksession.dispose();
  }
}


