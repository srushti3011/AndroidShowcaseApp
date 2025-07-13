package com.example.recipeapp.authenticationflow.view

import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeapp.R
import com.example.recipeapp.authenticationflow.model.ErrorType
import com.example.recipeapp.authenticationflow.viewmodel.LoginViewModel
import com.example.recipeapp.databinding.ActivityLoginBinding
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Idle
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import com.google.android.material.snackbar.Snackbar
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
            etEmail.setHint(ContextCompat.getString(this@LoginActivity, R.string.email_hint))
            etPassword.setHint(ContextCompat.getString(this@LoginActivity, R.string.password_hint))
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
        viewModel.loginInputError.observe(this) {
            binding.apply {
                when (it.emailErrorType) {
                    is ErrorType.FieldIsEmptyError -> etEmail
                        .showError((it.emailErrorType as ErrorType.FieldIsEmptyError).message)

                    is ErrorType.ValidationError -> etEmail
                        .showError((it.emailErrorType as ErrorType.ValidationError).message)
                    null -> {}
                }

                when (it.passwordErrorType) {
                    is ErrorType.FieldIsEmptyError -> etPassword
                        .showError((it.passwordErrorType as ErrorType.FieldIsEmptyError).message)
                    is ErrorType.ValidationError -> etPassword
                        .showError((it.passwordErrorType as ErrorType.ValidationError).message)
                    null -> {}
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
                    closeKeyboard()
                }

                is Success -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                    binding.btnSignIn.revertAnimation()
                    Snackbar.make(binding.root, "Login successful", Snackbar.LENGTH_LONG).show()
                    binding.btnSignIn.text = ContextCompat.getString(
                        this,
                        R.string.login_sign_in_button
                    )
                    binding.btnSignIn.isEnabled = true
                    binding.apply {
                        listOf(etEmail, etPassword).forEach { it1 ->
                            it1.setText("")
                        }
                    }
                }
            }
        }
    }

    private fun makeAlert(message: String, onClickButton: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Alert")
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
                viewModel.signUpClicked(this@LoginActivity)
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

    private fun closeKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(
                currentFocus?.windowToken,
                0
            )
    }
}