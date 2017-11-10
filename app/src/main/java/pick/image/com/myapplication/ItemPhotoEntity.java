package pick.image.com.myapplication;

import java.io.Serializable;

/**
 * Created by ke_li on 2017/11/8.
 */

public class ItemPhotoEntity implements Serializable{

    private String Type ;
    private long id;
    private String name;
    private String path;
    private boolean isChecked;


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
