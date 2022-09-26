package com.example.portsmap.module

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.portsmap.R


class SailignBoatFragment : AppCompatActivity() {

    lateinit var sailingBoat: ImageView
    lateinit var frameLayout: View

    protected var screenHeight = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sailing_boat)

        sailingBoat = findViewById<ImageView>(R.id.sailingboat)
        frameLayout = findViewById<View>(R.id.container)

        frameLayout.setOnClickListener { onStartAnimation() }

    }

    override fun onResume() {
        super.onResume()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels.toFloat()
    }

    protected fun onStartAnimation() {
        //val sunYStart: Float = sailingBoat.getTop().toFloat()
        val sunYEnd: Float = sailingBoat.getHeight().toFloat()

        val heightAnimator: ObjectAnimator = ObjectAnimator
            .ofFloat(sailingBoat, "x", 0f, sunYEnd)
            .setDuration(3000)


        val sunsetSkyAnimator: ObjectAnimator = ObjectAnimator
            .ofInt(
                frameLayout, "backgroundColor", resources.getColor(R.color.sunset_sky),
                resources.getColor(R.color.night_sky)
            )
            .setDuration(3000)

        heightAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationRepeat(animation: Animator) {
                // TODO Auto-generated method stub
            }

            override fun onAnimationEnd(animation: Animator) {
                val intent = Intent(this@SailignBoatFragment, Login::class.java)
                startActivity(intent)
            }

            override fun onAnimationCancel(animation: Animator) {
                // TODO Auto-generated method stub
            }
        })
        heightAnimator.start()
        sunsetSkyAnimator.start()
    }
}