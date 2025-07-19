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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeapp.BaseActivity
import com.example.recipeapp.R
import com.example.recipeapp.authenticationflow.model.ErrorType
import com.example.recipeapp.authenticationflow.viewmodel.SignupViewModel
import com.example.recipeapp.databinding.ActivitySignUpBinding
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Idle
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupInputFields()
        setupSignUpClick()
        setupObservers()
        setupSpannableString()
    }

    private fun setupInputFields() {
        binding.apply {
            etName.apply {
                setEditTextType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                )
                setHint(ContextCompat.getString(this@SignUpActivity, R.string.name_hint))
            }

            etEmail.apply {
                setEditTextType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                )
                setHint(ContextCompat.getString(this@SignUpActivity, R.string.email_hint))
            }
            etPassword.apply {
                setEditTextType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
                setHint(ContextCompat.getString(this@SignUpActivity, R.string.password_hint))
            }
            etConfirmPassword.apply {
                setEditTextType(
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
                setImeAction(EditorInfo.IME_ACTION_DONE)
                setHint(
                    ContextCompat.getString(
                        this@SignUpActivity,
                        R.string.confirm_password_hint
                    )
                )
            }
        }
    }

    private fun setupSignUpClick() {
        binding.apply {
            btnSignUp.setOnClickListener {
                listOf(etName, etEmail, etPassword, etConfirmPassword).forEach {
                    it.hideError()
                }
                viewModel.validateInput(
                    name = etName.getText(),
                    email = etEmail.getText(),
                    password = etPassword.getText(),
                    confirmPassword = etConfirmPassword.getText(),
                    conditionsAccepted = checkBoxAcceptConditions.isChecked
                )
            }
        }
    }

    private fun setupObservers() {
        setupSignupInputDataValidationObserver()
        setupSignupApiStateObserver()
    }

    private fun setupSignupInputDataValidationObserver() {
        viewModel.signupInputError.observe(this) {
            binding.apply {
                when (it.nameError) {
                    is ErrorType.FieldIsEmptyError -> {
                        (it.nameError as ErrorType.FieldIsEmptyError).message
                            .let { it1 -> etName.showError(it1) }
                    }
                    is ErrorType.ValidationError -> {
                        (it.nameError as ErrorType.ValidationError).message
                            .let { it1 -> etName.showError(it1) }
                    }
                    null -> {}
                }

                when (it.emailError) {
                    is ErrorType.FieldIsEmptyError -> {
                        (it.emailError as ErrorType.FieldIsEmptyError).message
                            .let { it1 -> etEmail.showError(it1) }
                    }
                    is ErrorType.ValidationError -> {
                        (it.emailError as ErrorType.ValidationError).message
                            .let { it1 -> etEmail.showError(it1) }
                    }
                    null -> {}
                }

                when (it.passwordError) {
                    is ErrorType.FieldIsEmptyError -> {
                        (it.passwordError as ErrorType.FieldIsEmptyError).message
                            .let { it1 -> etPassword.showError(it1) }
                    }
                    is ErrorType.ValidationError -> {
                        (it.passwordError as ErrorType.ValidationError).message
                            .let { it1 -> etPassword.showError(it1) }
                    }
                    null -> {}
                }

                when (it.confirmPasswordError) {
                    is ErrorType.FieldIsEmptyError -> {
                        (it.confirmPasswordError as ErrorType.FieldIsEmptyError).message
                            .let { it1 -> etConfirmPassword.showError(it1) }
                    }
                    is ErrorType.ValidationError -> {
                        (it.confirmPasswordError as ErrorType.ValidationError).message
                            .let { it1 -> etConfirmPassword.showError(it1) }
                    }
                    null -> {}
                }

                if (it.conditionCheckedError) {
                    Snackbar.make(
                        binding.root,
                        "Can't proceed without accepting terms and conditions",
                        Snackbar.LENGTH_LONG
                    ).setAction("Close") {}.show()
                }
            }
        }
    }

    private fun setupSignupApiStateObserver() {
        viewModel.signupApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                    generateToast("Api failed - $it")
                    binding.btnSignUp.apply {
                        revertAnimation()
                        text = ContextCompat.getString(
                            this@SignUpActivity,
                            R.string.sign_up_button
                        )
                        isEnabled = true
                    }
                }

                is Idle -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                }

                is Loading -> {
                    binding.btnSignUp.apply {
                        startAnimation()
                        text = ContextCompat.getString(
                            this@SignUpActivity,
                            R.string.loading
                        )
                        isEnabled = false
                    }
                    closeKeyboard()
                }

                is Success -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                    Snackbar.make(
                        binding.root,
                        "Signup successful",
                        Snackbar.LENGTH_LONG
                    ).show()
                    binding.btnSignUp.apply {
                        revertAnimation()
                        text = ContextCompat.getString(
                            this@SignUpActivity,
                            R.string.sign_up_button
                        )
                        isEnabled = true
                    }
                    binding.apply {
                        listOf(etName, etEmail, etPassword, etConfirmPassword).forEach { it1 ->
                            it1.setText("")
                        }
                    }
                }
            }
        }
    }

    private fun generateToast(message: String) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupSpannableString() {
        ViewCompat.enableAccessibleClickableSpanSupport(binding.tvGoToSignIn)
        val spannableString = SpannableString(binding.tvGoToSignIn.text.toString())
        val colorToApply = ContextCompat.getColor(this, R.color.colorTertiary)
        val foregroundSpan = ForegroundColorSpan(colorToApply)
        val clickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                viewModel.logInClicked(this@SignUpActivity)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickSpan, 18, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(foregroundSpan, 18, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvGoToSignIn.apply {
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