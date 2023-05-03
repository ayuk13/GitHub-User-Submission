package com.dicoding.picodiploma.githubusers.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dicoding.picodiploma.githubusers.adapter.ListUserAdapter
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.databinding.UserFavoriteFragmentBinding
import com.dicoding.picodiploma.githubusers.viewmodel.UserFavoriteViewModel

class UserFavoriteFragment : Fragment() {

    private var _binding: UserFavoriteFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserFavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = UserFavoriteFragmentBinding.inflate(
            inflater,
            container,
            false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsersFavorite()
        observeLiveData()
    }

    private fun observeLiveData(){
        viewModel.listUsers.observe(viewLifecycleOwner){ user ->
            user?.let{
                binding.rvUsers.adapter = ListUserAdapter(this, it as ArrayList<UserResponse>)
            }
        }
        viewModel.empty.observe(viewLifecycleOwner){ empty->
            empty?.let{
                binding.itemEmpty.visibility = if(it) View.VISIBLE else View.GONE
            }
        }
    }

    fun removeFavorite(users: String){
        viewModel.removeFromFavorite(users)
    }

    fun goToDetail(username: String){
        val action = UserFavoriteFragmentDirections.actionUserFavoriteFragmentToDetailUsersFragment()
        action.user = username
        Navigation.findNavController(binding.root).navigate(action)
    }

//    fun toFavorite(users: String){
//        val action = UserFavoriteFragmentDirections.actionUserFavoriteFragmentToDetailUsersFragment()
//        action.user = users
//        Navigation.findNavController(binding.root).navigate(action)
//    }


}