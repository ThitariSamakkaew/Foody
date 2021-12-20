package com.thitari.foody.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thitari.foody.ui.fragments.detail.DetailsFragment

class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragment: ArrayList<Fragment>,
    fragmentActivity: DetailsFragment
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragment.size
    }

    override fun createFragment(position: Int): Fragment {
        fragment[position].arguments = resultBundle
        return fragment[position]
    }
}
