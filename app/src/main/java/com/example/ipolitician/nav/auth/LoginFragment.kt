package com.example.ipolitician.nav.auth

import android.content.res.ColorStateList
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ipolitician.*
import com.example.ipolitician.Auth.Authenticate
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.Util.md5
import com.example.ipolitician.Util.sha256
import com.example.ipolitician.Util.showAlertDialogWithAutoDismiss
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.structures.User
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.richpath.RichPath
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator

class LoginFragment: Fragment() {

    private lateinit var authenticate: Authenticate
    private lateinit var personId: EditText
    private lateinit var password: TextInputLayout
    private lateinit var phoneText: TextView
    private lateinit var phoneEdit: EditText
    private lateinit var codeText: TextView
    private lateinit var codeEdit: EditText
    private lateinit var logIn: Button
    private lateinit var signUp: Button
    private lateinit var submit: Button
    private lateinit var georgia: RichPathView
    private val DB = DataAPI()
    private var time = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        retrieveViews(root)
        animateGeorgia()
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
            if (!validatePersonId()) {
                activity?.showAlertDialogWithAutoDismiss("Personal number used or illegal!")
            } else if (phoneEdit.isVisible && codeEdit.isVisible) {
                if (time != 0){
                    authenticate.verifyPhoneNumberWithCode(authenticate.storedVerificationId, codeEdit.text.toString())
                } else {
                    authenticate.resendVerificationCode(phoneEdit.text.toString(), authenticate.resendToken)
                    submit.text = "SUBMIT"
                }
            } else if (phoneEdit.isVisible){
                authenticate.startPhoneNumberVerification(phoneEdit.text.toString())
            } else {
                loginAttempt()
            }
            hideKeyboard()
        }
        return root
    }

    fun animateGeorgia() {
        for(i in 0..11) {
            val path = georgia.findRichPathByIndex(i)
            if(path!!.name == "აბხაზეთი" || path!!.name == "შიდა ქართლი") {
                RichPathAnimator.animate(path)
                    .interpolator(DecelerateInterpolator())
                    .fillColor(Color.RED)
                    .duration(16000)
                    .startDelay(50)
                    .start()
            }
//            RichPathAnimator.animate(path)
//                .interpolator(DecelerateInterpolator())
//                .strokeColor(Color.YELLOW)
//                .duration(16000)
//                .startDelay(50)
//                .start()
        }

    }

    fun loginAttempt(){
        dialog.show()
        DB.getUser(personId.text.toString().sha256()) {
            dialog.dismiss()
            if (it?.password == password.editText?.text?.toString()?.md5()) {
                MainActivity.uniqueID = personId.text.toString().sha256()
                MainActivity.user = it
                findNavController().navigateUp()
                findNavController().navigate(R.id.nav_public)
            } else {
                activity?.showAlertDialogWithAutoDismiss("Personal number or password invalid!")
            }
        }
    }

    fun codeSent(){
        time = 60
        val value = object : CountDownTimer(60000, 1000) {
             override fun onTick(p0: Long) {
                 codeText.text = "Code sent. Valid for $time seconds"
                 time--
             }
             override fun onFinish() {
                 codeText.text = "Try again"
                 submit.text = "RESEND THE CODE"
             }
         }.start()
    }

    private fun validatePersonId(): Boolean {
        return personId.text.toString().length == 3
    }

    fun authenticationComplete(){
        val usr = User(
            password = password.editText?.text.toString().md5(),
            phoneNumber = phoneEdit.text.toString()
        )
        DB.setUser(personId.text.toString().sha256(), usr)
        MainActivity.uniqueID = personId.text.toString().sha256()
        MainActivity.user = usr
        findNavController().navigateUp()
        findNavController().navigate(R.id.nav_profile)
    }

    private fun setBtnColors(active: Button, disabled: Button){
        active.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        disabled.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f3f3f3"))
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
        personId = root.findViewById(R.id.editTextTextPersonName)
        password = root.findViewById(R.id.editTextTextPersonName2)
        phoneText = root.findViewById(R.id.textView21)
        phoneEdit = root.findViewById(R.id.editTextPhone)
        codeText = root.findViewById(R.id.textView22)
        codeEdit = root.findViewById(R.id.phoneCode)
        submit = root.findViewById(R.id.login_submit)
        georgia = root.findViewById(R.id.georgia_back)
    }

}