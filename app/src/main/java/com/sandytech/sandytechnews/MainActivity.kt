package com.sandytech.sandytechnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private var btnChangeEmail: Button? = null
    private var btnChangePassword: Button? = null
    private var btnSendResetEmail: Button? = null
    private var btnRemoveUser: Button? = null
    private var actionChangeEmail: Button? = null
    private var actionChangePassword: Button? = null
    private var actionResetPassword: Button? = null
    private var actionRemoveUser: Button? = null
    private var btnSignOut: Button? = null

    private var getOldEmail: EditText? = null
    private var getNewEmail: EditText? = null
    private var getOldPassword: EditText? = null
    private var getNewPassword: EditText? = null

    private var progressBar: ProgressBar? = null

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                startActivity(Intent(this@MainActivity, LoginRegisterActivity::class.java))
                finish()
            }
        }

        getOldEmail = findViewById<EditText>(R.id.inputRegisteredEmail)
        getNewEmail = findViewById<EditText>(R.id.inputNewEmail)
        getOldPassword = findViewById<EditText>(R.id.inputOldPassword)
        getNewPassword = findViewById<EditText>(R.id.inputNewPassword)

        getOldEmail!!.visibility = View.GONE
        getNewEmail!!.visibility = View.GONE
        getOldPassword!!.visibility = View.GONE
        getNewPassword!!.visibility = View.GONE

        btnChangeEmail = findViewById<Button>(R.id.btnChangeEmail)
        btnChangePassword = findViewById<Button>(R.id.btnChangePassword)
        btnSendResetEmail = findViewById<Button>(R.id.btnResetPassword)
        btnRemoveUser = findViewById<Button>(R.id.btnRemoveUser)

        btnChangeEmail!!.visibility = View.GONE
        btnChangePassword!!.visibility = View.GONE
        btnSendResetEmail!!.visibility = View.GONE
        btnRemoveUser!!.visibility = View.GONE

        actionChangeEmail = findViewById<Button>(R.id.actionChangeEmail)
        actionChangePassword = findViewById<Button>(R.id.actionChangePassword)
        actionResetPassword = findViewById<Button>(R.id.actionResetPassword)
        actionRemoveUser = findViewById<Button>(R.id.actionRemoveUser)

        btnSignOut = findViewById<Button>(R.id.btnSignOut)

        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar!!.visibility = View.GONE



        btnChangeEmail!!.setOnClickListener { changeEmail() }

        btnChangePassword!!.setOnClickListener  { changePassword() }

        btnSendResetEmail!!.setOnClickListener { resetPassword() }

        btnRemoveUser!!.setOnClickListener { removeUser() }

        btnSignOut!!.setOnClickListener { signOut() }



        actionChangeEmail!!.setOnClickListener { actionChangeEmail() }

        actionChangePassword!!.setOnClickListener { actionChangePassword() }

        actionResetPassword!!.setOnClickListener { actionReset() }

        actionRemoveUser!!.setOnClickListener { actionRemoveUser() }
    }

    private fun changeEmail() {
        progressBar!!.visibility = View.VISIBLE
        if (user != null && getNewEmail!!.text.toString().trim { it <= ' ' } != "") {
            user!!.updateEmail(getNewEmail!!.text.toString().trim { it <= ' ' })
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@MainActivity,
                            "Email address is updated. Please sign in with new email id!",
                            Toast.LENGTH_LONG
                        ).show()
                        signOut()
                        progressBar!!.visibility = View.GONE
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to update email!", Toast.LENGTH_LONG).show()
                        progressBar!!.visibility = View.GONE
                    }
                }
        } else if (getNewEmail!!.text.toString().trim { it <= ' ' } == "") {
            getNewEmail!!.error = "Enter email"
            progressBar!!.visibility = View.GONE
        }
    }

    private fun changePassword() {
        progressBar!!.visibility = View.VISIBLE
        if (user != null && getNewPassword!!.text.toString().trim { it <= ' ' } != "") {
            if (getNewPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
                getNewPassword!!.error = "Password too short, enter minimum 6 characters"
                progressBar!!.visibility = View.GONE
            } else {
                user!!.updatePassword(getNewPassword!!.text.toString().trim { it <= ' ' })
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@MainActivity,
                                "Password is updated, sign in with new getOldPassword!",
                                Toast.LENGTH_SHORT
                            ).show()
                            signOut()
                            progressBar!!.visibility = View.GONE
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to update getOldPassword!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            progressBar!!.visibility = View.GONE
                        }
                    }
            }
        } else if (getNewPassword!!.text.toString().trim { it <= ' ' } == "") {
            getNewPassword!!.error = "Enter getOldPassword"
            progressBar!!.visibility = View.GONE
        }


    }

    private fun resetPassword() {
        progressBar!!.visibility = View.VISIBLE
        if (getOldEmail!!.text.toString().trim { it <= ' ' } != "") {
            auth!!.sendPasswordResetEmail(getOldEmail!!.text.toString().trim { it <= ' ' })
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Reset getOldPassword email is sent!", Toast.LENGTH_SHORT)
                            .show()
                        progressBar!!.visibility = View.GONE
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to send reset email!", Toast.LENGTH_SHORT)
                            .show()
                        progressBar!!.visibility = View.GONE
                    }
                }
        } else {
            getOldEmail!!.error = "Enter email"
            progressBar!!.visibility = View.GONE
        }
    }

    private fun removeUser() {
        progressBar!!.visibility = View.VISIBLE
        user?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@MainActivity,
                    "Your profile is deleted:( Create a account now!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@MainActivity, LoginRegisterActivity::class.java))
                finish()
                progressBar!!.visibility = View.GONE
            } else {
                Toast.makeText(this@MainActivity, "Failed to delete your account!", Toast.LENGTH_SHORT).show()
                progressBar!!.visibility = View.GONE
            }
        }
    }

    private fun signOut() {
        auth!!.signOut()
    }



    private fun actionChangeEmail() {
        getOldEmail!!.visibility = View.GONE
        getNewEmail!!.visibility = View.VISIBLE
        getOldPassword!!.visibility = View.GONE
        getNewPassword!!.visibility = View.GONE

        btnChangeEmail!!.visibility = View.VISIBLE
        btnChangePassword!!.visibility = View.GONE
        btnSendResetEmail!!.visibility = View.GONE
        btnRemoveUser!!.visibility = View.GONE
    }

    private fun actionChangePassword() {
        getOldEmail!!.visibility = View.GONE
        getNewEmail!!.visibility = View.GONE
        getOldPassword!!.visibility = View.GONE
        getNewPassword!!.visibility = View.VISIBLE

        btnChangeEmail!!.visibility = View.GONE
        btnChangePassword!!.visibility = View.VISIBLE
        btnSendResetEmail!!.visibility = View.GONE
        btnRemoveUser!!.visibility = View.GONE

    }

    private fun actionReset() {
        getOldEmail!!.visibility = View.VISIBLE
        getNewEmail!!.visibility = View.GONE
        getOldPassword!!.visibility = View.GONE
        getNewPassword!!.visibility = View.GONE

        btnChangeEmail!!.visibility = View.GONE
        btnChangePassword!!.visibility = View.GONE
        btnSendResetEmail!!.visibility = View.VISIBLE
        btnRemoveUser!!.visibility = View.GONE
    }

    private fun actionRemoveUser() {
        getOldEmail!!.visibility = View.GONE
        getNewEmail!!.visibility = View.GONE
        getOldPassword!!.visibility = View.GONE
        getNewPassword!!.visibility = View.GONE

        btnChangeEmail!!.visibility = View.GONE
        btnChangePassword!!.visibility = View.GONE
        btnSendResetEmail!!.visibility = View.GONE
        btnRemoveUser!!.visibility = View.VISIBLE
    }



    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }
}