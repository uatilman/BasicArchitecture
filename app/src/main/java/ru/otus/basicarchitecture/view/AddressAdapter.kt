package ru.otus.basicarchitecture.view


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.basicarchitecture.databinding.ListItemBinding


class AddressAdapter(
    private val addressItemListener: AddressItemListener,
    //todo использовать дефолтный адаптер?
) : ListAdapter<AddressItem, AddressAdapter.AddressVariantsViewHolder>(
    AddressVariantsCallback
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        AddressVariantsViewHolder(
            binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            addressItemListener = addressItemListener
        )

    override fun onBindViewHolder(holder: AddressVariantsViewHolder, position: Int) =
        holder.bind(getItem(position))

    class AddressVariantsViewHolder(
        private val binding: ListItemBinding,
        private val addressItemListener: AddressItemListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val address: TextView = binding.root

        fun bind(addressItem: AddressItem) {
            address.text = addressItem.address
            binding.root.setOnClickListener {
                addressItemListener.onItemClick(addressItem.address)
            }
        }
    }

}

object AddressVariantsCallback : DiffUtil.ItemCallback<AddressItem>() {
    override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem.address == newItem.address
    }

    override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
        return oldItem.address == newItem.address
    }
}