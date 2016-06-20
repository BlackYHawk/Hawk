package com.hawk.life.support.paging;

import android.text.TextUtils;

import com.hawk.library.support.paging.IPaging;
import com.hawk.life.support.bean.StatusContent;
import com.hawk.life.support.bean.StatusContents;
import com.hawk.life.support.utils.AisenUtils;


/**
 * 微博分页
 * 
 * @author wangdan
 * 
 */
public class TimelinePagingProcessor implements IPaging<StatusContent, StatusContents> {

	private static final long serialVersionUID = -1563104012290641720L;

	private String firstId;

	private String lastId;

	@Override
	public IPaging<StatusContent, StatusContents> newInstance() {
		return new TimelinePagingProcessor();
	}

	@Override
	public void processData(StatusContents newDatas, StatusContent firstData, StatusContent lastData) {
		if (firstData != null)
			firstId = AisenUtils.getId(firstData);
		if (lastData != null)
			lastId = AisenUtils.getId(lastData);
	}

	@Override
	public String getPreviousPage() {
		return firstId;
	}

	@Override
	public String getNextPage() {
		if (TextUtils.isEmpty(lastId))
			return null;

		return (Long.parseLong(lastId) - 1) + "";
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void setPage(String previousPage, String nextPage) {
		this.firstId = previousPage;
		this.lastId = nextPage;
	}

}