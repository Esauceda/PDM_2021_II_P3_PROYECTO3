package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar

import androidx.appcompat.app.AppCompatActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R

class MyToolbar {
    fun show(activities: AppCompatActivity, title:String, upButton:Boolean){
        activities.setSupportActionBar(activities.findViewById(R.id.toolbar))
        activities.supportActionBar?.title = title
        activities.supportActionBar?.setDisplayHomeAsUpEnabled(upButton)
    }

}