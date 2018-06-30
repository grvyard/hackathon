package com.naukri.aray.apply.repository;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.naukri.aray.apply.model.EmployeeInfo;

@Component
public class EmployeeInfoRepository {
	
//	@Query(value = "Select avg(ctc) from employee_info as a where a.college = (:college) and a.branch = (:branch) and a.batch = (:batch)", nativeQuery = true)
//	Double findAvgPackageOfThatBatch(@Param("college") String college, @Param("branch") String branch, @Param("batch") String batch);
//
//	@Query(value = "Select a.*, avg(ctc) as ctc from employee_info as a where a.college = (:college) and a.branch = (:branch) and a.batch >= (:startbatch) and a.batch <= (:endbatch) group by batch;", nativeQuery = true)
//	List<EmployeeInfo> findAvgPackageOfGivenRange(@Param("college") String college, @Param("branch") String branch, @Param("startbatch") String startbatch, @Param("endbatch") String endbatch);
//	
//	@Query("Select currentCompany from employeeInfo where college = (:college) and branch = (:branch) and batch = (:batch)")
//	List<EmployeeInfo> findAllCompaniesOfYourBatch(@Param("college") String college, @Param("branch") String branch, @Param("batch") String batch);
//	
//	@Query("Select currentCompany from employeeInfo where college = (:college) and branch = (:branch) and batch >= (:startbatch) and batch <= (:endbatch) group by batch;")
//	List<EmployeeInfo> findAllCompaniesOfGivenRange(@Param("college") String college, @Param("branch") String branch, @Param("startbatch") String startbatch, @Param("endbatch") String endbatch);
	
	public Double findAvgPackageOfThatBatch(Connection conn, String college, String branch, String batch) throws SQLException {
		 
		String sql = "Select avg(ctc) as avgCtc from employee_info as a where a.college = ? and a.branch = ? and a.batch = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, college);
		ps.setString(2,  branch);
		ps.setString(3, batch);
		ResultSet rs = ps.executeQuery();
		
		Double avgCtc = (double) 0;
		while(rs.next()) { 
			rs.getRow();
			avgCtc = rs.getDouble("avgCtc");
		}
		return avgCtc;
	}
	
	public Map<String, Double> findAvgPackageOfGivenRange(Connection conn, String college, String branch, String startbatch, String endbatch) throws SQLException {

		String sql = "Select batch, avg(ctc) as avgCtc from employee_info as a where a.college = ? and a.branch = ? and a.batch >= ? and a.batch <= ? group by batch";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, college);
		ps.setString(2,  branch);
		ps.setString(3, startbatch);
		ps.setString(4, endbatch);
		ResultSet rs = ps.executeQuery();
		
		Map<String, Double> map = new LinkedHashMap<>(); 
		while(rs.next()) { 
			rs.getRow();
			String batch = rs.getString("batch");
			Double ctc = rs.getDouble("avgCtc");
			map.put(batch, ctc);
		}
		return map;
	}
	
	public List<String> getCompaniesListByBatch(Connection conn, String college, String branch, String batch) throws SQLException {
		 
		String sql = "Select company  from employee_info as a where a.college = ? and a.branch = ? and a.batch = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, college);
		ps.setString(2,  branch);
		ps.setString(3, batch);
		ResultSet rs = ps.executeQuery();
		
		List<String> list = new ArrayList<>();
		while(rs.next()) { 
			rs.getRow();
			list.add(rs.getString("company"));
		}
		return list;
	}
	
	public List<String> getCompaniesListByGivenRange(Connection conn, String college, String branch, String startBatch, String endBatch) throws SQLException {
		 
		String sql = "Select company  from employee_info as a where a.college = ? and a.branch = ? and a.batch >= ? and a.batch <= ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, college);
		ps.setString(2,  branch);
		ps.setString(3, startBatch);
		ps.setString(4, endBatch);
		ResultSet rs = ps.executeQuery();
		
		List<String> list = new ArrayList<>();
		while(rs.next()) { 
			rs.getRow();
			list.add(rs.getString("company"));
		}
		return list;
	}
	
	
}
