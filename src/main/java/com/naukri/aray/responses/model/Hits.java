package com.naukri.aray.responses.model;

import java.util.List;

public class Hits {

	private int total_count;
	private int[] hits;
	private int max_score;
	public int getTotal_count() {
		return total_count;
	}
	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}
	
	public int[] getHits() {
		return hits;
	}
	public void setHits(int[] hits) {
		this.hits = hits;
	}
	public int getMax_score() {
		return max_score;
	}
	public void setMax_score(int max_score) {
		this.max_score = max_score;
	}
	
}
