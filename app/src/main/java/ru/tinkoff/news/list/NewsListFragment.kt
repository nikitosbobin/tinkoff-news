package ru.tinkoff.news.list

import android.app.ActivityOptions
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                NewsItemDetailsActivity.createIntent(requireContext(), newsItem.id, transitionName),
                activityOptions?.toBundle()
            )
        }
        newsList.layoutManager = LinearLayoutManager(requireContext())
        newsList.adapter = adapter

        refreshLayout.isEnabled = !isFavourite
        refreshLayout.setOnRefreshListener {
            presenter.loadNews(true)
        }
    }

    override fun showNews(news: List<ListItem>) {
        adapter.items = news
        adapter.notifyDataSetChanged()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isFavourite) {
            presenter.loadFavouriteNews()
        } else {
            presenter.loadNews(false)
        }
    }

    override fun showLoading(show: Boolean) {
        refreshLayout.isRefreshing = show
    }

    override fun onError(error: Throwable) {
        Snackbar.make(refreshLayout, error.message.orEmpty(), Snackbar.LENGTH_LONG).show()
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

