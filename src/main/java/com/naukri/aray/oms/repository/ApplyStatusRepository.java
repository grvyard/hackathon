package com.naukri.aray.oms.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naukri.aray.oms.model.ApplyStatus;

@Repository
public interface ApplyStatusRepository extends JpaRepository<ApplyStatus, Serializable> {
	
	public int countByBitflagDoneAndBitflagDateAndCountryType(String bitflagDone, String bitflagDate, String countryType);
	
	public int countByBitflagDoneAndBitflagDateLessThanAndCountryType(String bitflagDone, String bitflagDate, String countryType);
	
	public Integer countByBitflagDoneAndBitflagDateAndPickedBy(String bitflagDone, String bitflagDate, String pickedBy);

	@Query("Select compId from ApplyStatus where bitflagDone = (:bitflagDone) and bitflagDate = (:bitflagDate)")
	List<Integer> findCompanyIds(@Param("bitflagDone") String bitflagDone, @Param("bitflagDate") String bitflagDate);
	
	@Query("Select compId from ApplyStatus where bitflagDone = (:bitflagDone) and bitflagDate <= (:bitflagDate) and countryType = (:countryType)")
	List<Integer> findCompanyIdsTillNow(@Param("bitflagDone") String bitflagDone, @Param("bitflagDate") String bitflagDate, @Param("countryType") String countryType);

}

