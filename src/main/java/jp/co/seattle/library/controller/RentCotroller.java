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

import jp.co.seattle.library.dto.RentBookInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

/**
 * 貸出しコントローラー
 */
@Controller
public class RentCotroller {
	final static Logger logger = LoggerFactory.getLogger(RentCotroller.class);

	@Autowired
	private RentService rentService;

	@Autowired
	private BooksService booksService;

	/**
	 * 貸出し登録
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		// デバッグ用ログ
		logger.info("Welcome rentControler.java! The client locale is {}.", locale);

		// 書籍IDに紐ずく書籍が貸出しされているかどうか
		RentBookInfo selectedRentInfo = rentService.getRentBookInfo(bookId);

		if (selectedRentInfo == null) {
			rentService.rentBook(bookId);

			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

			return "details";
		} else {
			model.addAttribute("errorMessage", "貸出し済みです。");

			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

			return "details";
		}
	}
}
