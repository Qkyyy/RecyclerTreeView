package tellh.com.recyclertreeview.bean;

import tellh.com.recyclertreeview.R;
import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

class Dir(var dirName: String) : LayoutItemType {

    override fun getLayoutId(): Int {
        return R.layout.item_dir
    }
}
