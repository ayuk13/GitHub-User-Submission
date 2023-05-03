package com.dicoding.picodiploma.githubusers.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.picodiploma.githubusers.fragment.FolFragment

class SectionsPagerAdapter(
    private val fragment: Fragment,
    private val user: String
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FolFragment()

        fragment.arguments = Bundle().apply{
            putInt(FolFragment.ARG_NUMBER, position+1)
            putString(FolFragment.ARG_USER, user)
        }
        return fragment
    }

}