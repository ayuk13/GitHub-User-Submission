package com.dicoding.picodiploma.githubusers.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.dicoding.picodiploma.githubusers.R
import com.dicoding.picodiploma.githubusers.adapter.ListUserAdapter
import com.dicoding.picodiploma.githubusers.data.UserResponse
import com.dicoding.picodiploma.githubusers.databinding.FragmentMainBinding
import com.dicoding.picodiploma.githubusers.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel:MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUsers()
        binding.refresh.setOnRefreshListener {
            viewModel.getUsers()
            searchView.setQuery("",false)
        }
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.isNotEmpty()!!){
                    viewModel.getUsers(query)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked) }

        binding.btnFavList.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToUserFavoriteFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.users.observe(viewLifecycleOwner) { userList ->
            userList?.let {
                binding.rvUsers.adapter = ListUserAdapter(
                    this,
                    it as ArrayList<UserResponse>)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ loading ->
            loading?.let{
                binding.refresh.isRefreshing = it
                binding.rvUsers.visibility = if(it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isError.observe(viewLifecycleOwner){ error ->
            error?.let{
                binding.itemError.visibility = if (it) View.VISIBLE else View.GONE
                binding.rvUsers.visibility = if(it) View.GONE else View.VISIBLE
                binding.searchView.visibility = if (it) View.GONE else View.VISIBLE
                binding.btnFavList.visibility = if (it) View.GONE else View.VISIBLE
                binding.switchTheme.visibility = if(it) View.GONE else View.VISIBLE
                binding.helloText.visibility = if(it) View.GONE else View.VISIBLE
            }
        }

        viewModel.isEmpty.observe(viewLifecycleOwner){ empty ->
            empty?.let{
                binding.itemError.visibility = if (it) View.VISIBLE else View.GONE
                binding.rvUsers.visibility = if(it) View.GONE else View.VISIBLE

                if(it){binding.itemError.text = getString(R.string.empty_text)}
            }
        }

        viewModel.getThemeSettings().observe(viewLifecycleOwner)
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.switchTheme.isChecked = false
                }
            }
    }

    fun getFavorite(users: UserResponse){
        viewModel.getToFavorite(users, binding.searchView.query.toString())
    }

    fun removeFavorite(username:String){
        viewModel.removeFromFavorite(username, binding.searchView.query.toString())
    }

    fun toUsers(user: String){
        val action = MainFragmentDirections.actionMainFragmentToDetailUsersFragment2()
        action.user = user
        Navigation.findNavController(binding.root).navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}