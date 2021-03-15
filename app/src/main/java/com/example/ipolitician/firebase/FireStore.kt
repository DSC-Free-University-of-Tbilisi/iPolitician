package com.example.ipolitician.firebase

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class QA (
    val question: String = "",
    val answers: List<String> = listOf()
)

class FireStore {
    private val db = Firebase.firestore
    var qas: MutableList<QA> = ArrayList()

    init {
        qas.add(QA(question = "rao aba?", answers = listOf("ki", "ara")))
        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))
    }

    fun add2FireStore(){
//        db.collection("questions").get().addOnSuccessListener { documents ->
//            for (dc in documents) {
//                Log.d("aeee", "${dc.id}")
//            }
//        }
        qas.forEachIndexed { index, qa ->
            db.collection("questions").document("question$index")
                .set(qa)
                .addOnSuccessListener { Log.d("aeee","gaaketa") }
                .addOnFailureListener { Log.d("aeee","ar gauketebia") }
        }
    }

    fun getFromFireStore() {
        var qas: List<QA> = listOf()
        db.collection("questions").get().addOnSuccessListener { documents ->
            for (dc in documents) {
                Log.d("aeee", "${dc.id}")
            }
        }

    }
}