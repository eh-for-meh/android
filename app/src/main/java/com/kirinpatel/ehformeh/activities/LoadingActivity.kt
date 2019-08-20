package com.kirinpatel.ehformeh.activities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.kirinpatel.ehformeh.R
import com.kirinpatel.ehformeh.utils.Deal
import com.kirinpatel.ehformeh.utils.Theme

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        MobileAds.initialize(this, "ca-app-pub-9026572937829340/3372233259")
        loadCurrentTheme()
    }

    private fun loadCurrentTheme() {
        Theme.getCurrentTheme({ theme ->
            animateBackgroundColor(theme.backgroundColor)
            animateTitleAlpha()
        }) { error ->
            val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
            Snackbar
                    .make(constraintLayout, error.message, Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        loadCurrentTheme()
                    }
                    .setActionTextColor(Color.LTGRAY)
                    .show()
        }
    }

    private fun animateBackgroundColor(backgroundColor: Int) {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        val valueAnimator = ValueAnimator.ofInt(Color.WHITE, backgroundColor)
        valueAnimator.setEvaluator(ArgbEvaluator())
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            constraintLayout.setBackgroundColor(value)
            loadMainActivity()
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.start()
    }

    private fun animateTitleAlpha() {
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val valueAnimator = ValueAnimator.ofFloat(1F, 0F)
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            titleTextView.alpha = value
        }
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 500
        valueAnimator.start()
    }

    private fun loadMainActivity() {
        Theme.getCurrentTheme({ theme ->
            val intent = MainActivity.createIntent(this, theme)
            startActivity(intent)
        }) { error ->
            val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
            Snackbar
                    .make(constraintLayout, error.message, Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        loadMainActivity()
                    }
                    .setActionTextColor(Color.LTGRAY)
                    .show()
        }
    }
}
