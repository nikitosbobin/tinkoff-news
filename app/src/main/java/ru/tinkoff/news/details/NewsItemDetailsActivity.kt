package ru.tinkoff.news.details

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_news_item_details.*
import ru.tinkoff.news.NewsApplication
import ru.tinkoff.news.R
import ru.tinkoff.news.getHttpContent
import ru.tinkoff.news.model.NewsItemDetails
import ru.tinkoff.news.mvp.AndroidXMvpAppCompatActivity

class NewsItemDetailsActivity : AndroidXMvpAppCompatActivity(), NewsItemDetailsView {

    private var isInFavourite = false
    private val favouriteIcon: Drawable by lazy {
        ContextCompat.getDrawable(this@NewsItemDetailsActivity, R.drawable.ic_favorite)!!
    }
    private val nonFavouriteIcon: Drawable by lazy {
        ContextCompat.getDrawable(this@NewsItemDetailsActivity, R.drawable.ic_favorite_border)!!
    }

    @InjectPresenter
    lateinit var presenter: NewsItemDetailsPresenter

    @ProvidePresenter
    fun providePresenter() = NewsApplication.component.getNewsItemDetailsPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_item_details)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            titleTextView.transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)
            startPostponedEnterTransition()
        }

        floatingActionButton.hide()
        floatingActionButton.setOnClickListener {
            scrollView.smoothScrollTo(0, 0)
        }
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (scrollView.canScrollVertically(-1)) {
                floatingActionButton.show()
            } else {
                floatingActionButton.hide()
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.loadDetails(intent.getStringExtra(EXTRA_NEWS_ID)!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_favourite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.item_fav)?.apply {
            icon = if (isInFavourite) favouriteIcon else nonFavouriteIcon
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun showDetails(details: NewsItemDetails, isFavourite: Boolean) {
        isInFavourite = isFavourite
        invalidateOptionsMenu()

        content.movementMethod = LinkMovementMethod.getInstance()
        titleTextView.text = details.title.title
        content.text = details.getHttpContent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_fav -> {
                isInFavourite = !isInFavourite
                presenter.changeFavouriteState(isInFavourite)
                invalidateOptionsMenu()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showLoading(show: Boolean) {
        progressBar.isVisible = show
        val alpha = if (show) 0f else 1f
        content.animate()
            .setDuration(200L)
            .alpha(alpha)
    }

    override fun onError(error: Throwable) {
    }

    companion object {
        private const val EXTRA_NEWS_ID = "news_id"
        private const val EXTRA_TRANSITION_NAME = "transition_name"

        fun createIntent(context: Context, newsId: String, transitionName: String): Intent {
            return Intent(context, NewsItemDetailsActivity::class.java)
                .putExtra(EXTRA_NEWS_ID, newsId)
                .putExtra(EXTRA_TRANSITION_NAME, transitionName)
        }
    }
}
