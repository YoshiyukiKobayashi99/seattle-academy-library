package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.RentBookInfo;
import jp.co.seattle.library.rowMapper.RentBookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * rentテーブルに関する処理を実装する
 */
@Service
public class RentService {
	final static Logger logger = LoggerFactory.getLogger(RentService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 貸出する書籍が新規の場合登録し、すでにある場合は更新する
	 *
	 * @param bookId 書籍ID
	 */
	public void rentBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "INSERT INTO rent (book_id, rent_date, return_date) VALUES (" + bookId+ ", now(), null) ON CONFLICT (book_id) DO UPDATE SET rent_date  = now(), return_date  = null";
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍IDに紐づく書籍が貸出しされているかどうか確認
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public String getRentBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT rent_date FROM rent where book_id =" + bookId;

		try {
			String selectedRentInfo = jdbcTemplate.queryForObject(sql, String.class);
			return selectedRentInfo;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 書籍IDに紐づく貸出している書籍を返却する
	 *
	 * @param bookId 書籍ID
	 */
	public void returnBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "UPDATE rent SET rent_date = null, return_date = now() WHERE book_id = " + bookId;

		jdbcTemplate.update(sql);
	}

	/**
	 * 貸出し書籍リストを取得する
	 *
	 * @return 貸出し書籍リスト
	 */
	public List<RentBookInfo> getRentBookList() {

		List<RentBookInfo> getedRentBookList = jdbcTemplate.query(

				"SELECT rent.id, rent.book_id, books.title, rent.rent_date, rent.return_date FROM rent LEFT OUTER JOIN books ON rent.book_id = books.id",
				new RentBookInfoRowMapper());

		return getedRentBookList;
	}
}
