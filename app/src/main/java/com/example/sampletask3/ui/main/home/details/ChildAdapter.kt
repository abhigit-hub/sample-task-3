package com.example.sampletask3.ui.main.home.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sampletask3.data.remote.response.VariationsItem
import com.example.sampletask3.databinding.ItemChildBinding
import com.example.sampletask3.ui.base.BaseViewHolder

class ChildAdapter(
    private val list: MutableList<VariationsItem>,
    private val parentPosition: Int,
    private val currentGroupId: String
) :
    RecyclerView.Adapter<BaseViewHolder>(),
    ChildViewHolder.ChildViewHolderCallback {

    private var callback: NestedAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        ChildViewHolder(
            ItemChildBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.onBind(list[position])

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.setCallback(this@ChildAdapter)
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        holder.removeCallback()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onItemSelected(variationId: String) {
        list.forEach {
            it.isSelected = it.id == variationId
        }
        callback?.onSelected(currentGroupId, variationId, parentPosition)
        notifyDataSetChanged()
    }

    fun setCallback(callback: NestedAdapterCallback) {
        this.callback = callback
    }

    fun rearrangeDataSet(selectedGroupId: String, selectedVariationId: String) {
        callback?.let { nestedAdapterCallback ->
            val exclusionMap = nestedAdapterCallback.getExclusionMap()

            val tempList = exclusionMap[Pair(selectedGroupId, selectedVariationId)]
            tempList?.let {
                val displayList = it.filter { pair -> pair.first == currentGroupId }.toList()

                displayList.forEach { pair ->
                    list.forEach { variationItem -> variationItem.combinationValid = pair.second != variationItem.id }
                }

                notifyDataSetChanged()
            }

            if (tempList == null) {
                list.forEach { variationItem -> variationItem.combinationValid = true }
                notifyDataSetChanged()
            }
        }
    }

    interface NestedAdapterCallback {
        fun onSelected(selectedGroupId: String, selectedVariationId: String, parentPosition: Int)

        fun getExclusionMap(): Map<Pair<String, String>, MutableList<Pair<String, String>>>
    }
}