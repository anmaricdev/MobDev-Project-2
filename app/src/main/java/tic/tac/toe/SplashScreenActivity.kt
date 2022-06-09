package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        // after 3 seconds of splash screen visibility, switches from this SplashScreenActivity back to the MainActivity
       Handler(Looper.getMainLooper()).postDelayed({
           val intent = Intent (this@SplashScreenActivity, PreGameActivity::class.java)
           startActivity(intent)
           finish() // prevents the app returning to this screen if e.g. back button is pressed after switching to main app
        }, 3000)

    }
}