package com.example.ipolitician.nav.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.firebase.DataAPI

class LoginFragment: Fragment() {

    private lateinit var submit: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        submit = root.findViewById(R.id.login_submit)
        submit.setOnClickListener {
            findNavController().navigateUp()
            findNavController().navigate(R.id.nav_public)
        }
        return root
    }

}