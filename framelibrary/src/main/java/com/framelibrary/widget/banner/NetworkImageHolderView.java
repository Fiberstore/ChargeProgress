package com.framelibrary.widget.banner;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bigkoo.convenientbanner.holder.Holder;
import com.framelibrary.R;
import com.framelibrary.utils.HZDisplayUtil;
import com.framelibrary.widget.HZCallbackListener;

/**
 * Created by Sai on 15/8/4.
 * 网络图片加载例子
 * 添加自己的item布局
 */
public class NetworkImageHolderView implements Holder<String> {

    private int width;

    private int height;

    private HZCallbackListener listener;

    public NetworkImageHolderView(int width, int height, HZCallbackListener listener) {
        this.width = width;
        this.height = height;
        this.listener = listener;
    }

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        int s_width = HZDisplayUtil.getWidth(context);
        int s_height = (int) ((s_width * height * 1.0) / width);
        View view = inflater.inflate(R.layout.item_banner_image, null);
        imageView = (ImageView) view.findViewById(R.id.item_banner_img);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(s_width, s_height);
        imageView.setLayoutParams(params);
        return view;
    }

    @Override
    public void UpdateUI(Context context, final int position, String data) {
        if (!TextUtils.isEmpty(data)) {
//            setImageData()
//            imageView.setUrlObj(data, new ImageSize(this.width, this.height));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.call(position);
                }
            });
        }
    }
}
