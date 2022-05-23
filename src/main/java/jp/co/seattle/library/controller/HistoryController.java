package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class HistoryController {
    final static Logger logger = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private RentService rentService;

    /**
     * Homeボタンからホーム画面に戻るページ
     * @param locale
     * @param model
     * @return 画面遷移先
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public String history(Locale locale, Model model) {
    	
    	logger.info("Welcome historyControler.java! The client locale is {}.", locale);
    	
    	model.addAttribute("rentBookList", rentService.getRentBookList());
    	
        return "history";
    }
}
