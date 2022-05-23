package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.RentBookInfo;

@Configuration
public class RentBookInfoRowMapper implements RowMapper<RentBookInfo> {

	@Override
	public RentBookInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Query結果（ResultSet rs）を、オブジェクトに格納する実装
		RentBookInfo rentInfo = new RentBookInfo();
		rentInfo.setRentId(rs.getInt("id"));
		rentInfo.setBookId(rs.getInt("book_id"));
		rentInfo.setTitle(rs.getString("title"));
		rentInfo.setRentDate(rs.getString("rent_date"));
		rentInfo.setReturnDate(rs.getString("return_date"));
		return rentInfo;
	}
}