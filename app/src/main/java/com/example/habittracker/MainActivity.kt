package com.example.habittracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.recyclerView.Adapter
import com.example.habittracker.models.Item
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var itemList: MutableList<Item>? = null

    private val TAG = "main_activity"

    private val addItemLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            val data = result.data
            val item = data?.getParcelableExtra<Item>("item")
            Log.i(TAG, "$item")
            item?.let {
                itemList!!.add(it)
                recyclerView?.adapter?.notifyItemInserted(itemList!!.size - 1)
            }
        }
    }

    private val editItemLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val item = data?.getParcelableExtra<Item>("item")
            Log.i(TAG, "$item")
            item?.let { it ->
                val position = itemList!!.indexOfFirst {
                    Log.i(TAG, "${it.id}")
                    it.id == item.id
                }
                if (position != -1) {
                    itemList!![position] = it
                    recyclerView?.adapter?.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemList = savedInstanceState?.getParcelableArrayList("itemList") ?: mutableListOf()

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
        recyclerView?.adapter = Adapter(itemList!!) { item: Item -> this.onItemClick(item) }
        recyclerView?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, DataChangeActivity::class.java)
            addItemLauncher.launch(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("itemList", itemList?.let { ArrayList(it) })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        itemList = savedInstanceState.getParcelableArrayList("itemList") ?: mutableListOf()
        recyclerView?.adapter = Adapter(itemList!!) { item: Item -> this.onItemClick(item) }
    }

    private fun onItemClick(item: Item) {
        val intent = Intent(this@MainActivity, DataChangeActivity::class.java)
        intent.putExtra("item", item)
        Log.i(TAG, "${intent.getParcelableExtra<Item>("item")}")
        Log.i(TAG, "Intent extras: ${intent.extras}")
        editItemLauncher.launch(intent)
    }
}