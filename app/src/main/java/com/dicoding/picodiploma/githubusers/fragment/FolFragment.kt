package com.dicoding.picodiploma.githubusers.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.picodiploma.githubusers.adapter.ListUserAdapter
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.databinding.FragmentFolBinding
import com.dicoding.picodiploma.githubusers.viewmodel.FolUserVModel
import kotlinx.android.synthetic.main.fragment_detail_users.*

class FolFragment : Fragment(){

    private var _binding: FragmentFolBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FolUserVModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFolBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt(ARG_NUMBER,0)
        val username = arguments?.getString(ARG_USER)
        if(index == CODE_FOLLOWERS){
            viewModel.followersUser(username!!)
        }
        else{
            viewModel.followingUser(username!!)
        }

        if(parentFragment is DetailUsersFragment){
            (parentFragment as DetailUsersFragment).binding.refresh.setOnRefreshListener {
                if(index == CODE_FOLLOWERS) viewModel.followersUser(username)
                else viewModel.followingUser(username)

                (parentFragment as DetailUsersFragment).viewModel.detailUser(username)
            }
        }

        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.userFollowers.observe(viewLifecycleOwner){ userFollowers ->
            userFollowers?.let{
                binding.rvUsers.adapter = ListUserAdapter(this,
                    it as ArrayList<UserResponse>)
            }
        }

        viewModel.userFollowing.observe(viewLifecycleOwner){ userFollowing ->
            userFollowing?.let{
                binding.rvUsers.adapter = ListUserAdapter(this,
                    it as ArrayList<UserResponse>)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            loading?.let{
                binding.loadingBar.visibility = if(it) View.VISIBLE else View.GONE
                binding.rvUsers.visibility = if(it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isError.observe(viewLifecycleOwner){ error ->
            error?.let{
                binding.itemError.visibility = if (it) View.VISIBLE else View.GONE
                binding.rvUsers.visibility = if(it) View.GONE else View.VISIBLE
            }
        }
    }

    companion object{
        const val ARG_NUMBER = "section_number"
        const val ARG_USER = "user"
        const val CODE_FOLLOWERS = 1
    }
}