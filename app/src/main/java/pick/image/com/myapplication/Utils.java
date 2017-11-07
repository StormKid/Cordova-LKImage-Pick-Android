package pick.image.com.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

/**
 * Created by ke_li on 2017/11/7.
 */

public class Utils {
    /**
     * 查找id
     * @param t
     * @param activity
     * @param id
     * @param <T>
     * @return
     */
    public  static <T>T findViewById(Class<T> t, Activity activity, @IdRes int id){
        View view = activity.findViewById(android.R.id.content);
        return  (T)view.findViewById(id);
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return
     */
    public  static int  getWindowWidth(Context context){
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        return widthPixels;
    }

    /**
     * 通过地址获取图片
     * @param context
     * @param url
     * @param imageview
     */
    public static void putImg(Context context ,String url, ImageView imageview){
        Glide.with(context).asBitmap().load(url).into(imageview);
    }
}
