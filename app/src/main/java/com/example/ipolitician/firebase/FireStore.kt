package com.example.ipolitician.firebase

import android.util.Log
import com.example.ipolitician.structures.QA
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/* Example */
class FireStore {
    private val FS = Firebase.firestore
    var qas: MutableList<QA> = ArrayList()

    init { }

    fun add2FireStore(){
        qas.forEachIndexed { index, qa ->
            FS.collection("questions").document("question$index")
                .set(qa)
                .addOnSuccessListener { Log.d("listener", "yep") }
                .addOnFailureListener { Log.d("listener", "nope") }
        }
    }

    fun getFromFireStore()  {
        FS.collection("questions").get().addOnSuccessListener { documents ->
            var qas: ArrayList<QA> = arrayListOf()
            for (dc in documents) {
                Log.d("added", "${dc.id}")
                qas.add(dc.toObject(QA::class.java))
            }
        }
    }


}