package ru.otus.basicarchitecture.view


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.databinding.VhTagBinding


class TagAdapter(
    private val itemListener: ItemListener,
) : ListAdapter<TagItem, TagAdapter.TagViewHolder>(TagDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) =
        TagViewHolder(
            binding = VhTagBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            itemListener = itemListener
        )

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) =
        holder.bind(getItem(position))

    class TagViewHolder(
        private val binding: VhTagBinding,
        private val itemListener: ItemListener,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val tag: TextView = binding.root

        fun bind(tagItem: TagItem) {
            with(tagItem) {
                tag.text = name
            }
            binding.root.setOnClickListener {
                tagItem.isSelected = !tagItem.isSelected
                val context = binding.root.context
                val color =
                    if (tagItem.isSelected) ContextCompat.getColor(context, R.color.teal_200)
                    else ContextCompat.getColor(context, R.color.white)

                tag.setBackgroundColor(color)
                itemListener.onItemClick(tagItem.interest, tagItem.isSelected)
            }
        }
    }

}

object TagDiffCallback : DiffUtil.ItemCallback<TagItem>() {
    override fun areItemsTheSame(oldItem: TagItem, newItem: TagItem): Boolean {
        return oldItem.interest.id == newItem.interest.id
    }

    override fun areContentsTheSame(oldItem: TagItem, newItem: TagItem): Boolean {
        return oldItem.interest.title == newItem.interest.title
    }
}