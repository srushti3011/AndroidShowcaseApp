package com.example.recipeapp

import android.content.Intent
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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeapp.databinding.ActivitySignUpBinding
import com.example.recipeapp.model.SignupInputDetailEmptyFields
import com.example.recipeapp.model.ErrorState
import com.example.recipeapp.model.Idle
import com.example.recipeapp.model.Loading
import com.example.recipeapp.model.SignupInputError
import com.example.recipeapp.model.Success
import com.example.recipeapp.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

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
                setHint(ContextCompat.getString(
                    this@SignUpActivity,
                    R.string.confirm_password_hint)
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
        viewModel.signupInputErrorState.observe(this) {
            when (it) {
                is SignupInputError.DetailsEmpty -> {
                    it.emptyFields.forEach { it1 ->
                        when (it1) {
                            SignupInputDetailEmptyFields.NAME -> {
                                binding.etName.showError("Name is empty")
                            }
                            SignupInputDetailEmptyFields.EMAIL -> {
                                binding.etEmail.showError("Email is empty")
                            }
                            SignupInputDetailEmptyFields.PASSWORD -> {
                                binding.etPassword.showError("Password is empty")
                            }
                            SignupInputDetailEmptyFields.CONFPASSWORD -> {
                                binding.etConfirmPassword.showError(
                                    "Confirm password is empty"
                                )
                            }
                        }
                    }
                }

                SignupInputError.InvalidEmail -> {
                    binding.etEmail.showError("Email not valid")
                }

                SignupInputError.PasswordAndConfirmPasswordNotSame -> {
                    binding.etConfirmPassword.showError(
                        "Confirm password should be same as password"
                    )
                }

                SignupInputError.TermsAndConditionNotAccepted -> {
                    generateToast("Can't proceed as terms and conditions not accepted")
                }

                is SignupInputError.NoError -> {
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
                    window.setFlags(FLAG_NOT_TOUCHABLE, FLAG_NOT_TOUCHABLE)
                }
                is Success -> {
                    window.clearFlags(FLAG_NOT_TOUCHABLE)
                    generateToast("Signup successful")
                    binding.btnSignUp.apply {
                        revertAnimation()
                        text = ContextCompat.getString(
                            this@SignUpActivity,
                            R.string.sign_up_button
                        )
                        isEnabled = true
                    }
                    // TODO: Signifies signup successful, store hash in preference and navigate
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
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
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
}