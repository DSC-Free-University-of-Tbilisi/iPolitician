package com.example.ipolitician.nav.webview

import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

interface WebViewFragment {
    fun ceskoSuccess(name: String, surname: String, birthDate: String, address: String, lat: String, lng: String, img: String)
    fun close()
    fun error(err: String)

    fun configureWebView(fragment: WebViewFragment, webView: WebView){
        webView.settings.javaScriptEnabled = true
        webView!!.addJavascriptInterface(JsWebInterface(fragment), "androidApp")
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
                        "if(target.children.length == 0){ return; }" +

                        "androidApp.close();" +
                        "var p = [...target.children].find((elem) => elem.tagName === \"P\");" +
                        "var div = [...target.children].find((elem) => elem.tagName === \"DIV\");" +
                        "if(p != null){ androidApp.error(p.innerHTML); }" +
                        "if(div != null){" +
                        "var name = document.getElementsByClassName(\"fn\")[0].innerHTML;" +
                        "var surname = document.getElementsByClassName(\"sn\")[0].innerHTML;" +
                        "var birth = document.getElementsByClassName(\"dob\")[0].innerHTML;" +
                        "var address = document.getElementsByClassName(\"mis\")[0].innerHTML;" +
                        "var lat = document.getElementById(\"lat\").innerHTML;" +
                        "var lng = document.getElementById(\"lng\").innerHTML;" +
                        "var img = document.getElementsByClassName(\"photo\")[0].firstChild.src;" +
                        "androidApp.ceskoSuccess(name, surname, birth, address, lat, lng, img);" +
                        "}});" +
                        "});"+
                        "var config = { childList: true };" +
                        "observer.observe(target, config);" +
                        "})")
            }
        }

        webView.loadUrl("https://voters.cec.gov.ge/")
    }

    fun activateListener(webView: WebView, pID: String, surname: String){
        webView.loadUrl(
            "javascript:(function(){" +
                    "document.getElementById(\"pn\").value = \"$pID\";" +
                    "document.getElementById(\"sn\").value = \"$surname\";" +

                    "let closeInt = function (){clearInterval(inter);};" +
                    "var inter = setInterval(() => {" +
                    "if(document.getElementById(\"g-recaptcha-response\").value != \"\"){" +
                    "document.getElementById(\"submit\").click();" +
                    "closeInt();" +
                    "}}, 500);" +
                    "})()"
        )
    }

    fun clickSearch(webView: WebView, pID: String, surname: String){
        webView.loadUrl(
            "javascript:(function(){" +
                    "document.getElementById(\"pn\").value = \"$pID\";" +
                    "document.getElementById(\"sn\").value = \"$surname\";" +
                    "document.getElementById(\"submit\").click();" +
                    "})()"
        )
    }

    class JsWebInterface(private val fragment: WebViewFragment) {
        @JavascriptInterface
        fun ceskoSuccess(name: String, surname: String, birthDate: String, address: String, lat: String, lng: String, img: String) {
            CoroutineScope(Dispatchers.Main).async {
                fragment.ceskoSuccess(name, surname, birthDate, address, lat, lng, img)
            }
        }

        @JavascriptInterface
        fun close() {
            CoroutineScope(Dispatchers.Main).async {
                fragment.close()
            }
        }

        @JavascriptInterface
        fun error(err: String) {
            CoroutineScope(Dispatchers.Main).async {
                fragment.error(err)
            }
        }
    }
}