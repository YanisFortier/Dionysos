package com.yfortier.dionysos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yfortier.dionysos.databinding.ActivityChoixBinding


class ChoixActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoixBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix)
        binding = ActivityChoixBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabChoix.setOnClickListener {
            startActivity(Intent(applicationContext, CreerEvenementActivity::class.java))
        }

    }
}