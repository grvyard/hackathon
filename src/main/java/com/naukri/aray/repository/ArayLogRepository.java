package com.naukri.aray.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.naukri.aray.model.ErrorData;

@Component
public class ArayLogRepository {

	public Map<String, Integer> findReasonandCountMap(Connection conn, String countryType, String createdAt) throws SQLException {
		Map<String, Integer> map = new LinkedHashMap<>(); 
		String sql = "select reason, count(distinct applyId) as cnt from ARAY_LOG where  date(createdAt) = ? and countryType = ? group by reason with rollup Order by cnt desc";
		//String sql = "select reason, count(distinct applyId) as cnt from ARAY_LOG where date(createdAt) = ? group by reason with rollup";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, createdAt);
		ps.setString(2, countryType);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) { rs.getRow();
			String reason = rs.getString("reason");
			Integer count = rs.getInt("cnt");
			if (reason == null) {
				reason = "totalUniqueHits";
			}
			map.put(reason, count);
		}
		return map;
	}
	
	public Map<String, List<ErrorData>> findTop5ErrosByType(Connection conn, String date, String countryType, Set<String> errors) throws SQLException {
		Map<String, List<ErrorData>> errorsData = new HashMap<>();
		for (String error : errors) {
			if (error.equals("Success") || error.equals("totalUniqueHits") || error.equals("RegDoneVerification")) {
				continue;
			}
			//String sql = "SELECT companyId, count(1) as cnt, failedStep from ARAY_LOG where date(createdAt) = ? and reason = ? group by companyId order by cnt desc LIMIT 5";
			String sql = "SELECT companyId, count(distinct applyId) as cnt, failedStep from ARAY_LOG a where date(a.createdAt) = ? and reason = ? and countryType = ? group by companyId order by cnt desc LIMIT 5";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,  date);
			ps.setString(2, error);
			ps.setString(3, countryType);
			ResultSet rs = ps.executeQuery();
			
			List<ErrorData> list = new ArrayList<>();
			while (rs.next()) {
				ErrorData errorData = new ErrorData();
				errorData.setCompanyId(rs.getString("companyId"));
				errorData.setCnt(rs.getInt("cnt"));
				errorData.setFailedStep(rs.getString("failedStep"));
				list.add(errorData);
			}
			errorsData.put(error, list);
		}
		return errorsData;
	}
	
//	@Query(value = "SELECT companyId, count(distinct applyId) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and countryType = 'ng' group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
//	Object[] findTop5ByFailuresNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason);
//	
//	//top 5 expired jobs
//	@Query(value = "SELECT companyId, count(distinct applyId) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and countryType = 'ni' group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
//	Object[] findTop5ByExpiredJobsNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason);
//	
//	@Query(value = "SELECT companyId, count(distinct applyId) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and countryType = 'ng' group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
//	Object[] findTop5ByExpiredJobsNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason);
//	
//	//top 5 internal server error
//	@Query(value = "SELECT companyId, count(distinct applyId) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and countryType = 'ni' group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
//	Object[] findTop5ByInternalServerErrorNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason);
//	
//	@Query(value = "SELECT companyId, count(distinct applyId) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and countryType = 'ng' group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
//	Object[] findTop5ByInternalServerErrorNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason);
//	
}
