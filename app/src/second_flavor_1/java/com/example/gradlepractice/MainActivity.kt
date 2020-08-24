package com.example.gradlepractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.secondflavor.SecondFlavor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val test1 = SecondFlavor()
        text.text = test1.getText()
    }
}