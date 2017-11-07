package pick.image.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 图片相册选择插件
 * @author ke_li
 * @date 2017/11/6
 */
public class ImagePickActivity extends AppCompatActivity {
    /**
     * 显示中间抬头标题
     */
    private String TITLE_TEXT;
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
     * 未选中右上角图标
     */
    private  String UNCHECK_IMG_RES;
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
     * 取消字体大小
     */
    private String CANCEL_COLOR;
    /**
     * 确定字体大小
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
        TITLE_TEXT = intent.getStringExtra("title_text");
        OK_TEXT = intent.getStringExtra("ok_text");
        CANCEL_TEXT = intent.getStringExtra("cancel_text");
        CHCEK_IMG_RES = intent.getStringExtra("check_img_res");
        UNCHECK_IMG_RES = intent.getStringExtra("uncheck_img_res");
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

}

