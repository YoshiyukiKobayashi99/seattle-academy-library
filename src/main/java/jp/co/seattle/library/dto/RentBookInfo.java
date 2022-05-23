package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 貸出し書籍情報格納DTO
 */
@Configuration
@Data
public class RentBookInfo {

	private int rentId;

	private int bookId;
	
	private String title;
	
	private String rentDate;
	
	private String returnDate;
	

	public RentBookInfo() {

	}

	// コンストラクタ
	public RentBookInfo(int rentId, int bookId, String title, String rentDate, String returnDate) {
		this.rentId = rentId;
		this.bookId = bookId;
		this.title = title;
		this.rentDate = rentDate;
		this.returnDate = returnDate;
	}

}