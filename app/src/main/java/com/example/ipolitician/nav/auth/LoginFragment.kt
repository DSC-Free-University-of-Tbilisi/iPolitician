package com.example.ipolitician.nav.auth

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anychart.core.polar.series.Polygon
import com.example.ipolitician.*
import com.example.ipolitician.Auth.Authenticate
import com.example.ipolitician.Util.dialog
import com.example.ipolitician.Util.md5
import com.example.ipolitician.Util.sha256
import com.example.ipolitician.Util.showAlertDialogWithAutoDismiss
import com.example.ipolitician.firebase.DataAPI
import com.example.ipolitician.nav.profile.ProfileFragment
import com.example.ipolitician.nav.webview.WebViewFragment
import com.example.ipolitician.structures.User
import com.google.android.material.textfield.TextInputLayout
import com.google.maps.android.PolyUtil
import com.richpath.RichPath
import com.richpath.RichPath.OnPathClickListener
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlin.properties.Delegates


class LoginFragment: Fragment(), WebViewFragment {

    enum class LoginState { LOGIN, CESKOCHECK, PHONENUM, PHONECODE }
    companion object {
        fun getColor(clrName: String) : Int {
            if(clrName == "YELLOW") return Color.parseColor("#FFBF00")
            if(clrName == "RED") return Color.parseColor("#FF0000")
            if(clrName == "GREEN") return Color.parseColor("#3E9C42")
            if(clrName == "BLUE") return Color.parseColor("#03A9F4")
            return Color.WHITE
        }
    }
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
    lateinit var blurView: View
    lateinit var webView: WebView
    private lateinit var georgia: RichPathView
    private lateinit var appIcon: RichPathView
    private val DB = DataAPI()
    private var time = 0
    lateinit var region: String
    lateinit var optional: List<String>
    var prevState = LoginState.CESKOCHECK
    var currState by Delegates.observable(LoginState.LOGIN) { property, old, new ->
        Log.d("state", "$old $new")
        submit.text = if(new == LoginState.LOGIN) getString(R.string.log_in) else getString(R.string.submit)
        CoroutineScope(Dispatchers.Main).async {
            configureVisibility()
            inputs.invalidate()
        }
        prevState = old
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)
        retrieveViews(root)
        animateLaunch()

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

        configureWebView(this, webView)
        configureSubmit()

