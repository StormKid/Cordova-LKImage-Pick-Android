package pick.image.com.myapplication;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * 图片相册选择插件
 *
 * @author ke_li
 * @date 2017/11/6
 */
public class ImagePickActivity extends AppCompatActivity implements View.OnClickListener, AlbumAdapter.MyClickItemListerner {
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
    private String BANNER_CORLOR;
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
    private String OK_COLOR;

    /**
     * 查找相册
     */
    private final String ALBUM_TYPE = "ALBUM_TYPE";

    /**
     * 查找相片
     */
    private final String IMG_TYPE = "IMG_TYPE";

    /**
     * 记录状态更新
     */
    private String TYPE;

    /**
     * 新建数组来固定显示相册或者是相片
     */
    private ArrayList<ItemPhotoEntity> itemPhotoEntities = new ArrayList<>();

    /**
     * 限制能选择照相数量
     */
    private int LIMIT_NUM;

    /**
     * 是否直接请求网络
     */
    private boolean POST_IMGS;
    /**
     * 上传图片地址
     */
    private  String PATH_URL;

    /**
     * 上传图片其他参数
     */
    private Bundle params ;



    private final String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private final String[] iprojection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private AlbumAdapter albumAdapter;
    private MyTask task;
    private ProgressDialog progressDialog;
    private NetTask netTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        loadedView();
        initListner();
        initNet();

    }

    /**
     * 刷新网络参数,默认启用网络请求
     */
    private void initNet() {
       params = getIntent().getExtras();
       PATH_URL = getIntent().getStringExtra("path_url");
       POST_IMGS = getIntent().getBooleanExtra("post_imgs",true);
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
        BANNER_CORLOR = intent.getStringExtra("banner_color");
        MANAGER_TITLE_TEXT_SIZE = intent.getIntExtra("title_text_size", 10);
        LIMIT_NUM = intent.getIntExtra("limit_num", 9);
        TYPE = ALBUM_TYPE;
    }


    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setTitle("图片上传中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        progressDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        progressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        title_cancel = Utils.findViewById(TextView.class, this, R.id.title_cancel);
        title_ok = Utils.findViewById(TextView.class, this, R.id.title_ok);
        pick_main = Utils.findViewById(ViewGroup.class, this, R.id.pick_main);
        file_list = Utils.findViewById(RecyclerView.class, this, R.id.file_list);

    }


    private void loadedView() {
        OK_TEXT = TextUtils.isEmpty(OK_TEXT) ? "确定" : OK_TEXT;
        CANCEL_TEXT = TextUtils.isEmpty(CANCEL_TEXT) ? "返回" : CANCEL_TEXT;
        OK_COLOR = TextUtils.isEmpty(OK_COLOR) ? "666666" : OK_COLOR;
        CANCEL_COLOR = TextUtils.isEmpty(CANCEL_COLOR) ? "666666" : CANCEL_COLOR;
        BANNER_CORLOR = TextUtils.isEmpty(BANNER_CORLOR) ? "ffffff" : BANNER_CORLOR;
        CHCEK_IMG_RES = TextUtils.isEmpty(CHCEK_IMG_RES)?"":CHCEK_IMG_RES;
        getTextPx(MANAGER_TITLE_TEXT_SIZE, title_cancel);
        getTextPx(MANAGER_TITLE_TEXT_SIZE, title_ok);
        title_ok.setTextColor(Utils.getColorParcelable(OK_COLOR));
        title_cancel.setTextColor(Utils.getColorParcelable(CANCEL_COLOR));
        title_ok.setText(OK_TEXT);
        title_cancel.setText(CANCEL_TEXT);
        pick_main.setBackgroundColor(Utils.getColorParcelable(BANNER_CORLOR));
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        file_list.setLayoutManager(manager);
        albumAdapter = new AlbumAdapter(this, itemPhotoEntities,CHCEK_IMG_RES);
        file_list.setAdapter(albumAdapter);
        albumAdapter.setListerner(this);
        task = new MyTask();
        task.execute(TYPE);
    }

    /**
     * 获取字体大小
     *
     * @param point
     * @return
     */
    private void getTextPx(int point, TextView textView) {
        int windowWidth = Utils.getWindowWidth(this);
        int px = windowWidth * point / 240;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, px);
    }

    private List<String> results = new ArrayList<>();

    @Override
    public void onClick(View v) {
        if (v.getId() == title_cancel.getId()) {
            switch (TYPE) {
                case ALBUM_TYPE:
                    finish();
                    break;
                case IMG_TYPE:
                    this.TYPE = ALBUM_TYPE;
                    task = new MyTask();
                    task.execute(TYPE);
                    break;
            }
        }

        if (v.getId() == title_ok.getId()) {
            switch (TYPE) {
                case ALBUM_TYPE:
                    Toast.makeText(this, "请进入相册选择您需要上传的图片", Toast.LENGTH_SHORT).show();
                    break;
                case IMG_TYPE:
                    //获取相片URL
                    setResults();
                    break;
            }
        }
    }

    @Override
    public void onClick(String name) {
        TYPE = IMG_TYPE;
        task = new MyTask();
        task.setAlbum(name);
        task.execute(TYPE);
    }

    /**
     * 获取最终数据
     */
    private void setResults() {
        if (results != null && results.size() > 0) results.clear();
        for (ItemPhotoEntity entity :
                itemPhotoEntities) {
            if (entity.isChecked()) {
                results.add(entity.getPath());
            }
        }
        if (results.size()>LIMIT_NUM) {Toast.makeText(this,"您最多只能选择"+LIMIT_NUM+"张图片",Toast.LENGTH_SHORT).show();
            return;
        }else {//上传图片
            netTask = new NetTask();
            netTask.execute();
        }
    }


    /**
     * 后台默认执行Task来完成查找相册与查看相片
     * 在调用此tast的时候必须要验证权限
     */
    private class MyTask extends AsyncTask<String, Integer, ArrayList<ItemPhotoEntity>> {


        private String album;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //执行完毕获取数据
        @Override
        protected void onPostExecute(ArrayList<ItemPhotoEntity> items) {
            super.onPostExecute(items);

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
            switch (TAG) {
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
                            file = new File(image);
                            if (file.exists() && !albumSet.contains(album)) {
                                ItemPhotoEntity itemPhotoEntity = new ItemPhotoEntity();
                                itemPhotoEntity.setName(album);
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
                    if (this.isCancelled()) {
                        break;
                    }
                    Cursor icursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, iprojection,
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{album}, MediaStore.Images.Media.DATE_ADDED);

                    if (icursor == null) {
                        break;
                    }
                    ArrayList<ItemPhotoEntity> tempi = new ArrayList<>(icursor.getCount());

                    if (icursor.moveToLast()) {
                        do {
                            if (this.isCancelled()) {
                                break;
                            }
                            long id = icursor.getLong(icursor.getColumnIndex(iprojection[0]));
                            String name = icursor.getString(icursor.getColumnIndex(iprojection[1]));
                            String path = icursor.getString(icursor.getColumnIndex(iprojection[2]));
                            file = new File(path);
                            if (file.exists()) {
                                ItemPhotoEntity photoEntity = new ItemPhotoEntity();
                                photoEntity.setId(id);
                                photoEntity.setName(name);
                                photoEntity.setPath(path);
                                photoEntity.setType(TAG);
                                tempi.add(photoEntity);
                            }

                        } while (icursor.moveToPrevious());
                    }
                    icursor.close();

                    itemPhotoEntities.clear();
                    itemPhotoEntities.addAll(tempi);

                    break;
            }
            return itemPhotoEntities;
        }


        public void setAlbum(String album) {
            this.album = album;
        }
    }

    @Override
    protected void onDestroy() {
        if (task != null && task.isCancelled()) {
            //及时结束异步任务
            task.cancel(true);
            task = null;
        }
        if (netTask !=null && netTask.isCancelled()){
            netTask.cancel(true);
            netTask = null;
        }
        super.onDestroy();
    }

    //////////////////////////////////////////网络请求来上传文件///////////////////////////////////////////////////

    private  class  NetTask extends  AsyncTask<String,Integer,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.e("http",s+"");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
            progressDialog.setProgress(progress);
        }

        @Override
        protected String doInBackground(String... strings) {
            String[] temp = new String[results.size()];
            String[] paths = results.toArray(temp);
            Log.e("http",paths.length+"");
            PATH_URL = "http://172.16.32.128:8080/sns/app/file/uploads";
            String rep = Utils.uploadFile(PATH_URL, paths);
            return rep;
        }
    }

}

