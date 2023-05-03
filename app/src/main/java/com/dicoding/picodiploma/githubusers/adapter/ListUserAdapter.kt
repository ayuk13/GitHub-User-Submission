package com.dicoding.picodiploma.githubusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.databinding.ItemUsersBinding
import com.dicoding.picodiploma.githubusers.fragment.FolFragment
import com.dicoding.picodiploma.githubusers.fragment.MainFragment
import com.dicoding.picodiploma.githubusers.fragment.UserFavoriteFragment
import kotlinx.android.synthetic.main.item_users.view.*

class ListUserAdapter(
    private val fragment: Fragment,
    private val listUsers: ArrayList<UserResponse>
) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var binding: ItemUsersBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding.root)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUsers[position]
        holder.tvUsername.text = user.login
        Glide.with(holder.itemView.context)
            .asBitmap()
            .circleCrop()
            .load(user.avatar_url)
            .into(holder.imgPhoto)

        holder.itemView.setOnClickListener{
            if(fragment is MainFragment){
                fragment.toUsers(user.login)
            }
            else if(fragment is UserFavoriteFragment){
                fragment.goToDetail(user.login)
            }
        }

        if(fragment is FolFragment){
            holder.itemView.ivFav.visibility = View.GONE
        }

        if(fragment is MainFragment){
            holder.itemView.ivFav.visibility = View.GONE
        }

        holder.itemView.ivFav.setOnClickListener {
            if(fragment is UserFavoriteFragment) {
                fragment.removeFavorite(user.login)
            }
        }

        when{
            fragment is UserFavoriteFragment ->{
                holder.itemView.ivFav.setImageResource(R.drawable.ic_fav)
            }
            user.usersFav ->{
                holder.itemView.ivFav.setImageResource(R.drawable.ic_fav)
            }
            else ->{
                holder.itemView.ivFav.setImageResource(R.drawable.ic_fav_unpressed)
            }
        }
    }

    override fun getItemCount(): Int = listUsers.size

    class ListViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_avatar)
        var tvUsername: TextView = itemView.findViewById(R.id.tv_item_username)
    }
}