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

	public RentBookInfo() {

	}

	// コンストラクタ
	public RentBookInfo(int rentId, int bookId) {
		this.rentId = rentId;
		this.bookId = bookId;
	}

}