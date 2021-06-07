package com.example.ipolitician.nav.auth

import android.content.res.ColorStateList
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.Auth.Authenticate
import com.example.ipolitician.R
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.backgroundColor
import com.example.ipolitician.componentColor
import com.example.ipolitician.firebase.DataAPI

class LoginFragment: Fragment() {

    private lateinit var authenticate: Authenticate
    private lateinit var phoneText: TextView
    private lateinit var phoneEdit: EditText
    private lateinit var codeText: TextView
    private lateinit var codeEdit: EditText
    private lateinit var logIn: Button
    private lateinit var signUp: Button
    private lateinit var submit: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        retrieveViews(root)
        configureVisibility(false, false)
        authenticate = Authenticate(this.requireActivity() as AppCompatActivity, this)

        logIn.setOnClickListener {
            setBtnColors(logIn, signUp)
            configureVisibility(false, false)
        }

        signUp.setOnClickListener {
            setBtnColors(signUp, logIn)
            configureVisibility(true,false)
        }

        submit.setOnClickListener {
            if (phoneEdit.isVisible && codeEdit.isVisible) {
                authenticate.verifyPhoneNumberWithCode(authenticate.storedVerificationId, codeEdit.text.toString())
            } else if (phoneEdit.isVisible){
                authenticate.startPhoneNumberVerification(phoneEdit.text.toString())
            } else {
//                findNavController().navigateUp()
//                findNavController().navigate(R.id.nav_public)
            }
        }
        return root
    }

    fun authenticationComplete(){
        findNavController().navigate(R.id.nav_profile)
//        findNavController().navigateUp()
//        findNavController().navigate(R.id.nav_public)
    }

    private fun setBtnColors(active: Button, disabled: Button){
        active.backgroundTintList = ColorStateList.valueOf(componentColor.data)
        disabled.backgroundTintList = ColorStateList.valueOf(backgroundColor.data)
    }

    fun configureVisibility(phone: Boolean, code: Boolean){
        phoneText.visibility = if (phone) View.VISIBLE else View.GONE
        phoneEdit.visibility = if (phone) View.VISIBLE else View.GONE
        codeText.visibility = if (code) View.VISIBLE else View.GONE
        codeEdit.visibility = if (code) View.VISIBLE else View.GONE
    }

    private fun retrieveViews(root: View){
        logIn = root.findViewById(R.id.loginBtn)
        signUp = root.findViewById(R.id.signUpBtn)
        phoneText = root.findViewById(R.id.textView21)
        phoneEdit = root.findViewById(R.id.editTextPhone)
        codeText = root.findViewById(R.id.textView22)
        codeEdit = root.findViewById(R.id.phoneCode)
        submit = root.findViewById(R.id.login_submit)
    }

}