package tic.tac.toe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BluetoothActivity : AppCompatActivity() {

    private val TAG = "BLUETOOTH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        // hides the top bar that says "Three in a row"
        supportActionBar?.hide()



    }
}