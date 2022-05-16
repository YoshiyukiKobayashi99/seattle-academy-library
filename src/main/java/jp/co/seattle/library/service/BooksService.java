package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {
		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				// タイトル順に
				"select id, title, author, publisher, publish_date, thumbnail_url from books order by title",
				new BookInfoRowMapper());

		return getedBookList;
	}
	
	/**
	 * 検索ワードに引っかかる書籍リストを取得する
	 *
	 * @param  検索単語
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList(String searhWord) {
		
		List<BookInfo> getedBookList = jdbcTemplate.query(
				// タイトル順に
				"select id, title, author, publisher, publish_date, thumbnail_url from books WHERE title LIKE '%" + searhWord + "%' ORDER BY title",
				new BookInfoRowMapper());

		return getedBookList;
	}
	

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT * FROM books where id =" + bookId;

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 
	 * 書籍IDに紐づく書籍詳細情報を引数なしで取得する
	 * 
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */

	public BookDetailsInfo getBookInfo() {

		String sql = "select * from books where id = (select max(id) from books);";

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */

	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author, publisher, thumbnail_name, thumbnail_url, publish_date, isbn, description, reg_date, upd_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl() + "','" + bookInfo.getPublishDate()
				+ "','" + bookInfo.getIsbn() + "','" + bookInfo.getDescription() + "'," + "now()," + "now())";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を一括登録する
	 *
	 * @param bookList 書籍情報
	 */

	public void bulkRegistBook(List<BookDetailsInfo> bookList) {

		for (BookDetailsInfo bookInfo : bookList) {
			String sql = "INSERT INTO books (title, author, publisher, thumbnail_url, publish_date, isbn, reg_date, upd_date) VALUES ('"
					+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
					+ bookInfo.getThumbnailUrl() + "','" + bookInfo.getPublishDate() + "','" + bookInfo.getIsbn() + "',"
					+ "now()," + "now())";

			jdbcTemplate.update(sql);
		}
	}

	/**
	 * 書籍を更新する
	 *
	 * @param bookInfo 書籍情報
	 * @param bookId   書籍ID
	 */

	public void updateBook(BookDetailsInfo bookInfo) {

		String sql = "UPDATE books SET (title, author, publisher, thumbnail_name, thumbnail_url, publish_date, isbn, description, upd_date) = ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl() + "','" + bookInfo.getPublishDate()
				+ "','" + bookInfo.getIsbn() + "','" + bookInfo.getDescription() + "'," + "now())" + "WHERE id = "
				+ bookInfo.getBookId();

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を削除する
	 * 
	 * @param bookId 書籍ID
	 */

	public void deleteBook(int bookId) {

		String sql = "DELETE FROM books WHERE id = '" + bookId + "'";

		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍が貸出しテーブルにあるかどうか
	 * 
	 * @param bookId 書籍ID
	 * @return 書籍ステータス（貸出し状態）
	 */

	public String bookStatus(int bookId) {
		String sql = "SELECT rent.book_id FROM books LEFT OUTER JOIN rent ON books.id = rent.book_id WHERE books.id ="
				+ bookId;

		String bookStatus = jdbcTemplate.queryForObject(sql, String.class);

		return bookStatus;

	}
}
