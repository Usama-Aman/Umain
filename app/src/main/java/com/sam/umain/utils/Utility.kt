package com.sam.umain.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object Utility {

    fun replaceFragment(
        activity: AppCompatActivity,
        fragmentContainer: Int,
        fragment: Fragment,
        isBackStack: Boolean = true,
        sharedView: View? = null,
        elementName: String = ""
    ) {
        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(fragmentContainer, fragment, fragment::class.java.name)
        if (sharedView != null)
            fragmentTransaction.addSharedElement(sharedView, elementName)
        if (isBackStack) {
            fragmentTransaction.addToBackStack(fragment::class.java.name)
        }
        fragmentTransaction.commit()
    }


}

