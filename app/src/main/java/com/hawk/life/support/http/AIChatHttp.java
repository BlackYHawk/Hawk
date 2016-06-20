package com.hawk.life.support.http;

import android.text.TextUtils;

import com.hawk.library.network.biz.ABizLogic;
import com.hawk.library.network.http.HttpConfig;
import com.hawk.library.network.http.Params;
import com.hawk.library.network.task.TaskException;
import com.hawk.life.support.bean.TulingBean;
import com.hawk.life.support.utils.Constants;

/**
 * Created by heyong on 16/6/20.
 */
public class AIChatHttp extends ABizLogic {

    public AIChatHttp() {
    }

    @Override
    protected HttpConfig configHttpConfig() {
        HttpConfig httpConfig = new HttpConfig();
        httpConfig.baseUrl = getSetting(Constants.AI_URL).getValue();

        httpConfig.contentType = "application/x-www-form-urlencoded";
        return httpConfig;
    }

    private String getAppKey() {
        return getSetting("tuling_key").getValue();
    }

    private Params configParams(Params params) {
        if (params == null) {
            params = new Params();
        }

        if (!params.containsKey("key"))
            params.addParameter("key", getAppKey());

        return params;
    }

    /**
     * 根据输入智能回复文字。<br>
     * <br>
     *
     *
     * @param info
     *            (true):必须设置
     * @return
     */
    public TulingBean tulingText(String info) throws TaskException {
        Params params = new Params();
        if (!TextUtils.isEmpty(info))
            params.addParameter("info", info);
        params.setEncodeAble(false);

        return doGet(getSetting(""), configParams(params), TulingBean.class);
    }
}
