package com.example.ipolitician.Util

import com.example.ipolitician.structures.VocabData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DisposedCode {
//    fun webScrape(){
//        val dicti = "http://www.dictionary.css.ge/"
//        val arr = arrayListOf<String>("ე", "ვ", "კ", "ლ", "ჟ", "ტ", "უ", "ფ", "ღ", "ყ", "ც", "ა", "თ", "ი", "ო", "ბ", "ზ", "მ", "ნ", "პ", "რ", "ს", "შ", "ჩ", "ძ", "წ", "ხ", "ჰ", "გ", "დ")
//        for (str in arr){
//            val document =  Jsoup.connect("http://www.dictionary.css.ge/alpha-inx/$str").get()
//            document.select(".field-content a")    // <2>
//                .map { col -> col.attr("href") }    // <3>
//                .map { extractDictionary(dicti, it) }    // <5>
//                .filterNotNull()
//                .forEach {
//                    println(it.header)
//                    DB.setVocabulary(it)
//                }
//        }
//    }
//
//    private fun extractDictionary(start: String,url: String) : VocabData?{
//        val doc: Document
//        try {
//            doc = Jsoup.connect("$start$url").get()  // <2>
//        }catch (e: Exception){
//            return null
//        }
//        val header = doc.getElementById("page-title").text()
//        val description = doc.getElementsByClass("tex2jax").first().child(0).text()
//        return VocabData(header=header, description=description)
//    }


    //    fun CheckSafetynetreCAPTCHA() {
//        // Showing SafetyNet reCAPTCHA dialog
//        this.activity?.let {
//            SafetyNet.getClient(it).verifyWithRecaptcha(SAFETY_NET_API_KEY)
//                .addOnSuccessListener(this.requireActivity()) { response ->
//                    Log.d(TAG, "onSuccess")
//
//                    if (response.tokenResult.isNotEmpty()) {
//
//                        // Received reCaptcha token and This token still needs to be validated on
//                        // the server using the SECRET key api
//                        verifyTokenFromServer(response.tokenResult).execute()
//                        Log.i(TAG, "onSuccess: " + response.tokenResult)
//                    }
//                }
//                .addOnFailureListener(this.requireActivity()) { e ->
//                    if (e is ApiException) {
//                        Log.d(TAG, "SafetyNet Error: " + CommonStatusCodes.getStatusCodeString(e.statusCode))
//                    } else {
//                        Log.d(TAG, "Unknown SafetyNet error: " + e.message)
//                    }
//                }
//        }
//    }
//
//    /**
//     * Verifying the captcha token on the server
//     * Server makes call to https://www.google.com/recaptcha/api/siteverify
//     * with SECRET Key and SafetyNet token.
//     */
//    @SuppressLint("StaticFieldLeak")
//    private inner class verifyTokenFromServer(var sToken: String) : AsyncTask<String, String, String>() {
//
//        override fun doInBackground(vararg args: String): String {
//
//            // object to hold the information, which is sent to the server
//            val hashMap = HashMap<String, String>()
//            hashMap["recaptcha-response"] = sToken
//            // Optional params you can use like this
//            // hashMap.put("feedback-message", msg)
//
//            // Send the recaptcha response token and receive a Result in return
//            return CaptchaNet.SendParams(VERIFY_ON_API_URL_SERVER, hashMap)
//        }
//
//        override fun onPostExecute(result: String?) {
//
//            if (result == null)
//                return
//
//            Log.i("onPost::: ", result)
//            try {
//                val jsonObject = JSONObject(result)
//                val success = jsonObject.getBoolean("success")
//                val message = jsonObject.getString("message")
//
//                if (success) {
//                    // reCaptcha verified successfully.
//                    activity?.showAlertDialogWithAutoDismiss("aeeeeeee")
//
//                } else {
//                    activity?.showAlertDialogWithAutoDismiss(message)
//                }
//
//            } catch (e: JSONException) {
//                e.printStackTrace()
//                Log.i("Error: ", e.message)
//            }
//
//        }
//    }
//
//    companion object {
//        private val TAG = "reCAPTCHA_Activity"
//
//        // TODO: replace the reCAPTCHA KEY with yours
//        private val SAFETY_NET_API_KEY = "6LfkB_QUAAAAAJwkZeYpCNUoIM6bq8NhxbNfDZ_S"
//
//        // TODO: replace the SERVER API URL with yours
//        private val VERIFY_ON_API_URL_SERVER = "https://voters.cec.gov.ge/index.php"
//    }
}