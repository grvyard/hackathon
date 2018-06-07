package com.naukri.aray.apply.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

@Repository
public class ApplyLogRepository {

	public HashMap<String, String> getnumOfApplies(Connection conn) throws SQLException, ClassNotFoundException {
		HashMap<String, String> applies = new HashMap<String, String>();
		
		
		String sql = "select applogComId,count(*) as cnt from APPLY_LOG where applogDate >= DATE_SUB(now(), INTERVAL 30 DAY) and applogDate < date(now()) and substring(applogFile,7,1) in (5) group by applogComId order by count(*) desc";
		PreparedStatement ps = conn.prepareStatement(sql);
		// ps.setString(1, countryType);
		// ps.setString(2, bitflagDate);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			
			applies.put(rs.getString("applogComId"), rs.getString("cnt"));
			
		}
		return applies;
	}
}
