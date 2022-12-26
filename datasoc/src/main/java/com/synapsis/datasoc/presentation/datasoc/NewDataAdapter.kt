package com.synapsis.datasoc.presentation.datasoc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.synapsis.challengeandroid.core.domain.model.DataSocWithId
import com.synapsis.challengeandroid.databinding.ItemDataSocBinding

class NewDataAdapter(
    private val dataList: List<DataSocWithId>,
    private val onQrShow: (DataSocWithId) -> Unit,
    private val onDelete: (DataSocWithId) -> Unit
) :
    RecyclerView.Adapter<NewDataAdapter.SocViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocViewHolder {
        val view = ItemDataSocBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SocViewHolder(view)
    }

    override fun onBindViewHolder(holder: SocViewHolder, position: Int) {
        dataList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class SocViewHolder(private val binding: ItemDataSocBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buildModel: DataSocWithId) {
            binding.apply {
                tvNameData.text = buildModel.textInput
                tvNameDate.text = buildModel.date
                tvCreatedBy.text = "Created By: ${buildModel.createdBy}"
                btnDelete.setOnClickListener {
                    onDelete.invoke(buildModel)
                }
                btnShowQR.setOnClickListener {
                    onQrShow.invoke(buildModel)
                }
            }
        }
    }

}