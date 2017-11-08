package pick.image.com.myapplication;

import java.io.Serializable;

/**
 * Created by ke_li on 2017/11/8.
 */

public class ItemPhotoEntity implements Serializable{

    private int Type ;// 0为相册 ，1 为相册list
    private String name;
    private String url;
    private boolean isChecked;


    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
