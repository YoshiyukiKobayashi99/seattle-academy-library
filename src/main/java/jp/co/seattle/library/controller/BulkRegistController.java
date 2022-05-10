package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class BulkRegistController {
	final static Logger logger = LoggerFactory.getLogger(BulkRegistController.class);

	@Autowired
	private BooksService booksService;

	/**
	 * 一括登録画面に遷移する
	 * 
	 * @param model
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/bulkRegist", method = RequestMethod.GET) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String bulkRegist(Model model) {
		return "bulk";
	}

	/**
	 * 一括登録する
	 * 
	 * @param model
	 * @param locale ロケール情報
	 * @param file   CSVファイル
	 * @return 遷移先画面名
	 */
	@Transactional
	@RequestMapping(value = "/bulkRegistBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	// RequestParamでname属性を取得
	public String bulkRegistBook(Locale locale, @RequestParam("upload_file") MultipartFile file, Model model) {

		logger.info("Welcome bulkRegistBook.java! The client locale is {}.", locale);

		// エラーレコードと本の配列
		List<Integer> failedRecord = new ArrayList<>();
		List<BookDetailsInfo> bookList = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
			String line;

			// エラー行数を把握するカウント
			int count = 0;

			while ((line = br.readLine()) != null) {

				// インクリメントで行数把握
				count++;

				String[] split = line.split(",", -1);

				boolean mustItems = split[0].isEmpty() || split[1].isEmpty() || split[2].isEmpty()
						|| split[3].isEmpty();

				boolean dateCheck = !(split[3].matches("^[0-9]{8}$"));

				boolean isbnCheck = !split[4].isEmpty() && !split[4].matches("^[0-9]{10}$")
						&& !split[4].matches("^[0-9]{13}$");

				if (mustItems || dateCheck || isbnCheck) {
					// エラーが起きた行数をエラーレコードに格納
					failedRecord.add(count);
				} else {
					// エラーがない場合はBookInfoをbookListに格納
					BookDetailsInfo bookInfo = new BookDetailsInfo();
					bookInfo.setTitle(split[0]);
					bookInfo.setAuthor(split[1]);
					bookInfo.setPublisher(split[2]);
					bookInfo.setPublishDate(split[3]);
					bookInfo.setIsbn(split[4]);
					bookList.add(bookInfo);
				}

			}

		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません", e);

		}

		if (bookList == null || bookList.size() == 0) {

			model.addAttribute("errorText", "CSVに書籍情報がありません。");

			return "bulk";
		}

		if (failedRecord == null || failedRecord.size() == 0) {

			// 配列丸々を渡しbulkRegistBookでループさせて登録
			booksService.bulkRegistBook(bookList);

			return "redirect:/home";

		} else {
			model.addAttribute("failedRecord", failedRecord);

			return "bulk";
		}

	}

}
