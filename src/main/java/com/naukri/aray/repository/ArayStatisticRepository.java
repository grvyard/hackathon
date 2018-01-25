package com.naukri.aray.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.naukri.aray.model.ArayStatistic;

public interface ArayStatisticRepository extends JpaRepository<ArayStatistic, Serializable>{

	public List<ArayStatistic> findOneByDate(String date);
}
