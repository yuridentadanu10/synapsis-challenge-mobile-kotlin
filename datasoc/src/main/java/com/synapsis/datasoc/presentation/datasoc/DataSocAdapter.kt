package com.synapsis.datasoc.presentation.datasoc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.synapsis.challengeandroid.core.domain.model.BuildModel
import com.synapsis.challengeandroid.databinding.ItemBuildModelBinding

class DataSocAdapter(private val dataList: List<BuildModel>) :
    RecyclerView.Adapter<DataSocAdapter.SocViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocViewHolder {
        val view = ItemBuildModelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SocViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocViewHolder, position: Int) {
        dataList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class SocViewHolder(private val binding: ItemBuildModelBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buildModel: BuildModel) {
            binding.apply {
                tvNameBuild.text = buildModel.type
                tvValueBuild.text = buildModel.name
            }
        }
    }

}