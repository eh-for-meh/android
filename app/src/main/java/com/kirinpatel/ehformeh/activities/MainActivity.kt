package com.kirinpatel.ehformeh.activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import kotlinx.android.synthetic.main.activity_main.*
import com.kirinpatel.ehformeh.R
import com.kirinpatel.ehformeh.utils.Deal

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (intent.extras != null && intent.extras[MainActivity.DEAL_KEY] != null) {
            val deal = intent.extras[MainActivity.DEAL_KEY] as Deal
            setupView(deal)
        }

        buyFab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
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
