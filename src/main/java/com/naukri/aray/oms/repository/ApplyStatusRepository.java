package com.naukri.aray.oms.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.naukri.aray.oms.model.ApplyStatus;

@Component
public class ApplyStatusRepository {
	
	public int countByBitflagDoneAndBitflagDateAndCountryType(Connection conn, String bitflagDate, String countryType) throws SQLException {
		String sql = "select count(*) as cnt from apply_status where bitflagDone = 'yes' and countryType = ? and date(bitflagDate) = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, countryType);
		ps.setString(2, bitflagDate);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt("cnt");
		}
		return rs.getInt("cnt");
	}
	
	public int countByBitflagDoneAndBitflagDateLessThanAndCountryType(Connection conn, String bitflagDate, String countryType) throws SQLException {
		String sql = "select count(*) as cnt from apply_status where bitflagDone = 'yes' and countryType = ? and date(bitflagDate) <= ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, countryType);
		ps.setString(2, bitflagDate);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt("cnt");
		}
		return rs.getInt("cnt");
	}
//	
//	public Integer countByBitflagDoneAndBitflagDateAndPickedBy(String bitflagDone, String bitflagDate, String pickedBy);
//
//	@Query("Select compId from ApplyStatus where bitflagDone = (:bitflagDone) and bitflagDate = (:bitflagDate)")
//	List<Integer> findCompanyIds(@Param("bitflagDone") String bitflagDone, @Param("bitflagDate") String bitflagDate);
//	
//	@Query("Select compId from ApplyStatus where bitflagDone = (:bitflagDone) and bitflagDate <= (:bitflagDate) and countryType = (:countryType)")
//	List<Integer> findCompanyIdsTillNow(@Param("bitflagDone") String bitflagDone, @Param("bitflagDate") String bitflagDate, @Param("countryType") String countryType);

}

