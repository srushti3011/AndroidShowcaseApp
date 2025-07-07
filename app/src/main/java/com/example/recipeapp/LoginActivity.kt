package com.example.recipeapp

import android.graphics.Color
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeapp.databinding.ActivityLoginBinding
import com.example.recipeapp.model.ErrorState
import com.example.recipeapp.model.Idle
import com.example.recipeapp.model.Loading
import com.example.recipeapp.model.LoginInputError
import com.example.recipeapp.model.Success
import com.example.recipeapp.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupInputFields()
        setupButtonSignIn()
        setupObservers()
        setSpannableString()
    }

    private fun setupInputFields() {
        binding.apply {
            etEmail.setEditTextType(
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            )
            etEmail.setHint("Enter email")
            etPassword.setHint("Enter password")
            etPassword.setImeAction(EditorInfo.IME_ACTION_DONE)
        }
    }

    private fun setupButtonSignIn() {
        binding.apply {
            btnSignIn.setOnClickListener {
                viewModel.validateInput(etEmail.getText(), etPassword.getText())
            }
        }
    }

    private fun setupObservers() {
        setupInputDataValidationObserver()
        setupApiStateObserver()
    }

    private fun setupInputDataValidationObserver() {
        viewModel.loginErrorState.observe(this) {
            Log.i("TAG", it.toString())
            when (it) {
                LoginInputError.EmailAndPasswordEmpty -> {
                    binding.etEmail.showError("Email is empty")
                    binding.etPassword.showError("Password is empty")
                    binding.etEmail.requestFocus()
                }

                LoginInputError.EmailEmpty -> {
                    binding.etEmail.showError("Email is empty")
                    binding.etEmail.requestFocus()
                }

                LoginInputError.NotValidEmail -> {
                    binding.etEmail.showError("Email is not valid")
                    binding.etEmail.requestFocus()
                }

                LoginInputError.PasswordEmpty -> {
                    binding.etPassword.showError("Password is empty")
                    binding.etPassword.requestFocus()
                }

                is LoginInputError.NoError -> {
                    viewModel.connectUser(
                        it.email,
                        it.password
                    )
                }
            }
        }
    }

    private fun setupApiStateObserver() {
        viewModel.loginApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                    binding.btnSignIn.revertAnimation()
                    makeAlert("Api failed") {
                        binding.btnSignIn.text = ContextCompat.getString(
                            this,
                            R.string.login_sign_in_button
                        )
                        binding.btnSignIn.isEnabled = true
                    }
                }

                is Idle -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                }

                is Loading -> {
                    binding.btnSignIn.startAnimation()
                    binding.btnSignIn.text = ContextCompat.getString(
                        this,
                        R.string.loading
                    )
                    binding.btnSignIn.isEnabled = false
                    window.setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE)
                }

                is Success -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                    binding.btnSignIn.revertAnimation()
                    val userPreference = getSharedPreferences(
                        "UserPreferences",
                        MODE_PRIVATE
                    )
                    val editor = userPreference.edit()
                    editor.putString("authKey", it.response.hash)
                    editor.apply()
                    makeAlert("Login successful") {
                        // TODO: After user dismisses the alert box, navigate to home
                    }
                    binding.btnSignIn.text = ContextCompat.getString(
                        this,
                        R.string.login_sign_in_button
                    )
                    binding.btnSignIn.isEnabled = true
                }
            }
        }
    }

    private fun makeAlert(message: String, onClickButton: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Error in input")
            .setMessage(message)
            .setNeutralButton("Ok") { dialog, _ ->
                dialog.cancel()
                onClickButton()
            }
            .create()
            .show()
    }

    private fun setSpannableString() {
        ViewCompat.enableAccessibleClickableSpanSupport(binding.tvGoToSignup)
        val spannableString = SpannableString(binding.tvGoToSignup.text.toString())
        val colorToApply = ContextCompat.getColor(this, R.color.colorTertiary)
        val foregroundSpan = ForegroundColorSpan(colorToApply)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.apply {
            setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(foregroundSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvGoToSignup.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
        }
    }
}