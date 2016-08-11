package com.project.main.autohome.ui.fragment.pager;

import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.project.main.autohome.R;
import com.project.main.autohome.model.bean.OriginalBean;
import com.project.main.autohome.model.net.NetUrl;
import com.project.main.autohome.model.net.VolleyInstence;
import com.project.main.autohome.model.net.VolleyInterfaceResult;
import com.project.main.autohome.tools.CustomRefreshListView;
import com.project.main.autohome.tools.NetWorkConnectedToast;
import com.project.main.autohome.ui.adapter.OriginalAdapter;
import com.project.main.autohome.ui.fragment.AbsBaseFragment;
import com.youth.banner.Banner;

import java.util.List;

/**
 * Created by youyo on 2016/7/12 0012.
 * 优创页
 */
public class OriginalFrag extends AbsBaseFragment implements VolleyInterfaceResult {
    private String[] imgurl;
    private Banner banner;
    private CustomRefreshListView orig_ls;
    private OriginalAdapter originalAdapter;
    private String imgUrl = NetUrl.UNIHUB_URL;
    private String url = NetUrl.UNIHUB_URL;

    @Override
    protected int setLayout() {
        return R.layout.original_frag;
    }

    @Override
    protected void initView() {
        orig_ls = byView(R.id.orig_listview);
    }

    @Override
    protected void initData() {
        originalAdapter = new OriginalAdapter(getContext());
        //        List内容解析
        VolleyInstence.getInstence(getContext()).startRequest(url, this);
        //        轮播图解析
        VolleyInstence.getInstence(getContext()).startRequest(imgUrl, new VolleyInterfaceResult() {
            @Override
            public void success(String str) {
                Gson gson = new Gson();
                OriginalBean originalBean = gson.fromJson(str, OriginalBean.class);
                List<OriginalBean.ResultBean.FocusimgsBean> focusimgs = originalBean.getResult().getFocusimgs();
                imgurl = new String[focusimgs.size()];
                for (int i = 0; i < focusimgs.size(); i++) {
                    imgurl[i] = originalBean.getResult().getFocusimgs().get(i).getImgurl();
                }
                initBanner();
            }

            @Override
            public void failure() {

            }
        });

        View view = LayoutInflater.from(getContext()).inflate(R.layout.art_item_up_ls, null);
        orig_ls.addHeaderView(view);
        banner = (Banner) view.findViewById(R.id.uptodata_banner);
        orig_ls.setAdapter(originalAdapter);
        // 检查网路
        NetWorkConnectedToast.getConnectedToast().isNet(getContext());
    }

    @Override
    public void success(String str) {
        Gson gson = new Gson();
        OriginalBean origBean = gson.fromJson(str, OriginalBean.class);
        List<OriginalBean.ResultBean.NewslistBean> beanList = origBean.getResult().getNewslist();
        originalAdapter.setOriBean(beanList);
        orig_ls.setAdapter(originalAdapter);
    }

    @Override
    public void failure() {

    }

    private void initBanner() {
        //        设置指示器
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);
        //        设置时间
        banner.setDelayTime(3000);
        //        轮播图片
        banner.setImages(imgurl);
    }
}
