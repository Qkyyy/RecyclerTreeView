package tellh.com.recyclertreeview.viewbinder;

import android.view.View;
import android.widget.TextView;

import tellh.com.recyclertreeview.R;
import tellh.com.recyclertreeview.bean.File;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewBinder;

/**
 * Created by tlh on 2016/10/1 :)
 */

class FileNodeBinder : TreeViewBinder<FileNodeBinder.ViewHolder>() {

    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun <VH> bindView(holder: VH, position: Int, node: TreeNode<*>?) {
        with (holder as ViewHolder) {
            holder.tvName.text = (node?.getContent() as? File)?.fileName
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_file
    }

    class ViewHolder(rootView: View): TreeViewBinder.ViewHolder(rootView) {
        val tvName: TextView = rootView.findViewById(R.id.tv_name)
    }
}