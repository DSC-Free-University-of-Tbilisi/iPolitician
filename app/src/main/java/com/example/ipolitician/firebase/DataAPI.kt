package com.example.ipolitician.firebase

import android.util.Log
import com.example.ipolitician.MainActivity
import com.example.ipolitician.structures.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import java.util.concurrent.locks.Lock
import kotlin.collections.ArrayList

class DataAPI : DataAPInterface {

    private val FS = Firebase.firestore

    override fun getUsers(callback: (List<User>) -> Unit) {
        FS.collection("users").get()
            .addOnSuccessListener { documents ->
                var users = ArrayList<User>()
                for (dc in documents) {
                    Log.d("load USERS", dc.toString())
                    users.add(dc.toObject(User::class.java))
                }
                callback(users)
            }.addOnFailureListener {
                callback(ArrayList())
            }
    }

    override fun getUser(user_id: String, callback: (User?) -> Unit) {
        FS.collection("users").document(user_id)
            .get()
            .addOnSuccessListener { user ->
                if(user.exists()) callback(user.toObject(User::class.java))
                else callback(null)
            }.addOnFailureListener {
                callback(null)
            }
    }

    override fun setUser(user_id: String, user: User) {
        FS.collection("users").document(user_id)
            .set(user)
            .addOnSuccessListener {
                setSubmissions(user_id, Selected(selected = arrayListOf(-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1)))
                setUserProblems(user_id, Voted(voted=mutableMapOf()))
                Log.d("listener", "USER SET success")
            }
            .addOnFailureListener { Log.d("listener", "USER SET fail") }
    }

    override fun getSubmissions(user_id: String, callback: (Selected) -> Unit) {
        FS.collection("submissions").document(user_id).get()
            .addOnSuccessListener { document ->
                var selected: Selected = document.toObject(Selected::class.java)!!
                callback(selected)
            }.addOnFailureListener {
                callback(Selected())
            }
    }

    override fun setSubmissions(user_id: String, selected: Selected) {
        FS.collection("submissions").document(user_id)
            .set(selected)
            .addOnSuccessListener { Log.d("listener", "SUBMISSION SET success") }
            .addOnFailureListener { Log.d("listener", "SUBMISSION SET fail") }
    }

    override fun getParties(callback: (ArrayList<Party>) -> Unit) {
        FS.collection("parties").get()
            .addOnSuccessListener { documents ->
                var parties = ArrayList<Party>()
                for (dc in documents) {
                    Log.d("load Party", "${dc.id}")
                    parties.add(dc.toObject(Party::class.java))
                }
                callback(parties)
            }.addOnFailureListener {
                callback(ArrayList())
            }
    }


    override fun getProblem(problem_id: String, callback: (PV?) -> Unit) {
        FS.collection("problems").document(problem_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(PV::class.java)!!)
                else callback(null)
            }.addOnFailureListener {
                callback(null)
            }
    }

    override fun setProblem(problem: PV) {
        FS.collection("problems").document(problem.id)
            .set(problem)
            .addOnSuccessListener { Log.d("listener", "SUBMISSION SET success") }
            .addOnFailureListener { Log.d("listener", "SUBMISSION SET fail") }
    }

    override fun voteProblem(problem_id: String, vote: Int) {
        getProblem(problem_id) { prob ->
            if(prob != null) {
                val up = if (vote > 0) vote else 0
                val dw = if (vote < 0) vote else 0
                setProblem(PV(problem = prob.problem, upvotes = prob.upvotes+up, downvotes = prob.downvotes+dw, id=prob.id))
            }
        }
    }

    override fun getProblems(callback: (ArrayList<PV>) -> Unit) {
        FS.collection("problems").get()
            .addOnSuccessListener { documents ->
                var problems: ArrayList<PV> = arrayListOf()
                for (dc in documents) {
                    Log.d("load Problem", "${dc.id}")
                    problems.add(dc.toObject(PV::class.java))
                }
                callback(problems)
            }.addOnFailureListener {
                callback(ArrayList())
            }
    }

    override fun getQuestions(callback: (ArrayList<QA>) -> Unit) {
        FS.collection("questions").get()
            .addOnSuccessListener { documents ->
                var questions: ArrayList<QA> = arrayListOf()
                for (dc in documents) {
                    Log.d("load Question", "${dc.id}")
                    questions.add(dc.toObject(QA::class.java))
                }
                callback(questions)
            }.addOnFailureListener {
                callback(ArrayList())
            }
    }

    override fun setUserProblems(user_id: String, voted: Voted) {
        FS.collection("user_problems").document(user_id)
            .set(voted)
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
    }

    override fun getUserProblems(user_id: String, callback: (Voted) -> Unit) {
        FS.collection("user_problems").document(user_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(Voted::class.java)!!)
                else callback(Voted())
            }.addOnFailureListener {
                callback(Voted())
            }
    }


}