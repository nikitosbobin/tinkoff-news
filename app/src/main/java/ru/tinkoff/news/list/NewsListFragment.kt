package ru.tinkoff.news.list

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_news_list.*
import ru.tinkoff.news.NewsApplication
import ru.tinkoff.news.R
import ru.tinkoff.news.details.NewsItemDetailsActivity
import ru.tinkoff.news.list.adapter.NewsAdapter
import ru.tinkoff.news.mvp.AndroidXMvpAppCompatFragment
import java.util.*

class NewsListFragment : AndroidXMvpAppCompatFragment(), NewsListView {

    private lateinit var adapter: NewsAdapter
    private val isFavourite: Boolean by lazy {
        arguments!!.getBoolean(ARG_IS_FAVOURITE)
    }

    @InjectPresenter
    lateinit var presenter: NewsListPresenter

    @ProvidePresenter
    fun providePresenter() = NewsApplication.component.getNewsListPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter { newsItem, textView ->
            val transitionName = UUID.randomUUID().toString()
            val activityOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView.transitionName = transitionName
                ActivityOptions.makeSceneTransitionAnimation(activity, textView, transitionName)
            } else {
                null
            }

            startActivity(
                NewsItemDetailsActivity.createIntent(requireContext(), newsItem.id, newsItem.title, transitionName),
                activityOptions?.toBundle()
            )
        }
        newsList.layoutManager = LinearLayoutManager(requireContext())
        newsList.adapter = adapter

        refreshLayout.isEnabled = !isFavourite
        refreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark),
            ContextCompat.getColor(requireContext(), R.color.colorAccent)
        )
        refreshLayout.setOnRefreshListener {
            presenter.loadNews(true)
        }
    }

    override fun showNews(news: List<ListItem>) {
        adapter.items = news

        if (news.isEmpty()) {
            showEmptyPlaceholder()
        } else {
            emptyPlaceholder.isVisible = false
            newsList.isVisible = true

            adapter.notifyDataSetChanged()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isFavourite) {
            presenter.loadFavouriteNews()
        } else {
            presenter.subscribeToNetworkChanges()
            presenter.loadNews(false)
        }
    }

    override fun showLoading(show: Boolean) {
        refreshLayout.isRefreshing = show
    }

    override fun onError(error: Throwable) {
        if (adapter.itemCount == 0) {
            showEmptyPlaceholder()
        }
        Snackbar.make(refreshLayout, R.string.internal_error, Snackbar.LENGTH_LONG).show()
    }

    override fun onInternetStateChanged(connected: Boolean) {
        refreshLayout.isEnabled = !isFavourite && connected
        if (connected) {
            Snackbar.make(refreshLayout, R.string.yes_internet, Snackbar.LENGTH_SHORT).show()
        } else {
            Snackbar.make(refreshLayout, R.string.no_internet, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showEmptyPlaceholder() {
        emptyPlaceholder.isVisible = true
        newsList.isVisible = false

        val textRes = if (isFavourite) R.string.no_favourites else R.string.no_news
        emptyPlaceholderText.setText(textRes)
    }

    companion object {
        private const val ARG_IS_FAVOURITE = "is_favourite"

        fun newInstance(isFavourite: Boolean): NewsListFragment {
            val args = Bundle()
            args.putBoolean(ARG_IS_FAVOURITE, isFavourite)
            val fragment = NewsListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

