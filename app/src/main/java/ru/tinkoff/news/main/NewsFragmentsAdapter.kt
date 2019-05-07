package ru.tinkoff.news.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ru.tinkoff.news.R
import ru.tinkoff.news.list.NewsListFragment

class NewsFragmentsAdapter(
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return NewsListFragment.newInstance(position == 1)
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.news)
            1 -> context.getString(R.string.favourites)
            else -> null
        }
    }
}
