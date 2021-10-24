package tellh.com.recyclertreeview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import tellh.com.recyclertreeview.bean.Dir
import tellh.com.recyclertreeview.bean.File
import tellh.com.recyclertreeview.viewbinder.DirectoryNodeBinder
import tellh.com.recyclertreeview.viewbinder.FileNodeBinder
import tellh.com.recyclertreeview_lib.TreeNode
import tellh.com.recyclertreeview_lib.TreeViewAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var adapter: TreeViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initData() {
        val nodes = mutableListOf<TreeNode<*>>()
        val app = TreeNode(Dir("app"))
        nodes.add(app)
        app.addChild(
                TreeNode(Dir("manifests"))
                        .addChild(TreeNode(File("AndroidManifest.xml")))
        )
        app.addChild(
                TreeNode(Dir("java"))
                        .addChild(TreeNode(Dir("tellh"))
                                .addChild(TreeNode(Dir("com"))
                                        .addChild(TreeNode(Dir("recyclertreeview"))
                                                .addChild(TreeNode(File("Dir")))
                                                .addChild(TreeNode(File("DirectoryNodeBinder")))
                                                .addChild(TreeNode(File("File")))
                                                .addChild(TreeNode(File("FileNodeBinder")))
                                                .addChild(TreeNode(File("TreeViewBinder")))
                                        )
                                )
                        )

        )
        val res = TreeNode(Dir("res"))
        nodes.add(res)

        res.addChild(
                TreeNode(Dir("layout")).lock()
                        .addChild(TreeNode(File("activity_main.xml")))
                        .addChild(TreeNode(File("item_dir.xml")))
                        .addChild(TreeNode(File("item_file.xml")))
        )
        res.addChild(
                TreeNode(Dir("mipmap"))
                        .addChild(TreeNode(File("ic_launcher.png")))
        )

        rv.layoutManager = LinearLayoutManager(this)
        adapter = TreeViewAdapter(nodes, listOf(FileNodeBinder(), DirectoryNodeBinder()))

        adapter.setOnTreeNodeListener(object: TreeViewAdapter.OnTreeNodeListener {
            override fun onClick(node: TreeNode<*>?, holder: RecyclerView.ViewHolder?): Boolean {
                if (node?.isLeaf() == false) {
                    //Update and toggle the node.
                    onToggle(!node.isExpand(), holder)
                }
                return false
            }

            override fun onToggle(isExpand: Boolean, holder: RecyclerView.ViewHolder?) {
                val dirViewHolder: DirectoryNodeBinder.ViewHolder = holder as DirectoryNodeBinder.ViewHolder //todo
                val ivArrow = dirViewHolder.ivArrow
                val rotateDegree = if (isExpand) 90f else -90f
                ivArrow.animate().rotationBy(rotateDegree)
                        .start()
            }
        })
        rv.adapter = adapter
    }

    private fun initView() {
        rv = findViewById(R.id.rv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.id_action_close_all -> adapter.collapseAll()
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }
}