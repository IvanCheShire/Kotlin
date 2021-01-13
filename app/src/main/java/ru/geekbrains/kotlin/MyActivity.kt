package ru.geekbrains.kotlin

import android.app.Activity
import android.widget.TextView
import android.os.Bundle
import android.view.View
import ru.geekbrains.kotlin.R

class MyActivity : Activity() {
    private var mTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mTextView = findViewById<View>(R.id.textView) as TextView
    }

    fun onClick(view: View?) {
        mTextView!!.text = "Здравствуй товарищ!"
    }
}