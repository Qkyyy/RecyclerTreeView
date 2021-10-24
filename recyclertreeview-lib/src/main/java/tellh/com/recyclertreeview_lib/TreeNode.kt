package tellh.com.recyclertreeview_lib

import java.util.*

/**
 * Created by tlh on 2016/10/1 :)
 */

class TreeNode<T : LayoutItemType?>(content: T) : Cloneable {
    private var content: T
    private var parent: TreeNode<*>? = null
    private var childList: MutableList<TreeNode<*>>?
    private var isExpand = false
    private var isLocked = false

    //the tree high
    private var height = UNDEFINE
    fun getHeight(): Int {
        if (isRoot()) height = 0 else if (height == UNDEFINE) height = parent!!.getHeight() + 1
        return height
    }

    fun isRoot(): Boolean {
        return parent == null
    }

    fun isLeaf(): Boolean {
        return childList == null || childList!!.isEmpty()
    }

    fun setContent(content: T) {
        this.content = content
    }

    fun getContent(): T {
        return content
    }

    fun getChildList(): kotlin.collections.List<TreeNode<*>>? {
        return childList
    }

    fun setChildList(childList: kotlin.collections.List<TreeNode<*>>) {
        this.childList!!.clear()
        for (treeNode in childList) {
            addChild(treeNode)
        }
    }

    fun addChild(node: TreeNode<*>): TreeNode<*> {
        if (childList == null) childList = ArrayList()
        childList!!.add(node)
        node.parent = this
        return this
    }

    fun toggle(): Boolean {
        isExpand = !isExpand
        return isExpand
    }

    fun collapse() {
        if (isExpand) {
            isExpand = false
        }
    }

    fun collapseAll() {
        if (childList == null || childList!!.isEmpty()) {
            return
        }
        for (child in childList!!) {
            child.collapseAll()
        }
    }

    fun expand() {
        if (!isExpand) {
            isExpand = true
        }
    }

    fun expandAll() {
        expand()
        if (childList == null || childList!!.isEmpty()) {
            return
        }
        for (child in childList!!) {
            child.expandAll()
        }
    }

    fun isExpand(): Boolean {
        return isExpand
    }

    fun setParent(parent: TreeNode<*>?) {
        this.parent = parent
    }

    fun getParent(): TreeNode<*>? {
        return parent
    }

    fun lock(): TreeNode<T> {
        isLocked = true
        return this
    }

    fun unlock(): TreeNode<T> {
        isLocked = false
        return this
    }

    fun isLocked(): Boolean {
        return isLocked
    }

    override fun toString(): String {
        return "TreeNode{" +
                "content=" + content +
                ", parent=" + (if (parent == null) "null" else parent!!.getContent().toString()) +
                ", childList=" + (if (childList == null) "null" else childList.toString()) +
                ", isExpand=" + isExpand +
                '}'
    }

    @kotlin.Throws(CloneNotSupportedException::class)
    public override fun clone(): TreeNode<T> {
        val clone = TreeNode(content)
        clone.isExpand = isExpand
        return clone
    }

    companion object {
        private const val UNDEFINE = -1
    }

    init {
        this.content = content
        childList = ArrayList()
    }
}
