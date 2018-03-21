package com.licc.letsgo.rule;


import com.licc.letsgo.model.Rule;
import com.licc.letsgo.model.User;
import com.licc.letsgo.res.petdata.PetsOnSale;
import com.licc.letsgo.services.LetsgoService;
import com.licc.letsgo.util.SpringContext;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;


public class BuyRule {

  public static void execute(Rule rule,User user) {
    KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
    KieSession ksession = kc.newKieSession("buyRuleKS");
    ksession.setGlobal("letsgoService", SpringContext.getBean(LetsgoService.class));
    ksession.setGlobal("user",user);
    ksession.insert(rule);
    ksession.fireAllRules();
    ksession.dispose();
  }

  public static void executeDetail(Rule rule,User user) {
    KieContainer kc = KieServices.Factory.get().getKieClasspathContainer();
    KieSession ksession = kc.newKieSession("buyDetailRuleKS");
    ksession.setGlobal("letsgoService", SpringContext.getBean(LetsgoService.class));
    ksession.setGlobal("user",user);
    ksession.insert(rule);
    ksession.fireAllRules();
    ksession.dispose();
  }
}
