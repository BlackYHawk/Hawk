package com.hawk.life.support.bean;


import com.hawk.library.network.biz.IResult;

import java.io.Serializable;
import java.util.List;

public class StatusComments implements Serializable, IResult {

	private static final long serialVersionUID = 2420923134169920046L;
	
	private List<StatusComment> comments;

	private boolean cache;

	private boolean _expired;

	private boolean _noMore;

    private long length;

	public StatusComments() {

	}

	public StatusComments(List<StatusComment> comments) {
		this.comments = comments;
	}

	public List<StatusComment> getComments() {
		return comments;
	}

	public void setComments(List<StatusComment> comments) {
		this.comments = comments;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

	@Override
	public boolean expired() {
		return _expired;
	}

	@Override
	public boolean noMore() {
		return _noMore;
	}
	
	public void setExpired(boolean expired) {
		this._expired = expired;
	}
	
	public void setNoMore(boolean noMore) {
		this._noMore = noMore;
	}

	@Override
	public String[] pagingIndex() {
		return null;
	}

    public long getLength() {
        return length;
    }

}