        blurView.background.alpha = 220
        return root
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
                    blurView.visibility = View.VISIBLE
                    webView.visibility = View.VISIBLE
                    activateListener(webView, pID, surname)
                }
                LoginState.PHONENUM ->
                    authenticate.startPhoneNumberVerification(phoneEdit.text.toString())
                LoginState.PHONECODE ->
                    if (time != 0){
                        authenticate.verifyPhoneNumberWithCode(authenticate.storedVerificationId, codeEdit.text.toString())
                    } else {
                        authenticate.resendVerificationCode(phoneEdit.text.toString(), authenticate.resendToken)
                        submit.text = getString(R.string.submit)
                    }
            }
            hideKeyboard()
        }
    }


    fun animateLaunch() {
        val dst = 128f
        animateGeorgiaPath(georgia.findRichPathByIndex(0)!!, -dst, -dst)
        animateGeorgiaPath(georgia.findRichPathByIndex(7)!!, -dst/2, -dst/2)
        animateGeorgiaPath(georgia.findRichPathByIndex(10)!!, -dst/4, -dst/4)

        animateGeorgiaPath(georgia.findRichPathByIndex(4)!!, dst, dst)
        animateGeorgiaPath(georgia.findRichPathByIndex(5)!!, dst/2, dst/2)

        animateGeorgiaPath(georgia.findRichPathByIndex(6)!!, dst, -dst)
        animateGeorgiaPath(georgia.findRichPathByIndex(9)!!, dst/2, -dst/2)


        animateGeorgiaPath(georgia.findRichPathByIndex(1)!!, -dst, dst)
        animateGeorgiaPath(georgia.findRichPathByIndex(2)!!, -dst/2, dst/2)
        animateGeorgiaPath(georgia.findRichPathByIndex(3)!!, -dst/2, dst/2)
        animateGeorgiaPath(georgia.findRichPathByIndex(8)!!, -dst/4, dst/4)
        animateGeorgiaPath(georgia.findRichPathByIndex(11)!!, -dst/4, dst/4)

        appIcon.setOnPathClickListener {
            animateIconPath(appIcon.findRichPathByIndex(0)!!, dst, dst)      // YELLOW
            animateIconPath(appIcon.findRichPathByIndex(1)!!, -dst, -dst)     // RED
            animateIconPath(appIcon.findRichPathByIndex(2)!!, dst, -dst)     // GREEN
            animateIconPath(appIcon.findRichPathByIndex(3)!!, -dst, dst)      // BLUE
        }
        animateIconPath(appIcon.findRichPathByIndex(0)!!, dst, dst,6000L)       // YELLOW
        animateIconPath(appIcon.findRichPathByIndex(1)!!, -dst, -dst,6000L)     // RED
        animateIconPath(appIcon.findRichPathByIndex(2)!!, dst, -dst,6000L)      // GREEN
        animateIconPath(appIcon.findRichPathByIndex(3)!!, -dst, dst,6000L)      // BLUE
    }

    fun animateGeorgiaPath(richPath: RichPath, tx: Float, ty: Float) {
        RichPathAnimator.animate(richPath)
            .interpolator(DecelerateInterpolator())
            .fillColor(Color.BLACK)
            .strokeColor(Color.YELLOW)
            .translationX(tx,0f)
            .translationY(ty,0f)
            .duration(6000)
            .startDelay(1)
            .start()
    }

    fun animateIconPath(richPath: RichPath, tx: Float, ty: Float, dly: Long = 0L) {
        RichPathAnimator.animate(richPath)
            .interpolator(DecelerateInterpolator())
            .fillColor(getColor(richPath.name))
            .translationX(tx,0f)
            .translationY(ty,0f)
            .duration(3000)
            .startDelay(dly)
            .start()
    }

    //WebViewFragment
    override fun ceskoSuccess(
        name: String,
        surname: String,
        birthDate: String,
        address: String,
        lat: String,
        lng: String,
        img: String
    ) {
        DB.getUser(personId.text.toString().sha256()) {
            if (it == null){
                currState = LoginState.PHONENUM
                region = retrieveRegion(lat.toDouble(), lng.toDouble())
                optional = listOf(name, surname, birthDate, address, lat, lng)
            } else {
                activity?.showAlertDialogWithAutoDismiss("ასეთი მომხმარებელი უკვე დარეგისტრირებულია!")
            }
        }
    }

    fun retrieveRegion(lat: Double, lng: Double): String {
        for(pair in ProfileFragment.geoLocs){
            if (PolyUtil.containsLocation(lat, lng, pair.value, true)){
                return pair.key
            }
        }
        return ProfileFragment.regions.last()
    }

    override fun close() {
        webView.visibility = View.GONE
        blurView.visibility = View.GONE
    }

    override fun error(err: String) {
        activity?.showAlertDialogWithAutoDismiss(err)
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
                activity?.showAlertDialogWithAutoDismiss("პირადი ნომერი ან პაროლი არასწორია!")
            }
        }
    }

    fun codeSent(){
        time = 60
        val value = object : CountDownTimer(60000, 1000) {
             override fun onTick(p0: Long) {
                 codeText.text = "კოდი ვალიდურია $time წამი"
                 time--
             }
             override fun onFinish() {
                 codeText.text = "ხელახლა ცადე"
                 submit.text = "კოდის გამოგზავნა"
             }
         }.start()
    }

    fun authenticationComplete(){
        val usr = User(
            password = password.editText?.text.toString().md5(),
            phoneNumber = phoneEdit.text.toString(),
            region = region,
            age = ProfileFragment.getAge(optional[2]),
            optional = optional
        )
        DB.setUser(personId.text.toString().sha256(), usr)
        MainActivity.uniqueID = personId.text.toString().sha256()
        MainActivity.user = usr

        ProfileFragment.genders.add(0, ProfileFragment.choose_gender)
        findNavController().navigateUp()
        findNavController().navigate(R.id.nav_profile)
    }

    private fun setBtnColors(active: Button, disabled: Button){
        active.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        disabled.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#f3f3f3"))
    }

    fun configureVisibility(){
        personId.isEnabled = currState in arrayOf(LoginState.CESKOCHECK, LoginState.LOGIN)
        surnameEdit.isEnabled = currState in arrayOf(LoginState.CESKOCHECK, LoginState.LOGIN)
        password.isEnabled = currState in arrayOf(LoginState.CESKOCHECK, LoginState.LOGIN)
        phoneEdit.isEnabled = currState in arrayOf(LoginState.CESKOCHECK, LoginState.LOGIN, LoginState.PHONENUM)
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
        blurView = root.findViewById(R.id.blurView)
        webView = root.findViewById(R.id.webView)
        georgia = root.findViewById(R.id.georgia_back)
        appIcon = root.findViewById(R.id.launch_icon)
    }
}