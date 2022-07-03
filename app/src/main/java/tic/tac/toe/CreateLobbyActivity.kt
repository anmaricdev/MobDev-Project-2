package tic.tac.toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var isCodeMaker = true
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue: String = "null"

class CreateLobbyActivity : AppCompatActivity() {

    private val TAG = "MULTIPLAYER"

    private lateinit var gameCode: EditText
    private lateinit var createButton: Button
    private lateinit var joinButton: Button
    private lateinit var loadingBar: ProgressBar
    private lateinit var gameCodeHeading: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_lobby)

        // hides the top bar that says "Three in a row"
        supportActionBar?.hide()

        gameCode = findViewById(R.id.enter_game_code)
        createButton = findViewById(R.id.btn_createLobby)
        joinButton = findViewById(R.id.btn_joinLobby)
        loadingBar = findViewById(R.id.loadingGame)
        gameCodeHeading = findViewById(R.id.game_code_heading)

        createButton.setOnClickListener{
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = gameCode.text.toString()
            createButton.visibility = View.GONE
            joinButton.visibility = View.GONE
            loadingBar.visibility = View.VISIBLE
            gameCode.visibility = View.GONE
            gameCodeHeading.visibility = View.GONE

            if(code != "null" && code != ""){
                isCodeMaker = true
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var check = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            // check if data is already present in the DB
                            if(check == true){
                                createButton.visibility = View.VISIBLE
                                joinButton.visibility = View.VISIBLE
                                loadingBar.visibility = View.GONE
                                gameCode.visibility = View.VISIBLE
                                gameCodeHeading.visibility = View.VISIBLE
                                // if not, push the code to the database
                            } else {
                                FirebaseDatabase.getInstance().reference.child("codes").push().setValue(code)
                                isValueAvailable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(this@CreateLobbyActivity,"Code has been created!" ,Toast.LENGTH_SHORT).show()
                                }, 300)
                            }
                        }, 2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.v(TAG, "createButton: Couldn't load the DB")
                    }
                })
            } else {
                createButton.visibility = View.VISIBLE
                joinButton.visibility = View.VISIBLE
                loadingBar.visibility = View.GONE
                gameCode.visibility = View.VISIBLE
                gameCodeHeading.visibility = View.VISIBLE
                Toast.makeText(this@CreateLobbyActivity, "Please enter a valid Code", Toast.LENGTH_SHORT).show()
            }
        }

        joinButton.setOnClickListener{
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = gameCode.text.toString()

            if(code != "null" && code != ""){
                createButton.visibility = View.GONE
                joinButton.visibility = View.GONE
                loadingBar.visibility = View.VISIBLE
                gameCode.visibility = View.GONE
                gameCodeHeading.visibility = View.GONE
                isCodeMaker = false
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data : Boolean = isValueAvailable(snapshot, code)
                        Handler().postDelayed({
                            if (data == true){
                                codeFound = true
                                accepted()
                                createButton.visibility = View.VISIBLE
                                joinButton.visibility = View.VISIBLE
                                loadingBar.visibility = View.GONE
                                gameCode.visibility = View.VISIBLE
                                gameCodeHeading.visibility = View.VISIBLE
                            } else {
                                createButton.visibility = View.VISIBLE
                                joinButton.visibility = View.VISIBLE
                                loadingBar.visibility = View.GONE
                                gameCode.visibility = View.VISIBLE
                                gameCodeHeading.visibility = View.VISIBLE
                                Toast.makeText(this@CreateLobbyActivity, "Wrong Code, please try again", Toast.LENGTH_SHORT).show()
                            }
                        }, 2000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.v(TAG, "joinButton: Couldn't load the DB")
                    }
                })
            } else {
                Toast.makeText(this@CreateLobbyActivity, "Please enter the code from the Host!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    // called when the second player accepts the request of the first user
    fun accepted(){
        val intent = Intent(this@CreateLobbyActivity, PlayerNameActivity::class.java)
        startActivity(intent)
        createButton.visibility = View.VISIBLE
        joinButton.visibility = View.VISIBLE
        loadingBar.visibility = View.GONE
        gameCode.visibility = View.VISIBLE
        gameCodeHeading.visibility = View.VISIBLE
        finish()
    }
    // called when the game code is inside the DB
    fun isValueAvailable(snapshot: DataSnapshot, code : String): Boolean{
        var data = snapshot.children
        data.forEach{
            var value = it.getValue().toString()
            if(value == code){
                keyValue = it.key.toString()
                return true
            }
        }
        return false
    }

}