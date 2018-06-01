package com.naukri.aray.responses.model;

public class ApplyOnEmailStats {

	private int took;
	private boolean timed_out;
	private Object _shards;
	private Object hits;
	
	public int getTook() {
		return took;
	}
	public void setTook(int took) {
		this.took = took;
	}
	public boolean isTimed_out() {
		return timed_out;
	}
	public void setTimed_out(boolean timed_out) {
		this.timed_out = timed_out;
	}
	public Object get_shards() {
		return _shards;
	}
	public void set_shards(Object _shards) {
		this._shards = _shards;
	}
	public Object getHits() {
		return hits;
	}
	public void setHits(Object hits) {
		this.hits = hits;
	}
	
	
	
	
}
