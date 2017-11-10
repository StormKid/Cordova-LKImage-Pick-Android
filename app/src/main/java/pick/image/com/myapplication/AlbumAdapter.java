package pick.image.com.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * 相册Adapter
 * Created by ke_li on 2017/11/7.
 */

public  class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    private Context context;
    private int ItemWidth;
    private int ItemHeight;
    private int checkWidth;
    private int checkHeight;
    private int margin_size;
    private int itemTextSize;
    private List<ItemPhotoEntity> entityList;
    private String type;
    /**
     * 查找相册
     */
    private final String ALBUM_TYPE="ALBUM_TYPE";

    /**
     * 查找相片
     */
    private final String IMG_TYPE = "IMG_TYPE";
    public AlbumAdapter(Context context, List<ItemPhotoEntity> entityList,String type) {
        this.context = context;
        this.entityList = entityList;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_main,parent,false);
        return new ViewHolder(inflate);
    }

    public  void  update(List<ItemPhotoEntity> entityList){
        this.entityList = entityList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ItemPhotoEntity itemPhotoEntity = entityList.get(position);
        boolean checked = itemPhotoEntity.isChecked();
        if (checked) holder.check_box.setVisibility(View.VISIBLE);
        else holder.check_box.setVisibility(View.GONE);
        holder.itemView.setTag(checked);
        caculateItem();
        LinearLayout.LayoutParams itemParams =  new LinearLayout.LayoutParams(ItemWidth,ItemHeight);
        RelativeLayout.LayoutParams checkParams = new RelativeLayout.LayoutParams(checkWidth,checkHeight);
        checkParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        checkParams.setMargins(0,margin_size,margin_size,0);
        holder.check_box.setLayoutParams(checkParams);
        holder.item_img.setLayoutParams(itemParams);
        holder.item_img.setImageResource(R.mipmap.ic_launcher);
        holder.check_box.setImageResource(R.mipmap.choose);
        holder.item_name.setText(itemPhotoEntity.getName());
        holder.item_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,itemTextSize);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case IMG_TYPE:
                        boolean tag = v.getTag() == null ? false : (boolean) v.getTag();
                        if (tag) {
                            tag = false;
                            v.setTag(tag);
                            itemPhotoEntity.setChecked(tag);
                        } else {
                            tag = true;
                            v.setTag(tag);
                            itemPhotoEntity.setChecked(tag);
                        }
                        entityList.set(position, itemPhotoEntity);

                        notifyDataSetChanged();
                        break;
                    case ALBUM_TYPE:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityList==null?0:entityList.size();
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
        margin_size = windowWidth/35;
        itemTextSize = windowWidth/26;
    }


}