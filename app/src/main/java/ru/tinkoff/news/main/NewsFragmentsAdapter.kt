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
        return NewsListFragment.newInstance(position == PAGE_FAVOURITES_NEWS)
    }

    override fun getCount(): Int = PAGES_COUNT

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            PAGE_ALL_NEWS -> context.getString(R.string.news)
            PAGE_FAVOURITES_NEWS -> context.getString(R.string.favourites)
            else -> throw IllegalStateException("Unknown page $position")
        }
    }

    private companion object {
        const val PAGE_ALL_NEWS = 0
        const val PAGE_FAVOURITES_NEWS = 1
        const val PAGES_COUNT = 2
    }
}
