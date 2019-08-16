package com.kirinpatel.ehformeh.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.kirinpatel.ehformeh.R
import com.kirinpatel.ehformeh.utils.Deal
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (intent.extras != null && intent.extras[MainActivity.DEAL_KEY] != null) {
            val deal = intent.extras[MainActivity.DEAL_KEY] as Deal
            setupView(deal)
        }

        buyFab.setOnClickListener {
            val uri = Uri.parse("https://meh.com/account/signin?returnurl=https%3A%2F%2Fmeh.com%2F%23checkout")
            val browserIntent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(browserIntent)
        }
    }

    private fun setupView(deal: Deal) {
        val foregroundColor = if (deal.theme.isDark) Color.WHITE else Color.BLACK
        app_bar.setBackgroundColor(foregroundColor)
        toolbar_layout.setBackgroundColor(foregroundColor)
        toolbar.setBackgroundColor(foregroundColor)
        toolbar.setTitleTextColor(deal.theme.accentColor)
        if (deal.date == null) {
            toolbar.title = "Today's Deal"
            toolbar_layout.title = "Today's Deal"
        }
        buyFab.setBackgroundColor(deal.theme.accentColor)
        ImageViewCompat.setImageTintList(buyFab, ColorStateList.valueOf(deal.theme.backgroundColor))
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        constraintLayout.setBackgroundColor(deal.theme.backgroundColor)
        val dealTitleTextView = findViewById<TextView>(R.id.dealTitleTextView)
        dealTitleTextView.text = deal.title
        dealTitleTextView.setTextColor(deal.theme.accentColor)
        val dealPriceTextView = findViewById<TextView>(R.id.dealPriceTextView)
        dealPriceTextView.text = deal.price
        dealPriceTextView.setTextColor(deal.theme.accentColor)
        Picasso.get().load(deal.photos[0]).into(findViewById<ImageView>(R.id.testImageView))
    }

    companion object {
        private const val DEAL_KEY = "DEAL_KEY"

        fun createIntent(context: Context, deal: Deal): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(DEAL_KEY, deal)
            return intent
        }
    }
}
