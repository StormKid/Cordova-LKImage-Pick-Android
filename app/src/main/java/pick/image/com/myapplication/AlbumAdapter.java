package pick.image.com.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 相册Adapter
 * Created by ke_li on 2017/11/7.
 */

public  class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    private Context context;
    private int ITEM_SIZE;
    private int ItemWidth;
    private int ItemHeight;
    private int checkWidth;
    private int checkHeight;
    private int margin_size;
    private List<ItemPhotoEntity> entityList;

    public AlbumAdapter(Context context, int item_size, List<ItemPhotoEntity> entityList) {
        this.context = context;
        ITEM_SIZE = item_size;
        this.entityList = entityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_main,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        caculateItem();
        RelativeLayout.LayoutParams itemParams =  new RelativeLayout.LayoutParams(ItemWidth,ItemHeight);
        RelativeLayout.LayoutParams checkParams = new RelativeLayout.LayoutParams(checkWidth,checkHeight);
        checkParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        checkParams.setMargins(0,margin_size,margin_size,0);
        holder.check_box.setLayoutParams(checkParams);
        holder.item_img.setLayoutParams(itemParams);
        holder.item_img.setImageResource(R.mipmap.ic_launcher);
        holder.check_box.setImageResource(R.mipmap.choose);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class  ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView item_img;
        private final ImageView check_box;
        private final TextView item_name;

        public ViewHolder(View itemView) {
            super(itemView);
            item_img = Utils.findViewById(ImageView.class, itemView, R.id.item_img);
            check_box = Utils.findViewById(ImageView.class, itemView, R.id.check_box);
            item_name = Utils.findViewById(TextView.class, itemView, R.id.item_name);

        }
    }


    private void  caculateItem(){
        int windowWidth = Utils.getWindowWidth(context);
        ItemHeight = ItemWidth = windowWidth*80/250;
        checkHeight = checkWidth = windowWidth/20;
        ITEM_SIZE = ITEM_SIZE==0? windowWidth/25:ITEM_SIZE/240;
        margin_size = windowWidth/35;
    }


}