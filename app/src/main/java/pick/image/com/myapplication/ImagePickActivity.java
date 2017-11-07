package pick.image.com.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

/**
 * 图片相册选择插件
 * @author ke_li
 * @date 2017/11/6
 */
public class ImagePickActivity extends AppCompatActivity {
    /**
     * 确认名称
     */
    private String OK_TEXT;
    /**
     * 取消名称
     */
    private String CANCEL_TEXT;
    /**
     * 选中右上角图标显示URI地址
     */
    private String CHCEK_IMG_RES;
    /**
     * 显示toolbar的颜色
     */
    private  String BANNER_CORLOR;
    private TextView title_cancel;
    private TextView title_ok;
    private ViewGroup pick_main;
    private RecyclerView file_list;
    /**
     * 默认标题字体大小
     */
    private int MANAGER_TITLE_TEXT_SIZE;
    /**
     * 取消字体颜色
     */
    private String CANCEL_COLOR;
    /**
     * 确定字体颜色
     */
    private  String OK_COLOR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        loadedView();

    }


    private void initData() {
        Intent intent = getIntent();
        OK_TEXT = intent.getStringExtra("ok_text");
        CANCEL_TEXT = intent.getStringExtra("cancel_text");
        CHCEK_IMG_RES = intent.getStringExtra("check_img_res");
        BANNER_CORLOR= intent.getStringExtra("banner_color");
        MANAGER_TITLE_TEXT_SIZE = intent.getIntExtra("title_text_size",10);
    }


    private void initView() {
        title_cancel = Utils.findViewById(TextView.class, this, R.id.title_cancel);
        title_ok = Utils.findViewById(TextView.class, this, R.id.title_ok);
        pick_main = Utils.findViewById(ViewGroup.class, this, R.id.pick_main);
        file_list = Utils.findViewById(RecyclerView.class, this, R.id.file_list);

    }


    private void loadedView() {
        getTextPx(MANAGER_TITLE_TEXT_SIZE,title_cancel);
        getTextPx(MANAGER_TITLE_TEXT_SIZE,title_ok);
        title_ok.setTextColor(getColorParcelable(OK_COLOR));
        title_cancel.setTextColor(getColorParcelable(CANCEL_COLOR));
        title_ok.setText(OK_TEXT);
        title_cancel.setText(CANCEL_TEXT);
        pick_main.setBackgroundColor(getColorParcelable(BANNER_CORLOR));
        GridLayoutManager manager = new GridLayoutManager(this,3);
        file_list.setLayoutManager(manager);
    }

    /**
     * 获取字体大小
     * @param point
     * @return
     */
    private void getTextPx(int point, TextView textView){
        int windowWidth = Utils.getWindowWidth(this);
        int px = windowWidth*point/240;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,px);
    }

    /**
     * 获取自定义颜色parse
     * @param color
     * @return
     */
    private  int getColorParcelable(String color){
        String match = "[a-f0-9A-F]{6}";
        String matchApache = "[a-f0-9A-F]{8}";
        if (TextUtils.isEmpty(color))return Color.TRANSPARENT;
        if (color.matches(match)||color.matches(matchApache)){
            return  Color.parseColor("#"+color);
        }else return Color.TRANSPARENT;
    }





    public   class  ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }




}

