package com.hawk.life.ui.fragment.timeline;

import android.os.Bundle;
import android.text.TextUtils;

import com.hawk.library.common.utils.Logger;
import com.hawk.library.network.http.Params;
import com.hawk.library.network.task.TaskException;
import com.hawk.library.ui.fragment.ABaseFragment;
import com.hawk.library.ui.fragment.ARefreshFragment;
import com.hawk.library.ui.fragment.AStripTabsFragment;
import com.hawk.life.base.AppContext;
import com.hawk.life.base.AppSettings;
import com.hawk.life.support.bean.StatusContents;

import java.lang.reflect.Method;

/**
 * 默认首页的列表<br/>
 * 
 * @author wangdan
 *
 */
public class TimelineDefaultFragment extends ATimelineFragment {

	public static TimelineDefaultFragment newInstance() {
		TimelineDefaultFragment fragment = new TimelineDefaultFragment();

		
		return fragment;
	}

    static int count = 0;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        Logger.e("TimelineDefaultFragment, " + --count);
    }

    public TimelineDefaultFragment() {
        Logger.e("TimelineDefaultFragment, " + ++count);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
	protected void requestData(ARefreshFragment.RefreshMode mode) {
        // 如果还没有加载过数据，切且显示的是当前的页面
        if (getTaskCount("TimelineTask") == 0) {
            if (getPagerCurrentFragment() == this)
                new DefGroupTimelineTask(mode).execute();
        }
        else {
            new DefGroupTimelineTask(mode).execute();
        }
	}
	
	class DefGroupTimelineTask extends TimelineTask {

		public DefGroupTimelineTask(ARefreshFragment.RefreshMode mode) {
			super(mode);
		}

		@Override
		protected StatusContents workInBackground(ARefreshFragment.RefreshMode mode, String previousPage, String nextPage, Void... p) throws TaskException {
			try {
				String uid = AppContext.getUser().getIdstr();
				
				Params params = new Params();

				if (mode == ARefreshFragment.RefreshMode.refresh && !TextUtils.isEmpty(previousPage))
					params.addParameter("since_id", previousPage);

				if (mode == ARefreshFragment.RefreshMode.update && !TextUtils.isEmpty(nextPage))
					params.addParameter("max_id", nextPage);
				
				params.addParameter("count", String.valueOf(AppSettings.getTimelineCount()));

                long time = System.currentTimeMillis();
				Method method = SinaSDK.class.getMethod(getGroup().getType(), new Class[] { Params.class });
				StatusContents beans = (StatusContents) method.invoke(SinaSDK.getInstance(AppContext.getToken(), getTaskCacheMode(this)), params);

                // 如果是缓存，延迟一点返回，防止有点点卡顿
                if (beans.isCache() && System.currentTimeMillis() - time < 100) {
                    try {
                        Thread.sleep(150);
                    } catch (Exception e) {

                    }
                }

				return beans;
			} catch (Exception e) {
				e.printStackTrace();

				if (e.getCause() instanceof TaskException)
					throw ((TaskException) e.getCause());
				if (e instanceof TaskException)
					throw (TaskException) e;
				
				throw new TaskException(TextUtils.isEmpty(e.getMessage()) ? "服务器错误" : e.getMessage());
			}
		}
		
	}
	
}
