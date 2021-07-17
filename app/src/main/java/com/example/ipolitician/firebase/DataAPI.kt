package com.example.ipolitician.firebase

import android.util.Log
import com.example.ipolitician.structures.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.KeyPair
import java.security.Timestamp
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
                        selected = (IntArray(30) {-1}).toList() as ArrayList<Int>
                    )
                )
                Log.d("listener", "USER SET success")
            }
            .addOnFailureListener { Log.d("listener", "USER SET fail") }
    }

    @Synchronized
    override fun updateUser(user_id: String, user: User) {
        FS.collection("users").document(user_id)
            .update("age", user.age, "gender", user.gender)
            .addOnSuccessListener { Log.d("listener", "updateUser success") }
            .addOnFailureListener { Log.d("listener", "updateUser fail") }
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
    override fun setProblem(user_id: String, problem: PV) {
        setUserTimestamp(user_id)
        FS.collection("problems").document(problem.id)
            .set(problem)
            .addOnSuccessListener { Log.d("listener", "SUBMISSION SET success") }
            .addOnFailureListener { Log.d("listener", "SUBMISSION SET fail") }
    }

    @Synchronized
    override fun voteProblem(problem_id: String, upvote: Long, downvote: Long) {
        FS.collection("problems").document(problem_id)
            .update("upvotes", FieldValue.increment(upvote), "downvotes", FieldValue.increment(downvote))
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
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
        FS.collection("problems").get()
            .addOnSuccessListener { documents ->
                val nextId = if(documents.size() > 9) documents.size().toString() else "0" + documents.size().toString()
                callback("problem$nextId")
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
    override fun setQuestion(id: String, question: QA) {
        FS.collection("questions").document(id)
            .set(question)
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
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
                val elections: ArrayList<EV> = ArrayList(documents.map{it.toObject(EV::class.java)})
                elections.sortBy{it.id}
                callback(elections)
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

    @Synchronized
    override fun getElectionVotes(callback: (Vote) -> Unit) {
        val votes = Vote()
        FS.collection("votes").get()
            .addOnSuccessListener { documents ->
                for(dc in documents) {
                    votes.votes[dc.id] = (dc["votes"] as Long).toInt()
                }
                callback(votes)
                Log.d("listener", "getElectionVotes success")
            }.addOnFailureListener {
                callback(votes)
                Log.d("listener", "getElectionVotes fail")
            }
    }

    @Synchronized
    override fun voteElection(id: String) {
        FS.collection("votes").document(id)
            .update("votes", FieldValue.increment(1))
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
    }

    @Synchronized
    override fun unvoteElection(id: String) {
        FS.collection("votes").document(id)
            .update("votes", FieldValue.increment(-1))
            .addOnSuccessListener { Log.d("listener", "setUserProblems success") }
            .addOnFailureListener { Log.d("listener", "setUserProblems fail") }
    }

    @Synchronized
    override fun setUserTimestamp(user_id: String) {
        FS.collection("user_timestamps").document(user_id)
            .set(TM())
            .addOnSuccessListener { Log.d("listener", "setUserTimestamp success") }
            .addOnFailureListener { Log.d("listener", "setUserTimestamp fail") }
    }

    @Synchronized
    override fun getUserTimestamp(user_id: String, callback: (TM?) -> Unit) {
        FS.collection("user_timestamps").document(user_id).get()
            .addOnSuccessListener { document ->
                if(document.exists()) callback(document.toObject(TM::class.java))
                else callback(null)
                Log.d("listener", "getUserTimestamp success") }
            .addOnFailureListener { Log.d("listener", "getUserTimestamp fail") }
    }
}
