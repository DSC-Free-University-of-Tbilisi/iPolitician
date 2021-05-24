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
}