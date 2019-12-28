package com.sandytech.sandytechnews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import android.widget.ProgressBar
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText

class LoginRegisterActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var auth: FirebaseAuth? = null
    private var progressBar: ProgressBar? = null
    private var btnSignup: Button? = null
    private var btnLogin: Button? = null
    private var btnReset: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        auth = FirebaseAuth.getInstance()

        if (auth!!.currentUser != null) {
            startActivity(Intent(this@LoginRegisterActivity, MainActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login_register)

        inputEmail = findViewById<EditText>(R.id.email)
        inputPassword = findViewById<EditText>(R.id.inputOldPassword)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        btnSignup = findViewById<Button>(R.id.sign_up_button)
        btnLogin = findViewById<Button>(R.id.btn_login)
        btnReset = findViewById<Button>(R.id.btn_reset_password)

        btnLogin!!.setOnClickListener { userLoginIn() }
        btnSignup!!.setOnClickListener { userRegister() }
        btnReset!!.setOnClickListener { userForgotPassword() }
    }

    private fun userLoginIn() {
        val email = inputEmail!!.text.toString()
        val password = inputPassword!!.text.toString()

        when {
            TextUtils.isEmpty(email) -> inputEmail!!.error = "Enter email address!"

            TextUtils.isEmpty(password) -> inputPassword!!.error = "Enter password!"

            else -> {
                progressBar!!.visibility = View.VISIBLE

                auth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this@LoginRegisterActivity
                    ) { task ->
                        progressBar!!.visibility = View.GONE
                        if (!task.isSuccessful) {
                            if (password.length < 6) {
                                inputPassword!!.error = "Password too short, enter minimum 6 characters!"
                            } else {
                                inputPassword!!.error = "Authentication failed, Check your Email and Password or Sign Up"
                            }
                        } else {
                            val intent = Intent(this@LoginRegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        }
    }

    private fun userRegister() {
        val email = inputEmail!!.text.toString().trim { it <= ' ' }
        val password = inputPassword!!.text.toString().trim { it <= ' ' }

        when {
            TextUtils.isEmpty(email) -> inputEmail!!.error = "Enter email address!"

            TextUtils.isEmpty(password) -> inputPassword!!.error = "Enter password!"

            password.length < 6 -> inputPassword!!.error = "Password too short, enter minimum 6 characters!"

            else -> {
                progressBar!!.visibility = View.VISIBLE

                auth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                        this@LoginRegisterActivity
                    ) { task ->
                        Toast.makeText(
                            this@LoginRegisterActivity,
                            "Account Created. Here you go to next activity." + task.isSuccessful,
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar!!.visibility = View.GONE

                        if (!task.isSuccessful) {
                            Toast.makeText(
                                this@LoginRegisterActivity, "Authentication failed." + task.exception!!,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            startActivity(Intent(this@LoginRegisterActivity, MainActivity::class.java))
                            finish()
                        }
                    }
            }
        }
    }

    private fun userForgotPassword() {
        val email = inputEmail!!.text.toString().trim { it <= ' ' }

        if (TextUtils.isEmpty(email)) {
           inputEmail!!.error = "Enter email address!"
        }

        else {
            progressBar!!.visibility = View.VISIBLE
            auth!!.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@LoginRegisterActivity,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LoginRegisterActivity,
                            "Failed to send reset email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    progressBar!!.visibility = View.GONE
                }
        }
    }
}
