package tellh.com.recyclertreeview_lib

import kotlin.jvm.Throws

/**
 * Created by tlh on 2016/10/1 :)
 */

class TreeNode<T : LayoutItemType?>(content: T) : Cloneable {
    var content: T = content
    var parent: TreeNode<*>? = null

    private var childList = mutableListOf<TreeNode<*>>()
    private var isExpand = false
    private var isLocked = false
    private var height = UNDEFINE

    fun getHeight(): Int = if (isRoot()) 0 else if (height == UNDEFINE) parent!!.getHeight() + 1 else height
    fun getChildList() = childList

    fun isRoot(): Boolean = parent == null
    fun isLeaf(): Boolean = childList.isEmpty()
    fun isExpand() = isExpand
    fun isLocked() = isLocked

    override fun toString() = "TreeNode{content=$content,parent = ${parent?.content?.toString() ?: "null"}, childList = $childList, isExpand = $isExpand}"

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): TreeNode<T> {
        val clone = TreeNode(content)
        clone.isExpand = isExpand
        return clone
    }

    companion object {
        private const val UNDEFINE = -1
    }

    fun setChildList(childList: List<TreeNode<*>>) {
        this.childList.clear()
        childList.forEach { addChild(it) }
    }

    fun addChild(node: TreeNode<*>): TreeNode<*> {
        childList.add(node)
        node.parent = this
        return this
    }

    fun toggle(): Boolean {
        isExpand = !isExpand
        return isExpand
    }

    fun collapse() {
        if (isExpand)
            isExpand = false
    }

    fun collapseAll() {
        collapse()
        childList.takeIf { children -> children.isNotEmpty() }?.let { children ->
            children.forEach { child -> child.collapseAll() }
        }
    }

    fun expand() {
        if (!isExpand) {
            isExpand = true
        }
    }

    fun expandAll() {
        expand()
        childList.takeIf { children -> children.isNotEmpty() }?.let { children ->
            children.forEach { child -> child.expandAll() }
        }
    }

    fun lock(): TreeNode<T> {
        isLocked = true
        return this
    }

    fun unlock(): TreeNode<T> {
        isLocked = false
        return this
    }
}