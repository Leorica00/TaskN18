package com.example.taskn18.users

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.taskn18.databinding.UserRecyclerItemBinding
import com.example.taskn18.users.model.User

class UsersRecyclerViewAdapter :
    PagingDataAdapter<User, UsersRecyclerViewAdapter.UserViewHolder>(myItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(UserRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind()
    }

    inner class UserViewHolder(private val binding: UserRecyclerItemBinding) :
        ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind() {
            val user = getItem(bindingAdapterPosition)
            user?.let {
                with(binding) {
                    tvUserEmail.text = it.email
                    tvUserFullName.text = "${it.firstName} ${it.lastName}"
                    Glide.with(itemView.context).load(it.avatar).into(binding.ivAvatar)
                }
            }
        }
    }

    companion object {
        val myItemDiffCallback = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}