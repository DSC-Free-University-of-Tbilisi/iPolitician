package com.example.ipolitician.nav.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.animation.DecelerateInterpolator
import android.widget.*
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
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.material.textfield.TextInputLayout
import com.richpath.RichPath
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text
import kotlin.properties.Delegates

class LoginFragment: Fragment() {

    enum class LoginState { LOGIN, CESKOCHECK, PHONENUM, PHONECODE }

    lateinit var authenticate: Authenticate
    lateinit var personId: EditText
    lateinit var surnameTxt: TextView
    lateinit var surnameEdit: EditText
    lateinit var password: TextInputLayout
    lateinit var phoneText: TextView
    lateinit var phoneEdit: EditText
    lateinit var codeText: TextView
    lateinit var codeEdit: EditText
    lateinit var logIn: Button
    lateinit var signUp: Button
    lateinit var submit: Button
    lateinit var inputs: LinearLayout
    lateinit var webView: WebView
    private lateinit var georgia: RichPathView
    private val DB = DataAPI()
    private var time = 0
    var prevState = LoginState.CESKOCHECK
    var currState by Delegates.observable(LoginState.LOGIN) { property, old, new ->
        Log.d("aeeee", "$old $new")
        CoroutineScope(Dispatchers.Main).async {
            configureVisibility()
            inputs.invalidate()
        }
        prevState = old
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        retrieveViews(root)
        animateGeorgia()

        currState = LoginState.LOGIN
        prevState = LoginState.CESKOCHECK
        authenticate = Authenticate(this.requireActivity() as AppCompatActivity, this)

        logIn.setOnClickListener {
            setBtnColors(logIn, signUp)
            currState = LoginState.LOGIN
        }

        signUp.setOnClickListener {
            setBtnColors(signUp, logIn)
            currState = prevState
        }

        configureWebView()
        configureSubmit()

        webView.loadUrl("https://voters.cec.gov.ge/")

        return root
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView!!.addJavascriptInterface(JsWebInterface(this), "androidApp")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean { return true }

            override fun onPageFinished(view: WebView?, url: String?) {
                view?.loadUrl("javascript:(window.onload = function(){" +
                        "var bChildren = document.getElementsByTagName(\"body\")[0].children;" +
                        "for (let element of bChildren) {" +
                            "element.style.display = \"none\";" +
                        "}" +
                        "document.getElementsByClassName(\"envelope\")[1].style.display = \"block\";" +
                        "document.getElementById(\"numonly\").style.display = \"none\";" +
                        "document.getElementById(\"sn\").style.display = \"none\";" +
                        "document.getElementById(\"submit\").style.display = \"none\";" +
                        "document.getElementsByTagName(\"body\")[0].insertAdjacentHTML(\'beforeend\', '<style>.envelope {border:none; padding: 0;} center {display:none;}</style>');" +
                        "document.getElementsByTagName(\"body\")[0].insertAdjacentHTML(\'beforeend\', '<style>#votersform {margin: 0 0; width: auto;}</style>');" +
                        "document.getElementsByTagName(\"body\")[0].insertAdjacentHTML(\'beforeend\', '<style>.g-recaptcha {display: flex; justify-content:center;} .g-recaptcha div div {border: none}</style>');" +

                        "document.getElementsByTagName(\"head\")[0].insertAdjacentHTML(\'beforeend\', \'<meta name=\"mobile-web-app-capable\" content=\"yes\">\');" +
                        "document.getElementsByTagName(\"head\")[0].insertAdjacentHTML(\'beforeend\', \'<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\');" +

                        "var target = document.getElementById(\"page-wrap\");" +
                        "var observer = new MutationObserver(function (mutations) {" +
                        "mutations.forEach(function (mutation) {" +
                            "androidApp.testi(target.children.length);" +
                            "if(target.children.length > 2){" +
                                "androidApp.close();" +
                                "var name = document.getElementsByClassName(\"sn\")[0].innerHTML;" +
                                "var surname = document.getElementsByClassName(\"fn\")[0].innerHTML;" +
                                "var birth = document.getElementsByClassName(\"dob\")[0].innerHTML;" +
                                "var address = document.getElementsByClassName(\"mis\")[0].innerHTML;" +
                                "var lat = document.getElementById(\"lat\").innerHTML;" +
                                "var lng = document.getElementById(\"lng\").innerHTML;" +
                                "androidApp.ceskoSuccess(name, surname, birth, address, lat, lng);" +

//                            "} else {" +
//                                "androidApp.testi(-1);" +
                            "}});" +
                        "});"+
                        "var config = { childList: true };" +
                        "observer.observe(target, config);" +
                        "})")
            }
        }
    }

    private fun configureSubmit() {
        submit.setOnClickListener {

//            activity?.showAlertDialogWithAutoDismiss("Personal number used or illegal!")
            when (currState) {
                LoginState.LOGIN ->
                    loginAttempt()
                LoginState.CESKOCHECK -> {
                    val pID = personId.text.toString()
                    val surname = surnameEdit.text.toString()
                    webView.visibility = View.VISIBLE
                    webView.loadUrl(
                        "javascript:(function(){" +
                                "document.getElementById(\"pn\").value = \"$pID\";" +
                                "document.getElementById(\"sn\").value = \"$surname\";" +

                                "setInterval(() => {" +
                                "if(document.getElementById(\"g-recaptcha-response\").value != \"\"){" +
                                "document.getElementById(\"submit\").click();" +
                                "}}, 500);" +
                                "})()"
                    )
                }
                LoginState.PHONENUM ->
                    authenticate.startPhoneNumberVerification(phoneEdit.text.toString())
                LoginState.PHONECODE ->
                    if (time != 0){
                        authenticate.verifyPhoneNumberWithCode(authenticate.storedVerificationId, codeEdit.text.toString())
                    } else {
                        authenticate.resendVerificationCode(phoneEdit.text.toString(), authenticate.resendToken)
                        submit.text = "SUBMIT"
                    }
            }
            hideKeyboard()
        }
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

    class JsWebInterface(private val fragment: LoginFragment) {
        @JavascriptInterface
        fun ceskoSuccess(name: String, surname: String, birthDate: String, address: String, lat: String, lng: String) {
            fragment.currState = LoginState.PHONENUM
            Log.d("aeeee", name)
        }

        @JavascriptInterface
        fun close() {
            CoroutineScope(Dispatchers.Main).async {
                fragment.webView.visibility = View.GONE
            }
        }

        @JavascriptInterface
        fun testi(ln: Int) {
            Log.d("aeeee", ln.toString())
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

    fun configureVisibility(){
        surnameTxt.visibility = if (currState in arrayOf(LoginState.CESKOCHECK, LoginState.PHONENUM, LoginState.PHONECODE)) View.VISIBLE else View.GONE
        surnameEdit.visibility = if (currState in arrayOf(LoginState.CESKOCHECK, LoginState.PHONENUM, LoginState.PHONECODE)) View.VISIBLE else View.GONE
        phoneText.visibility = if (currState in arrayOf(LoginState.PHONENUM, LoginState.PHONECODE)) View.VISIBLE else View.GONE
        phoneEdit.visibility = if (currState in arrayOf(LoginState.PHONENUM, LoginState.PHONECODE)) View.VISIBLE else View.GONE
        codeText.visibility = if (currState in arrayOf(LoginState.PHONECODE)) View.VISIBLE else View.GONE
        codeEdit.visibility = if (currState in arrayOf(LoginState.PHONECODE)) View.VISIBLE else View.GONE
    }

    private fun retrieveViews(root: View){
        logIn = root.findViewById(R.id.loginBtn)
        signUp = root.findViewById(R.id.signUpBtn)
        personId = root.findViewById(R.id.editTextTextPersonName)
        surnameTxt = root.findViewById(R.id.surnameText)
        surnameEdit = root.findViewById(R.id.editSurname)
        password = root.findViewById(R.id.editTextTextPersonName2)
        phoneText = root.findViewById(R.id.textView21)
        phoneEdit = root.findViewById(R.id.editTextPhone)
        inputs = root.findViewById(R.id.inputs)
        codeText = root.findViewById(R.id.textView22)
        codeEdit = root.findViewById(R.id.phoneCode)
        submit = root.findViewById(R.id.login_submit)
        webView = root.findViewById(R.id.webView)
        georgia = root.findViewById(R.id.georgia_back)
    }
}