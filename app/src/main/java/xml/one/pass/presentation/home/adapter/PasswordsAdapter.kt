package xml.one.pass.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import xml.one.pass.databinding.ItemStoredPasswordBinding
import xml.one.pass.domain.model.PasswordModel

class PasswordsAdapter(
    private val passwordID: (Int) -> Unit
) : ListAdapter<PasswordModel, PasswordsAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStoredPasswordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(password = currentList[position])
    }

    inner class ViewHolder(
        val binding: ItemStoredPasswordBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(password: PasswordModel) {
            binding.apply {
                storedPasswordsSubTitle.text = password.siteName

                itemView.setOnClickListener {
                    passwordID(password.id)
                }
            }
        }
    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<PasswordModel>() {
            override fun areItemsTheSame(
                oldItem: PasswordModel,
                newItem: PasswordModel
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: PasswordModel,
                newItem: PasswordModel
            ): Boolean = oldItem == newItem
        }
    }
}