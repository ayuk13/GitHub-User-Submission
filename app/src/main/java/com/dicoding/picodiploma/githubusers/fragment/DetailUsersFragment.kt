package com.dicoding.picodiploma.githubusers.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.adapter.SectionsPagerAdapter
import com.dicoding.picodiploma.githubusers.databinding.FragmentDetailUsersBinding
import com.dicoding.picodiploma.githubusers.viewmodel.DetailUserVModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_detail_users.*


class DetailUsersFragment : Fragment() {
    private var _binding: FragmentDetailUsersBinding? = null
    val binding get() = _binding!!
    val viewModel: DetailUserVModel by viewModels()
    private val args: DetailUsersFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailUsersBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        val sectionsPagerAdapter = SectionsPagerAdapter(this,args.user)
        with(binding) {
            viewPager.adapter = sectionsPagerAdapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }

        btnShare.setOnClickListener{
            Toast.makeText(context,"You share this profile", Toast.LENGTH_SHORT).show()
        }

        viewModel.detailUser(args.user)

        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.users.observe(viewLifecycleOwner){ users ->
            users?.let{
                with(binding){
                    tvItemUsername.text = it.login
                    tvItemName.text = it.name
                    tvItemCompany.text = it.company
                    tvItemLocation.text = it.location
                    tvRepository.text = it.public_repos.toString()
                    tvItemFollowers.text = it.followers.toString()
                    tvItemFollowing.text = it.following.toString()
                    val avatar = it.avatar_url
                    Glide.with(requireContext())
                        .asBitmap()
                        .circleCrop()
                        .load(avatar)
                        .into(img_item_avatar)

                    btnFav.setOnClickListener{
                        viewModel.addFavorite(users)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            loading?.let{
                binding.refresh.isRefreshing = it
            }
        }

        viewModel.isError.observe(viewLifecycleOwner){ error ->
            error?.let{
                with(binding){
                    itemError.visibility = if (it) View.VISIBLE else View.GONE
                    imgItemAvatar.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemLocation.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemFollowing.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemFollowers.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemName.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemUsername.visibility = if(it) View.GONE else View.VISIBLE
                    tvRepository.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemCompany.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemFollowersTitle.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemFollowingTitle.visibility = if(it) View.GONE else View.VISIBLE
                    tvItemRepositoryTitle.visibility = if(it) View.GONE else View.VISIBLE
                    tabs.visibility = if(it) View.GONE else View.VISIBLE
                    btnFav.visibility = if(it) View.GONE else View.VISIBLE
                }
            }
        }

        viewModel.isFav.observe(viewLifecycleOwner){ userFav ->
            binding.btnFav.apply{
                if(userFav != null){
                    setImageResource(R.drawable.ic_fav)
                }else{
                    setImageResource(R.drawable.ic_fav_unpressed)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}