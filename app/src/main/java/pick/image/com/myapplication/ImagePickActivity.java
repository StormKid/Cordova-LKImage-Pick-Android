package pick.image.com.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 图片相册选择插件
 * @author ke_li
 * @date 2017/11/6
 */
public class ImagePickActivity extends AppCompatActivity implements View.OnClickListener{
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
    /**
     *
     */
    private  int ITEM_SIZE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        loadedView();
        initListner();

    }

    private void initListner() {
        title_ok.setOnClickListener(this);
        title_cancel.setOnClickListener(this);

    }


    private void initData() {
        Intent intent = getIntent();
        OK_TEXT = intent.getStringExtra("ok_text");
        CANCEL_TEXT = intent.getStringExtra("cancel_text");
        CHCEK_IMG_RES = intent.getStringExtra("check_img_res");
        BANNER_CORLOR= intent.getStringExtra("banner_color");
        MANAGER_TITLE_TEXT_SIZE = intent.getIntExtra("title_text_size",9);
    }


    private void initView() {
        title_cancel = Utils.findViewById(TextView.class, this, R.id.title_cancel);
        title_ok = Utils.findViewById(TextView.class, this, R.id.title_ok);
        pick_main = Utils.findViewById(ViewGroup.class, this, R.id.pick_main);
        file_list = Utils.findViewById(RecyclerView.class, this, R.id.file_list);

    }


    private void loadedView() {
        OK_TEXT = TextUtils.isEmpty(OK_TEXT)?"确定":OK_TEXT;
        CANCEL_TEXT = TextUtils.isEmpty(CANCEL_TEXT)?"返回":CANCEL_TEXT;
        OK_COLOR = TextUtils.isEmpty(OK_COLOR)?"666666":OK_COLOR;
        CANCEL_COLOR = TextUtils.isEmpty(CANCEL_COLOR)?"666666":CANCEL_COLOR;
        BANNER_CORLOR = TextUtils.isEmpty(BANNER_CORLOR)?"ffffff":BANNER_CORLOR;
        getTextPx(MANAGER_TITLE_TEXT_SIZE,title_cancel);
        getTextPx(MANAGER_TITLE_TEXT_SIZE,title_ok);
        title_ok.setTextColor(Utils.getColorParcelable(OK_COLOR));
        title_cancel.setTextColor(Utils.getColorParcelable(CANCEL_COLOR));
        title_ok.setText(OK_TEXT);
        title_cancel.setText(CANCEL_TEXT);
        pick_main.setBackgroundColor(Utils.getColorParcelable(BANNER_CORLOR));
        GridLayoutManager manager = new GridLayoutManager(this,3);
        file_list.setLayoutManager(manager);
        ArrayList<ItemPhotoEntity> itemPhotoEntities = new ArrayList<>();
        for (int i=0;i<40;i++) itemPhotoEntities.add(new ItemPhotoEntity());
        file_list.setAdapter(new AlbumAdapter(this,itemPhotoEntities));
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


    @Override
    public void onClick(View v) {
        if (v.getId()==title_cancel.getId()){
            finish();
        }

        if (v.getId()==title_ok.getId()){

        }
    }
}

