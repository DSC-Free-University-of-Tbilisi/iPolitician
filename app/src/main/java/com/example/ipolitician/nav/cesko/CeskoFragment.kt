package com.example.ipolitician.nav.cesko

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64.DEFAULT
import android.util.Base64.decode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.ipolitician.R
import com.example.ipolitician.Util.showAlertDialogWithAutoDismiss
import com.example.ipolitician.hideKeyboard
import com.example.ipolitician.nav.webview.WebViewFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [CeskoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CeskoFragment : Fragment(), WebViewFragment {

    lateinit var citizen: LinearLayout
    lateinit var citizenImageView: ImageView
    lateinit var citizenName: TextView
    lateinit var citizenSurname: TextView
    lateinit var citizenBirth: TextView
    lateinit var citizenAddr: TextView
    lateinit var personId: EditText
    lateinit var surnameEdit: EditText
    lateinit var webView: WebView
    lateinit var searchBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_cesko, container, false)
        citizen = root.findViewById(R.id.citizen)
        citizenImageView = root.findViewById(R.id.citizenImg)
        citizenName = root.findViewById(R.id.citizenName)
        citizenSurname = root.findViewById(R.id.citizenSurname)
        citizenBirth = root.findViewById(R.id.citizenBirth)
        citizenAddr = root.findViewById(R.id.citizenAddr)
        personId = root.findViewById(R.id.ceskoPIDedit)
        surnameEdit = root.findViewById(R.id.ceskoEditSurname)
        webView = root.findViewById(R.id.cesko_web)
        searchBtn = root.findViewById(R.id.cesko_submit)

        configureWebView(this, webView)
        Timer("SettingUp", false).schedule(object : TimerTask() {
            override fun run() {
                CoroutineScope(Dispatchers.Main).async { webView.visibility = View.VISIBLE }
            }
        }, 2000)

        searchBtn.setOnClickListener {
            hideKeyboard()
            citizen.visibility = View.GONE
            clickSearch(webView, personId.text.toString(), surnameEdit.text.toString())
        }
        return root
    }
    
    override fun ceskoSuccess(
        name: String,
        surname: String,
        birthDate: String,
        address: String,
        lat: String,
        lng: String,
        img: String
    ) {
        citizenName.text = "$name $surname"
        citizenSurname.text = "პ/ნ.  ${personId.text}"
        citizenBirth.text = "დ/თ.  $birthDate"
        citizenAddr.text = address

        val decodedString: ByteArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Base64.getDecoder().decode(img.substring(img.indexOf(',') + 1))
        } else {
            ByteArray(0)
        }
        val bitMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        citizenImageView.setImageBitmap(bitMap)
        citizen.visibility = View.VISIBLE
    }

    override fun close() {}

    override fun error(err: String) {
        activity?.showAlertDialogWithAutoDismiss(err)
    }
}