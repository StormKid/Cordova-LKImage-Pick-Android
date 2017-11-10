package pick.image.com.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

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
     * 查找相册
     */
    private final String ALBUM_TYPE="ALBUM_TYPE";

    /**
     * 查找相片
     */
    private final String IMG_TYPE = "IMG_TYPE";

    /**
     * 记录状态更新
     */
    private  String TYPE;

    /**
     * 新建数组来固定显示相册或者是相片
     */
    private ArrayList<ItemPhotoEntity> itemPhotoEntities = new ArrayList<>();

    private final String[] projection = new String[]{ MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA };
    private AlbumAdapter albumAdapter;

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
        TYPE = ALBUM_TYPE;
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
        albumAdapter = new AlbumAdapter(this, itemPhotoEntities,ALBUM_TYPE);
        file_list.setAdapter(albumAdapter);
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
            switch (TYPE){
                case ALBUM_TYPE:
                    finish();
                    break;
                case IMG_TYPE:
                    this.TYPE = ALBUM_TYPE;
                    break;
            }
        }

        if (v.getId()==title_ok.getId()){
            MyTask task = new MyTask();
            task.execute(TYPE);
        }
    }

    /**
     * 后台默认执行Task来完成查找相册与查看相片
     */
    private class  MyTask extends AsyncTask<String,Integer,ArrayList<ItemPhotoEntity>>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        //执行完毕获取数据
        @Override
        protected void onPostExecute(ArrayList<ItemPhotoEntity> items) {
            super.onPostExecute(items);
            Log.e("items",items.size()+"");
            albumAdapter.update(items);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        //执行中判断string是相册还是查相片
        @Override
        protected ArrayList<ItemPhotoEntity> doInBackground(String... strings) {
            String TAG = strings[0];
            switch (TAG){
                case ALBUM_TYPE:
                    if (this.isCancelled()) {
                        break;
                    }
                    Cursor cursor = getApplicationContext().getContentResolver()
                            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                                    null, null, MediaStore.Images.Media.DATE_ADDED);
                    if (cursor == null) {
                        break;
                    }

                    ArrayList<ItemPhotoEntity> temp = new ArrayList<>(cursor.getCount());
                    HashSet<String> albumSet = new HashSet<>();
                    File file;

                    if (cursor.moveToLast()) {
                        do {
                            if (Thread.interrupted()) {
                                break;
                            }

                            String album = cursor.getString(cursor.getColumnIndex(projection[0]));
                            String image = cursor.getString(cursor.getColumnIndex(projection[1]));

                    /*
                    It may happen that some image file paths are still present in cache,
                    though image file does not exist. These last as long as media
                    scanner is not run again. To avoid get such image file paths, check
                    if image file exists.
                     */
                            file = new File(image);
                            if (file.exists() && !albumSet.contains(album)) {
                                ItemPhotoEntity itemPhotoEntity = new ItemPhotoEntity();
                                itemPhotoEntity.setName(album);
                                itemPhotoEntity.setPath(image);
                                itemPhotoEntity.setType(TAG);
                                temp.add(itemPhotoEntity);
                                albumSet.add(album);
                            }

                        } while (cursor.moveToPrevious());
                    }
                    cursor.close();

                    itemPhotoEntities.clear();
                    itemPhotoEntities.addAll(temp);
                    break;
                case IMG_TYPE:
                    break;
            }
            return itemPhotoEntities;
        }
    }










}

