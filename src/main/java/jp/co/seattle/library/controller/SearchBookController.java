package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class SearchBookController {
	final static Logger logger = LoggerFactory.getLogger(SearchBookController.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 検索キーワードに一致した本を表示する
	 * 
	 * @param locale ロケール情報
	 * @param model  モデル
	 * @param search 検索キーワード
	 * @return 画面遷移先
	 */
	@Transactional
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String transitionHome(Locale locale, @RequestParam("search") String searchWord,
			@RequestParam("searchtype") String searchType, Model model) {

		logger.info("Welcome search.java! The client locale is {}.", locale);

		// 一部一致かどうか
		if (searchType.equals("match")) {

			model.addAttribute("bookList", booksService.getBookList(searchWord));

		} else {

			model.addAttribute("bookList", booksService.getFullMatchBookList(searchWord));
		}
		return "home";
	}
}
