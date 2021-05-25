package com.example.ipolitician.firebase

import android.util.Log
import com.example.ipolitician.structures.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class DataAPI : DataAPInterface {

    private val FS = Firebase.firestore
    companion object {
        val instance = DataAPI()
    }

    @Synchronized
    override fun getUsers(callback: (List<User>, List<String>) -> Unit) {
        FS.collection("users").get()
            .addOnSuccessListener { documents ->
                var ids = ArrayList<String>()
                var users = ArrayList<User>()
                for (dc in documents) {
                    Log.d("load USERS", dc.toString())
                    ids.add(dc.id)
                    users.add(dc.toObject(User::class.java))
                }
                callback(users, ids)
            }.addOnFailureListener {
                callback(ArrayList(), ArrayList())
            }
    }

    @Synchronized
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

    @Synchronized
    override fun setUser(user_id: String, user: User) {
        FS.collection("users").document(user_id)
            .set(user)
            .addOnSuccessListener {
                setSubmission(
                    user_id, Selected(
                        selected = arrayListOf(
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1,
                            -1
                        )
                    )
                )
                setUserProblems(user_id, Voted(voted = mutableMapOf()))
                Log.d("listener", "USER SET success")
            }
            .addOnFailureListener { Log.d("listener", "USER SET fail") }
    }

    @Synchronized
    override fun getSubmission(user_id: String, callback: (Selected) -> Unit) {
        FS.collection("submissions").document(user_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(Selected::class.java)!!)
                else callback(Selected())
            }.addOnFailureListener {
                callback(Selected())
            }
    }

    @Synchronized
    override fun setSubmission(user_id: String, selected: Selected) {
        FS.collection("submissions").document(user_id)
            .set(selected)
            .addOnSuccessListener { Log.d("listener", "SUBMISSION SET success") }
            .addOnFailureListener { Log.d("listener", "SUBMISSION SET fail") }
    }

    @Synchronized
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

    @Synchronized
    override fun getProblem(problem_id: String, callback: (PV?) -> Unit) {
        FS.collection("problems").document(problem_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(PV::class.java)!!)
                else callback(null)
            }.addOnFailureListener {
                callback(null)
            }
    }

    @Synchronized
    override fun setProblem(problem: PV) {
        FS.collection("problems").document(problem.id)
            .set(problem)
            .addOnSuccessListener { Log.d("listener", "SUBMISSION SET success") }
            .addOnFailureListener { Log.d("listener", "SUBMISSION SET fail") }
    }

    @Synchronized
    override fun voteProblem(problem_id: String, upvote: Int, downvote: Int) {
        getProblem(problem_id) { prob ->
            if(prob != null) {
                setProblem(
                    PV(
                        problem = prob.problem,
                        upvotes = prob.upvotes + upvote,
                        downvotes = prob.downvotes + downvote,
                        id = prob.id
                    )
                )
            }
        }
    }

    @Synchronized
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

    @Synchronized
    override fun getProblemID(callback: (String) -> Unit) {
        Log.d("IDDDDDDDDDDDDDDDDDDDDDD", "")
        FS.collection("problems").get()
            .addOnSuccessListener { documents ->
                var id: String = "problem00"
                for (dc in documents) {
                    Log.d("load Problem", "${dc.id}")
                    id = (dc.toObject(PV::class.java)).id
                }
                var next_id = id.substring(7).toInt() + 1
                id = "problem" + if(next_id > 9) next_id.toString() else "0" + next_id.toString()
                callback(id)
            }.addOnFailureListener {
                callback("problem00000")
            }
    }

    @Synchronized
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

    @Synchronized
    override fun setUserProblems(user_id: String, voted: Voted) {
        FS.collection("user_problems").document(user_id)
            .set(voted)
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
    }

    @Synchronized
    override fun getUserProblems(user_id: String, callback: (Voted) -> Unit) {
        FS.collection("user_problems").document(user_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(Voted::class.java)!!)
                else callback(Voted())
            }.addOnFailureListener {
                callback(Voted())
            }
    }

    @Synchronized
    override fun setVocabulary(vocab: VocabData) {
        FS.collection("vocabulary").document()
            .set(vocab)
            .addOnSuccessListener { Log.d("listener", "setVocabulary success") }
            .addOnFailureListener { Log.d("listener", "setVocabulary fail") }
    }

    @Synchronized
    override fun getVocabulary(callback: (ArrayList<VocabData>) -> Unit) {
        FS.collection("vocabulary").get()
            .addOnSuccessListener { documents ->
                Log.d("listener", "getVocabulary success")
                callback(ArrayList(documents.map { it.toObject(VocabData::class.java) }.sortedBy { it.header }))
            }
            .addOnFailureListener { Log.d("listener", "getVocabulary fail") }
    }

    @Synchronized
    override fun getElections(callback: (ArrayList<EV>) -> Unit) {
        FS.collection("elections").get()
            .addOnSuccessListener { documents ->
                callback(ArrayList(documents.map{it.toObject(EV::class.java)}))
            }.addOnFailureListener {
                callback(ArrayList())
            }
    }

    @Synchronized
    override fun setElections(election: EV) {
        FS.collection("elections").document()
            .set(election)
            .addOnSuccessListener { Log.d("listener", "setElections success") }
            .addOnFailureListener { Log.d("listener", "setElections fail") }
    }

    @Synchronized
    override fun setUserElections(user_id: String, voted: Voted) {
        FS.collection("user_elections").document(user_id)
            .set(voted)
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
    }

    @Synchronized
    override fun getUserElections(user_id: String, callback: (Voted) -> Unit) {
        FS.collection("user_elections").document(user_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(Voted::class.java)!!)
                else callback(Voted())
            }.addOnFailureListener {
                callback(Voted())
            }
    }
}
