package com.example.sampletask3.ui.main.home.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sampletask3.data.remote.response.VariantGroupsItem
import com.example.sampletask3.databinding.ItemGroupBinding
import kotlinx.android.synthetic.main.item_group.view.*

class GroupAdapter(private val list: MutableList<VariantGroupsItem>) : RecyclerView.Adapter<GroupViewHolder>(),
    ChildAdapter.NestedAdapterCallback {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val childAdapterList = mutableListOf<ChildAdapter>()
    private var exclusionMap =
        mapOf<Pair<String, String>, MutableList<Pair<String, String>>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder =
        GroupViewHolder(
            ItemGroupBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.onBind(list[position])

        initChildAdapter(holder, position)
    }

    private fun initChildAdapter(holder: GroupViewHolder, position: Int) {
        val childLinearLayoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayout.HORIZONTAL, false)

        holder.itemView.recyclerview_variation?.apply {
            layoutManager = childLinearLayoutManager
            list[position].variations?.let {
                val childAdapter = ChildAdapter(it.toMutableList(), position, list[position].groupId)
                childAdapter.setCallback(this@GroupAdapter)
                childAdapterList.add(childAdapter)
                adapter = childAdapter
            }
            setRecycledViewPool(viewPool)
        }
    }

    fun appendData(newList: List<VariantGroupsItem>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateExclusionMap(exclusionMap: Map<Pair<String, String>, MutableList<Pair<String, String>>>) {
        this.exclusionMap = exclusionMap
    }

    override fun onSelected(selectedGroupId: String, selectedVariationId: String, parentPosition: Int) {
        childAdapterList.forEachIndexed { index, childAdapter ->
            if (index != parentPosition) {
                childAdapter.rearrangeDataSet(selectedGroupId, selectedVariationId)
            }
        }
    }

    override fun getExclusionMap(): Map<Pair<String, String>, MutableList<Pair<String, String>>> = exclusionMap
}