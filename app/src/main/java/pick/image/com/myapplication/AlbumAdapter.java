package pick.image.com.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 相册Adapter
 * Created by ke_li on 2017/11/7.
 */

public  class AlbumAdapter extends RecyclerView.Adapter<ImagePickActivity.ViewHolder> {


    private Context context;

    @Override
    public ImagePickActivity.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_main,parent,false);
        return null;
    }

    @Override
    public void onBindViewHolder(ImagePickActivity.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}