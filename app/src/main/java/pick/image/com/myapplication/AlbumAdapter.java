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
import com.bumptech.glide.Glide;

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
    private int gridWidth;
    /**
     * 查找相册
     */
    private final String ALBUM_TYPE="ALBUM_TYPE";

    /**
     * 查找相片
     */
    private final String IMG_TYPE = "IMG_TYPE";
    public AlbumAdapter(Context context, List<ItemPhotoEntity> entityList) {
        this.context = context;
        this.entityList = entityList;
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
        final String type = itemPhotoEntity.getType();
        boolean checked = itemPhotoEntity.isChecked();
        final String name = itemPhotoEntity.getName();
        String path = itemPhotoEntity.getPath();
        if (checked) holder.check_box.setVisibility(View.VISIBLE);
        else holder.check_box.setVisibility(View.GONE);
        holder.itemView.setTag(checked);
        caculateItem();
        LinearLayout.LayoutParams itemParams =  new LinearLayout.LayoutParams(ItemWidth,ItemHeight);
        RelativeLayout.LayoutParams checkParams = new RelativeLayout.LayoutParams(checkWidth,checkHeight);
        RelativeLayout.LayoutParams gridParams = new RelativeLayout.LayoutParams(gridWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        checkParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        checkParams.setMargins(0,margin_size,margin_size,0);
        holder.check_box.setLayoutParams(checkParams);
        holder.item_img.setLayoutParams(itemParams);
        if (type.equals(ALBUM_TYPE))
        holder.item_img.setImageResource(R.mipmap.album);
        else {
            Glide.with(context).load(path).into(holder.item_img);
        }
        holder.check_box.setImageResource(R.mipmap.choose);
        holder.item_name.setText(name);
        holder.item_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,itemTextSize);
        holder.item_contain.setLayoutParams(gridParams);
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
                        if (listerner!=null)listerner.onClick(name);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityList==null?0:entityList.size();
    }

    public void setListerner(MyClickItemListerner listerner) {
        this.listerner = listerner;
    }

    class  ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView item_img;
        private final ImageView check_box;
        private final TextView item_name;
        private final RelativeLayout item_contain;

        public ViewHolder(View itemView) {
            super(itemView);
            item_img = Utils.findViewById(ImageView.class, itemView, R.id.item_img);
            check_box = Utils.findViewById(ImageView.class, itemView, R.id.check_box);
            item_name = Utils.findViewById(TextView.class, itemView, R.id.item_name);
            item_contain = Utils.findViewById(RelativeLayout.class, itemView, R.id.item_contain);

        }
    }


    private void  caculateItem(){
        int windowWidth = Utils.getWindowWidth(context);
        ItemHeight = ItemWidth = windowWidth*80/300;
        checkHeight = checkWidth = windowWidth/20;
        margin_size = windowWidth/35;
        itemTextSize = windowWidth/26;
        gridWidth = windowWidth/3;
    }


    private MyClickItemListerner listerner;


    public interface MyClickItemListerner{
        void onClick(String name);
    }


}