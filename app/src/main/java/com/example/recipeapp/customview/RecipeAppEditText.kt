package com.example.recipeapp.customview

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.example.recipeapp.R
import com.example.recipeapp.databinding.RecipeAppEditTextBinding

class RecipeAppEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: RecipeAppEditTextBinding
    private var isPasswordVisible = false

    init {
        val inflater = LayoutInflater.from(context)
        binding = RecipeAppEditTextBinding.inflate(inflater, this, true)

        orientation = VERTICAL

        binding.ivTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.etInput.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                hideError()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun setEditTextType(type: Int = InputType.TYPE_CLASS_TEXT) {
        binding.etInput.inputType = type
        val isPasswordType = (type and InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD
                || (type and InputType.TYPE_NUMBER_VARIATION_PASSWORD) == InputType.TYPE_NUMBER_VARIATION_PASSWORD
                || (type and InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        binding.ivTogglePassword.visibility = if (isPasswordType) View.VISIBLE else View.GONE
        if (!isPasswordType) {
            isPasswordVisible = false
        }
    }

    private fun togglePasswordVisibility() {
        val input = binding.etInput
        val pos = input.selectionStart
        if (isPasswordVisible) {
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.ivTogglePassword.setImageResource(R.drawable.password_hide)
        } else {
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.ivTogglePassword.setImageResource(R.drawable.password_show)
        }
        isPasswordVisible = !isPasswordVisible
        input.setSelection(pos)
    }

    fun getText(): String = binding.etInput.text.toString()

    fun setHint(hint: String) {
        binding.etInput.hint = hint
    }

    fun showError(msg: String) {
        binding.tvError.text = msg
        binding.tvError.visibility = View.VISIBLE
    }

    fun hideError() {
        binding.tvError.text = ""
        binding.tvError.visibility = View.GONE
    }

    fun setImeAction(action: Int) {
        binding.etInput.imeOptions = action
    }
}
