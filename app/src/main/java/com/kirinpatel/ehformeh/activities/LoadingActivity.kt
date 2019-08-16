package com.kirinpatel.ehformeh.activities

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.kirinpatel.ehformeh.R
import com.kirinpatel.ehformeh.utils.Theme

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        loadCurrentTheme()
    }

    private fun loadCurrentTheme() {
        Theme.getCurrentTheme({ theme ->
            animateBackgroundColor(Color.parseColor(theme.backgroundColor))
            animateTitleAlpha()
        }) { error ->
            val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
            Snackbar
                    .make(constraintLayout, error.message, Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        loadCurrentTheme()
                    }
                    .show()
        }
    }

    private fun animateBackgroundColor(color: Int) {
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)
        val valueAnimator = ValueAnimator.ofInt(Color.WHITE, color)
        valueAnimator.setEvaluator(ArgbEvaluator())
        valueAnimator.addUpdateListener {
            val value = it.animatedValue as Int
            constraintLayout.setBackgroundColor(value)
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
}
