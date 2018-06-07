package com.naukri.aray.oms.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public class ApplyStatusRepository {

	public int countByBitflagDoneAndBitflagDateAndCountryType(Connection conn, String bitflagDate, String countryType)
			throws SQLException {
		String sql = "select count(*) as cnt from APPLY_STATUS where bitflagDone = 'yes' and countryType = ? and date(bitflagDate) = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, countryType);
		ps.setString(2, bitflagDate);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt("cnt");
		}
		return rs.getInt("cnt");
	}

	public int countByBitflagDoneAndBitflagDateLessThanAndCountryType(Connection conn, String bitflagDate,
			String countryType) throws SQLException {
		
		String sql = "select count(*) as cnt from APPLY_STATUS where bitflagDone = 'yes' and countryType = ? and date(bitflagDate) <= ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, countryType);
		ps.setString(2, bitflagDate);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return rs.getInt("cnt");
		}
		return rs.getInt("cnt");
	}

	public void updateNumOfApplies(Connection conn, HashMap<String, String> applies) throws SQLException, ClassNotFoundException {

		int rs = 0;
		
		for (String entry : applies.keySet()) {
			String sql = "Update APPLY_STATUS set numOfApplies = ? where compId = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			System.out.println(entry + " " + applies.get(entry));
			ps.setString(2, entry);
			ps.setString(1, applies.get(entry));
			System.out.println(ps);
			rs = ps.executeUpdate();
			
			if (rs == 0) {
				sql = "insert into APPLY_STATUS(compId, numOfApplies) values(?, ?)";
				PreparedStatement ps1 = conn.prepareStatement(sql);
				ps1.setString(1, entry);
				ps1.setString(2, applies.get(entry));
				System.out.println(ps1);
				ps1.execute();
			}
		}
		
	}
	//
	// public Integer countByBitflagDoneAndBitflagDateAndPickedBy(String
	// bitflagDone, String bitflagDate, String pickedBy);
	//
	// @Query("Select compId from ApplyStatus where bitflagDone = (:bitflagDone)
	// and bitflagDate = (:bitflagDate)")
	// List<Integer> findCompanyIds(@Param("bitflagDone") String bitflagDone,
	// @Param("bitflagDate") String bitflagDate);
	//
	// @Query("Select compId from ApplyStatus where bitflagDone = (:bitflagDone)
	// and bitflagDate <= (:bitflagDate) and countryType = (:countryType)")
	// List<Integer> findCompanyIdsTillNow(@Param("bitflagDone") String
	// bitflagDone, @Param("bitflagDate") String bitflagDate,
	// @Param("countryType") String countryType);

}
