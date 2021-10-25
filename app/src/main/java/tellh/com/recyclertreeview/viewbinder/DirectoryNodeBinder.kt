package tellh.com.recyclertreeview.viewbinder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import tellh.com.recyclertreeview.R
import tellh.com.recyclertreeview.bean.Dir
import tellh.com.recyclertreeview_lib.TreeNode
import tellh.com.recyclertreeview_lib.TreeViewBinder

/**
 * Created by tlh on 2016/10/1 :)
 */

class DirectoryNodeBinder : TreeViewBinder<DirectoryNodeBinder.ViewHolder>() {

    override fun provideViewHolder(itemView: View): ViewHolder {
        return ViewHolder(itemView)
    }

    override fun <VH> bindView(holder: VH, position: Int, node: TreeNode<*>?) {
        with(holder as ViewHolder) {
            holder.ivArrow.rotation = 0f
            holder.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_right_black_18dp)
            holder.ivArrow.rotation = if (node?.isExpand() == true) 90f else 0f
            val dirNode = (node?.content as? Dir)
            holder.tvName.text = dirNode?.dirName
            holder.ivArrow.visibility = if (node?.isLeaf() == true) View.INVISIBLE else View.VISIBLE
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.item_dir
    }

    open class ViewHolder(rootView: View) : TreeViewBinder.ViewHolder(rootView) {
        val ivArrow: ImageView = rootView.findViewById(R.id.iv_arrow)
        val tvName: TextView = rootView.findViewById(R.id.tv_name)
    }

}