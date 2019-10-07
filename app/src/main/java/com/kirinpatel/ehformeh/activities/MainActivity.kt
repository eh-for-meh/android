package com.kirinpatel.ehformeh.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.ImageViewCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.kirinpatel.ehformeh.R
import com.kirinpatel.ehformeh.utils.Deal
import com.kirinpatel.ehformeh.utils.Theme
import com.squareup.picasso.Picasso
import io.noties.markwon.Markwon
import android.content.ClipData.newIntent



class MainActivity : AppCompatActivity() {


    lateinit var backgroundConstraintLayout: ConstraintLayout
    lateinit var constraintLayout: ConstraintLayout
    lateinit var dealTitleTextView: TextView
    lateinit var dealPriceTextView: TextView
    lateinit var dealFeaturesTextView: TextView
    lateinit var dealSpecificationsTextView: TextView
    lateinit var dealStoryTextView: TextView
    lateinit var theme: Theme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        backgroundConstraintLayout = findViewById(R.id.backgroundConstraintLayout)
        constraintLayout = findViewById(R.id.constraintLayout)
        dealTitleTextView = findViewById(R.id.dealTitleTextView)
        dealPriceTextView = findViewById(R.id.dealPriceTextView)
        dealFeaturesTextView = findViewById(R.id.dealFeaturesTextView)
        dealSpecificationsTextView = findViewById(R.id.dealSpecificationsTextView)
        dealStoryTextView = findViewById(R.id.dealStoryTextView)

        if (intent.extras != null && intent.extras[MainActivity.DEAL_KEY] != null) {
            val deal = intent.extras[MainActivity.DEAL_KEY] as Deal
            setupView(deal)
        } else if (intent.extras != null && intent.extras[MainActivity.THEME_KEY] != null) {
            val theme = intent.extras[MainActivity.THEME_KEY] as Theme
            setupTheme(theme)
            loadCurrentDeal()
        }

        val dealFeatureBanner = findViewById<AdView>(R.id.dealFeatureBanner)
        dealFeatureBanner.loadAd(AdRequest.Builder().build())

        buyFab.setOnClickListener {
            val uri = Uri.parse("https://meh.com/account/signin?returnurl=https%3A%2F%2Fmeh.com%2F%23checkout")
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        if (theme != null) {
            if (menu != null) {
                for (i in 0 until menu.size()) {
                    val drawable = menu.getItem(i).icon
                    if (drawable != null) {
                        val wrapped = DrawableCompat.wrap(drawable)
                        drawable.mutate()
                        DrawableCompat.setTint(wrapped, theme.backgroundColor)
                        menu.getItem(i).icon = drawable
                    }
                }

            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item == null) {
            return super.onOptionsItemSelected(item)
        }
        return when (item.itemId) {
            R.id.action_buy -> {
                buyDeal()
                true
            }
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupTheme(theme: Theme) {
        val foreground = if (theme.isDark) Color.WHITE else Color.BLACK
        app_bar.setBackgroundColor(theme.accentColor)
        toolbar_layout.setBackgroundColor(theme.accentColor)
        toolbar_layout.setExpandedTitleColor(theme.backgroundColor)
        toolbar_layout.setCollapsedTitleTextColor(theme.backgroundColor)
        toolbar.setBackgroundColor(theme.accentColor)
        buyFab.backgroundTintList = ColorStateList.valueOf(foreground)
        buyFab.setOnClickListener { buyDeal() }
        ImageViewCompat.setImageTintList(buyFab, ColorStateList.valueOf(theme.backgroundColor))
        backgroundConstraintLayout.setBackgroundColor(theme.backgroundColor)
        constraintLayout.setBackgroundColor(theme.backgroundColor)
        dealTitleTextView.setTextColor(theme.accentColor)
        dealPriceTextView.setTextColor(theme.accentColor)
        dealFeaturesTextView.setTextColor(theme.accentColor)
        dealSpecificationsTextView.setTextColor(theme.accentColor)
        dealStoryTextView.setTextColor(theme.accentColor)
        this.theme = theme
    }

    private fun setupView(deal: Deal) {
        constraintLayout.alpha = 1F
        setupTheme(deal.theme)
        if (deal.date == null) {
            toolbar.title = "Today's Deal"
            toolbar_layout.title = "Today's Deal"
        }
        dealTitleTextView.text = deal.title
        dealPriceTextView.text = deal.price
        Picasso.get().load(deal.photos[0]).into(findViewById<ImageView>(R.id.testImageView))
        val markwon = Markwon.create(applicationContext)
        markwon.setMarkdown(dealFeaturesTextView, deal.features)
        markwon.setMarkdown(dealSpecificationsTextView, deal.specifications)
        val story: String = StringBuilder()
                .append("# ")
                .append(deal.story.title)
                .append("\n")
                .append(deal.story.body)
                .toString()
        markwon.setMarkdown(dealStoryTextView, story)
    }

    private fun loadCurrentDeal() {
        Deal.watchCurrentDeal({ deal ->
            setupView(deal)
        }) {
            Snackbar
                    .make(
                            constraintLayout,
                            "Unable to load this deal!",
                            Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.LTGRAY)
                    .show()
        }
    }

    private fun buyDeal() {
        val url = "https://meh.com/account/signin?returnurl=https%3A%2F%2Fmeh.com%2F%23checkout"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    companion object {
        private const val DEAL_KEY = "DEAL_KEY"
        private const val THEME_KEY = "THEME_KEY"

        fun createIntent(context: Context, deal: Deal): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(DEAL_KEY, deal)
            return intent
        }

        fun createIntent(context: Context, theme: Theme): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(THEME_KEY, theme)
            return intent
        }
    }
}
