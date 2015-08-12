package cn.bingoogolapple.acvp.velocitytracker.engine;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.velocitytracker.model.RefreshModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/26 上午1:03
 * 描述:
 */
public class DataEngine {
    private static AsyncHttpClient sAsyncHttpClient = new AsyncHttpClient();

    public static void loadInitDatas(final RefreshModelResponseHandler responseHandler) {
        sAsyncHttpClient.get("http://7xk9dj.com1.z0.glb.clouddn.com/refreshlayout/api/defaultdata.json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onFailure();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<RefreshModel> refreshModels = new GsonBuilder().create().fromJson(responseString, new TypeToken<ArrayList<RefreshModel>>() {
                }.getType());
                responseHandler.onSuccess(refreshModels);
            }
        });
    }

    public interface RefreshModelResponseHandler {
        void onFailure();

        void onSuccess(List<RefreshModel> refreshModels);
    }

}