package com.example.ipolitician.firebase

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class QA(
    val question: String = "",
    val answers: List<String> = listOf()
)

class FireStore {
    private val FS = Firebase.firestore
    var qas: MutableList<QA> = ArrayList()

    init {
//        qas.add(QA(question = "rao aba?", answers = listOf("ki", "ara")))
//        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))
//        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))
//        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))
//        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))
//        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))
//        qas.add(QA(question = "ras vaketebt?", answers = listOf("ara", "ki")))

    }

    fun add2FireStore(){
//        db.collection("questions").get().addOnSuccessListener { documents ->
//            for (dc in documents) {
//                Log.d("aeee", "${dc.id}")
//            }
//        }
        qas.forEachIndexed { index, qa ->
            FS.collection("questions").document("question$index")
                .set(qa)
                .addOnSuccessListener { Log.d("aeee", "gaaketa") }
                .addOnFailureListener { Log.d("aeee", "ar gauketebia") }
        }
    }

    fun getFromFireStore()  {
        FS.collection("questions").get().addOnSuccessListener { documents ->
            var qas: ArrayList<QA> = arrayListOf()
            for (dc in documents) {
                Log.d("aeee", "${dc.id}")
                qas.add(dc.toObject(QA::class.java))
            }
        }
    }


}