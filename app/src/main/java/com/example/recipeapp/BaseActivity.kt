package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity: AppCompatActivity(), FinishableActivity {

    override fun finishActivity() {
        this.finish()
    }

    override fun launchIntent(intent: Intent) {
        startActivity(intent)
    }
}