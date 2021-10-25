package tellh.com.recyclertreeview_lib;

import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import android.view.View


abstract class TreeViewBinder<VH> : LayoutItemType where VH : RecyclerView.ViewHolder {
    abstract fun provideViewHolder(itemView: View): VH
    abstract fun <VH> bindView(holder: VH, position: Int, node: TreeNode<*>?)
    open class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        protected fun <T : View?> findViewById(@IdRes id: Int): T {
            return (itemView.findViewById<View>(id) as T)
        }
    }
}