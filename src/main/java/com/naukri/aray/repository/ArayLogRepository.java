package com.naukri.aray.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naukri.aray.model.ArayLog;

@Repository
public interface ArayLogRepository extends JpaRepository<ArayLog, Serializable> {

	List<ArayLog> findAllByReason(String reason);
	
	//success hits
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and companyId not in (:companiesList)")
	int findSuccessHitsNI(@Param("status") int status,@Param("createdAt") Date createdAt, @Param("companiesList") List<Integer> companiesList);
	
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and companyId in  (:companiesList)")
	int findSuccessHitsNG(@Param("status") int status,@Param("createdAt") Date createdAt, @Param("companiesList") List<Integer> companiesList);
	
	//failure hits
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId not in (:companiesList)")
	int findFailureHitsNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId in (:companiesList)")
	int findFailureHitsNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	//expired jobs
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId not in (:companiesList)")
	int findExpiredJobsNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId in (:companiesList)")
	int findExpiredJobsNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	//internal server error
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId not in (:companiesList)")
	int findInternalServerErrorsNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	@Query("SELECT count(1) from ArayLog a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId in (:companiesList)")
	int findInternalServerErrorsNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	//top 5 failures
	@Query(value = "SELECT companyId, count(*) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId not in (:companiesList) group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
	Object[] findTop5ByFailuresNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	@Query(value = "SELECT companyId, count(*) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId in (:companiesList) group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
	Object[] findTop5ByFailuresNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	//top 5 expired jobs
	@Query(value = "SELECT companyId, count(*) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId not in (:companiesList) group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
	Object[] findTop5ByExpiredJobsNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	@Query(value = "SELECT companyId, count(*) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId in (:companiesList) group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
	Object[] findTop5ByExpiredJobsNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	//top 5 internal server error
	@Query(value = "SELECT companyId, count(*) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId not in (:companiesList) group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
	Object[] findTop5ByInternalServerErrorNI(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
	@Query(value = "SELECT companyId, count(*) as failureCount, failedStep from ARAY_LOG a where a.status = (:status) and date(a.createdAt) = (:createdAt) and reason = :reason and companyId in (:companiesList) group by companyId order by failureCount desc LIMIT 5", nativeQuery = true)
	Object[] findTop5ByInternalServerErrorNG(@Param("status") int status, @Param("createdAt") Date createdAt,@Param("reason") String reason, @Param("companiesList") List<Integer> companiesList);
	
}
