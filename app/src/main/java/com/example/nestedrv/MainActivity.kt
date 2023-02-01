package com.example.nestedrv

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var nestedScrollView: MxNested
    private lateinit var recyclerView: RecyclerView
    private lateinit var view1: View
    private lateinit var view2: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nestedScrollView = findViewById(R.id.nestedScrollView)
        recyclerView = findViewById(R.id.recyclerView)
        view1 = findViewById(R.id.view1)
        view2 = findViewById(R.id.view2)

        val mAdapter = TestAdapter(recyclerView).apply {
            loadMoreListener = object : OnLoadMoreListener {
                override fun onLoadMore() {
                    addData {
                        val startIndex = data.size
                        data.addAll(it)
                        notifyItemRangeInserted(startIndex, it.size)
                    }
                }
            }
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }

        val rootView = findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                nestedScrollView.mParentScrollHeight = view1.height
                val rvNewHeight = rootView.height - view2.height
                recyclerView.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, rvNewHeight
                )
            }
        })

        addData {
            mAdapter.data.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun addData(complete: (List<String>) -> Unit) {
        launch {
            delay(2000)
            complete(
                mutableListOf<String>().apply {
                    repeat(20) {
                        add("data $it")
                    }
                }
            )
        }
    }

    inner class TestAdapter(
        private val rv: RecyclerView
    ): RecyclerView.Adapter<TestViewHolder>() {

        var loadMoreListener: OnLoadMoreListener? = null

        val data = mutableListOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
            return TestViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false))
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
            val p = rv.getChildAdapterPosition(holder.itemView)
            holder.textView.text = data[position]
            if (p == data.size - 1) {
                loadMoreListener?.onLoadMore()
            }
        }
    }

    inner class TestViewHolder constructor(view: View): RecyclerView.ViewHolder(view) {

        val textView: TextView = itemView.findViewById(R.id.tv)
    }
}

interface OnLoadMoreListener {
    fun onLoadMore()
}
