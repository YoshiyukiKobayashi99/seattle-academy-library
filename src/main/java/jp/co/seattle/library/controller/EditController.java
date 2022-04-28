package jp.co.seattle.library.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class EditController {
	final static Logger logger = LoggerFactory.getLogger(EditController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	/**
	 * 詳細画面に遷移する
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return
	 */
	@Transactional
	@RequestMapping(value = "/editBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	public String editBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {

		logger.info("Welcome editControler.java! The client locale is {}.", locale);

		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		return "edit";
	}

	/**
	 * 書籍情報を登録する
	 * 
	 * @param locale       ロケール情報
	 * @param title        書籍名
	 * @param author       著者名
	 * @param publisher    出版社
	 * @param file         サムネイルファイル
	 * @param model        モデル
	 * @param publish_date 出版日
	 * @param isbn         ISBN
	 * @param description  説明文
	 * @return 遷移先画面
	 */
	@Transactional
	@RequestMapping(value = "/editUpdateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String editUpdateBookBook(Locale locale, @RequestParam("title") String title,
			@RequestParam("author") String author, @RequestParam("publisher") String publisher,
			@RequestParam("thumbnail") MultipartFile file, @RequestParam("publish_date") String publishDate,
			@RequestParam("isbn") String isbn, @RequestParam("description") String description,
			@RequestParam("bookId") Integer bookId,

			Model model) {
		logger.info("Welcome editUpdateBooks.java! The client locale is {}.", locale);

		// パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setDescription(description);
		bookInfo.setIsbn(isbn);
		bookInfo.setPublishDate(publishDate);

		List<String> errorMessage = new ArrayList<>();

		// クライアントのファイルシステムにある元のファイル名を設定する
		String thumbnail = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				// サムネイル画像をアップロード
				String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
				// URLを取得
				String thumbnailUrl = thumbnailService.getURL(fileName);

				bookInfo.setThumbnailName(fileName);
				bookInfo.setThumbnailUrl(thumbnailUrl);

			} catch (Exception e) {

				// 異常終了時の処理
				logger.error("サムネイルアップロードでエラー発生", e);
				model.addAttribute("bookDetailsInfo", bookInfo);
				return "edit";
			}
		}

		if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty()) {
			errorMessage.add("必須項目を入力してください。<p><br>");
		}

		if (!(publishDate.matches("^[0-9]{8}$"))) {
			errorMessage.add("出版日は半角数字はYYYYMMDD形式で入力してください。<p><br>");
		}

		if (!isbn.isEmpty() && (!isbn.matches("^[0-9]{10}$")) && !(isbn.matches("^[0-9]{13}$"))) {
			errorMessage.add("ISBNは10桁または13桁の半角数字が正しくありません。");
		}

		if (errorMessage == null || errorMessage.size() == 0) {
			booksService.updateBook(bookInfo);

			model.addAttribute("resultMessage", "更新完了");

			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

			// TODO 登録した書籍の詳細情報を表示するように実装
			// 詳細画面に遷移する
			return "details";

		} else {
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("bookDetailsInfo", bookInfo);
			return "edit";
		}

	}

}