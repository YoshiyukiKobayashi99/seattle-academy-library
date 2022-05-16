package jp.co.seattle.library.service;

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
	 * 貸出する書籍を登録する
	 *
	 * @param bookId 書籍ID
	 */
	public void rentBook(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "INSERT INTO rent (book_id) VALUES (" + bookId + ")";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍IDに紐づく書籍が貸出しされているかどうか確認
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public RentBookInfo getRentBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT * FROM rent where book_id =" + bookId;

		try {
			RentBookInfo selectedRentInfo = jdbcTemplate.queryForObject(sql, new RentBookInfoRowMapper());
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
		String sql = "DELETE FROM rent WHERE book_id = " + bookId;

		jdbcTemplate.update(sql);
	}

}
