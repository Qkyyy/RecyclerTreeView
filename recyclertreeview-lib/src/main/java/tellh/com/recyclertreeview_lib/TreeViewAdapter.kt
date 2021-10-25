package tellh.com.recyclertreeview_lib

import android.os.Build
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.UnsupportedOperationException

/**
 * Created by tlh on 2016/10/1 :)
 */
class TreeViewAdapter(nodes: List<TreeNode<*>>? = null, private val viewBinders: List<TreeViewBinder<*>>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private val displayNodes = mutableListOf<TreeNode<*>>()
    private var padding = 30
    private var onTreeNodeListener: OnTreeNodeListener? = null
    private var collapseChildWhenParentIsCollapsed = false

    companion object {
        private const val KEY_IS_EXPAND = "IS_EXPAND"
    }

    init {
        nodes?.let { findDisplayNodes(it) }
    }

    private fun findDisplayNodes(nodes: List<TreeNode<*>>) {
        nodes.forEach { node ->
            displayNodes.add(node)
            if (!node.isLeaf() && node.isExpand())
                findDisplayNodes(node.getChildList())
        }
    }

    override fun getItemViewType(position: Int): Int {
        return displayNodes[position].content?.getLayoutId()
                ?: throw UnsupportedOperationException("content should not be null before trying to get the item view type")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        if (viewBinders.size == 1)
            return viewBinders[0].provideViewHolder(view)
        viewBinders.forEach { viewBinder ->
            if (viewBinder.getLayoutId() == viewType) return viewBinder.provideViewHolder(view)
        }
        return viewBinders[0].provideViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: List<Any>?) {
        (payloads?.elementAtOrNull(0) as? Bundle)?.let { bundle ->
            for (key in bundle.keySet()) {
                when (key) {
                    KEY_IS_EXPAND -> onTreeNodeListener?.onToggle(bundle.getBoolean(key), holder)
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            holder?.itemView?.setPaddingRelative(displayNodes[position].getHeight() * padding, 3, 3, 3)
        else
            holder?.itemView?.setPadding(displayNodes[position].getHeight() * padding, 3, 3, 3)

        holder?.itemView?.setOnClickListener {
            val selectedNode = displayNodes[holder.layoutPosition]
            kotlin.runCatching {
                val lastClickTime = holder.itemView.tag as Long
                if (System.currentTimeMillis() - lastClickTime < 500) return@setOnClickListener
            }.onFailure { holder.itemView.tag = System.currentTimeMillis() }
            holder.itemView.tag = System.currentTimeMillis()
            if (onTreeNodeListener?.onClick(selectedNode, holder) == true) return@setOnClickListener
            if (selectedNode.isLeaf() || selectedNode.isLocked()) return@setOnClickListener
            val positionStart = displayNodes.indexOf(selectedNode) + 1
            if (!selectedNode.isExpand()) notifyItemRangeInserted(positionStart, addChildNodes(selectedNode, positionStart))
            else notifyItemRangeRemoved(positionStart, removeChildNodes(selectedNode, true))
        }
        viewBinders.forEach {
            if (it.getLayoutId() == displayNodes[position].content?.getLayoutId()) it.bindView(holder, position, displayNodes[position])
        }
    }

    private fun addChildNodes(pNode: TreeNode<*>, startIndex: Int): Int {
        val childList = pNode.getChildList()
        var addChildCount = 0
        for (treeNode in childList) {
            displayNodes.add(startIndex + addChildCount++, treeNode)
            if (treeNode.isExpand()) {
                addChildCount += addChildNodes(treeNode, startIndex + addChildCount)
            }
        }
        if (!pNode.isExpand()) pNode.toggle()
        return addChildCount
    }

    private fun removeChildNodes(pNode: TreeNode<*>, shouldToggle: Boolean = true): Int {
        if (pNode.isLeaf()) return 0
        val childList = pNode.getChildList()
        var removeChildCount = childList.size
        displayNodes.removeAll(childList)
        for (child in childList) {
            if (child.isExpand()) {
                if (collapseChildWhenParentIsCollapsed) child.toggle()
                removeChildCount += removeChildNodes(child, false)
            }
        }
        if (shouldToggle) pNode.toggle()
        return removeChildCount
    }

    override fun getItemCount() = displayNodes.size

    fun setPadding(padding: Int) {
        this.padding = padding
    }

    fun collapseChildWhenParentIsCollapsed(value: Boolean) {
        this.collapseChildWhenParentIsCollapsed = value
    }

    fun setOnTreeNodeListener(onTreeNodeListener: OnTreeNodeListener?) {
        this.onTreeNodeListener = onTreeNodeListener
    }

    interface OnTreeNodeListener {
        fun onClick(node: TreeNode<*>?, holder: RecyclerView.ViewHolder?): Boolean
        fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder?)
    }

    fun refresh(treeNodes: List<TreeNode<*>>) {
        displayNodes.clear()
        findDisplayNodes(treeNodes)
        notifyDataSetChanged()
    }

    fun getDisplayNodesIterator() = displayNodes.iterator()

    private fun notifyDiff(temp: List<TreeNode<*>>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return temp.size
            }

            override fun getNewListSize(): Int {
                return displayNodes.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return this@TreeViewAdapter.areItemsTheSame(temp[oldItemPosition], displayNodes[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return this@TreeViewAdapter.areContentsTheSame(temp[oldItemPosition], displayNodes[newItemPosition])
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return this@TreeViewAdapter.getChangePayload(temp[oldItemPosition], displayNodes[newItemPosition])
            }
        })
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getChangePayload(oldNode: TreeNode<*>, newNode: TreeNode<*>): Any? {
        Bundle().let { diffBundle ->
            if (newNode.isExpand() != oldNode.isExpand())
                diffBundle.putBoolean(KEY_IS_EXPAND, newNode.isExpand())
            return if (diffBundle.size() == 0) null else diffBundle
        }
    }

    private fun areContentsTheSame(oldNode: TreeNode<*>, newNode: TreeNode<*>): Boolean {
        return oldNode.content != null && oldNode.content == newNode.content && oldNode.isExpand() == newNode.isExpand()
    }

    private fun areItemsTheSame(oldNode: TreeNode<*>, newNode: TreeNode<*>): Boolean {
        return oldNode.content != null && oldNode.content == newNode.content
    }

    fun collapseAll() {
        val temp = backupDisplayNodes()
        val roots = mutableListOf<TreeNode<*>>()

        displayNodes.forEach { if (it.isRoot()) roots.add(it) }
        roots.forEach { if (it.isExpand()) removeChildNodes(it) }

        notifyDiff(temp)
    }

    private fun backupDisplayNodes(): List<TreeNode<*>> {
        val temp = mutableListOf<TreeNode<*>>()
        displayNodes.forEach { displayNode ->
            kotlin.runCatching { temp.add(displayNode.clone()) }
                    .onFailure { temp.add(displayNode) }
        }
        return temp
    }

    fun collapseNode(pNode: TreeNode<*>) {
        val temp = backupDisplayNodes()
        removeChildNodes(pNode)
        notifyDiff(temp)
    }

    fun collapseBrotherNode(pNode: TreeNode<*>) {
        val temp = backupDisplayNodes()
        if (pNode.isRoot()) {
            val roots = mutableListOf<TreeNode<*>>()
            displayNodes.forEach { if (it.isRoot()) roots.add(it) }
            //Close all root nodes.
            roots.forEach { if (it.isExpand() && it != pNode) removeChildNodes(it) }
        } else {
            val parent = pNode.parent ?: return
            val childList = parent.getChildList()
            for (node in childList) {
                if (node == pNode || !node.isExpand()) continue
                removeChildNodes(node)
            }
        }
        notifyDiff(temp)
    }
}