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
 * 返却コントローラー
 */
@Controller
public class ReturnController {
	final static Logger logger = LoggerFactory.getLogger(ReturnController.class);

	@Autowired
	private RentService rentService;

	@Autowired
	private BooksService booksService;

	/**
	 * 書籍返却処理
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/returnBook", method = RequestMethod.POST)
	public String rentBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		// デバッグ用ログ
		logger.info("Welcome returnControler.java! The client locale is {}.", locale);

		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		// 書籍IDに紐ずく書籍が貸出しされているかどうか
		RentBookInfo selectedRentInfo = rentService.getRentBookInfo(bookId);

		if (selectedRentInfo != null) {
			rentService.returnBook(bookId);

		} else {
			model.addAttribute("errorMessage", "貸出しされていません。");
		}
		// 更新後書籍テーブルと貸出テーブルを結合しデータがあるかどうか
		String status = booksService.bookStatus(bookId);

		if (status == null) {
			model.addAttribute("bookStatus", "貸し出し可");

		} else {
			model.addAttribute("bookStatus", "貸し出し中");

		}
		return "details";
	}
}
