package hska.iwi.eShopMaster.controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import hska.iwi.eShopMaster.config.TemplateFactory;

public class LogoutAction extends ActionSupport {

    /**
     *
     */
    private static final long serialVersionUID = -530488065543708898L;

    public String execute() throws Exception {

        // Clear session:
        ActionContext.getContext().getSession().clear();
        TemplateFactory.nullTheOAuth2RestTemplate();

        return "success";

    }
}
