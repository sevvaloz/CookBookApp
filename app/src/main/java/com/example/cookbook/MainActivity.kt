package com.example.cookbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //Add Cook Menu'sunu Main Activity'e bağlama
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInf = menuInflater
        menuInf.inflate(R.menu.add_cook, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Menu'den bir item seçildiğinde yapılacakları ayarlama
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addcookbuttonid){
            val action = CookListFragmentDirections.actionCookListFragmentToAddCookFragment("cameFromMenu", 0)
            Navigation.findNavController(this, R.id.fragmentContainerView).navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }
}