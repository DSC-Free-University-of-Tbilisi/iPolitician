package com.example.ipolitician.firebase

import com.example.ipolitician.structures.*
import java.sql.Timestamp

interface DataAPInterface {

    fun getUsers(callback: (List<User>, List<String>) -> Unit)
    fun getUser(user_id: String, callback: (User?) -> Unit)
    fun setUser(user_id: String, user: User)
    fun updateUser(user_id: String, user: User)

    fun getQuestions(callback: (ArrayList<QA>) -> Unit)
    fun setQuestion(id: String, question: QA)

    fun getSubmission(user_id: String, callback: (Selected) -> Unit)
    fun setSubmission(user_id: String, selected: Selected)

    fun getParties(callback: (ArrayList<Party>) -> Unit)

    fun getProblems(callback: (ArrayList<PV>) -> Unit)
    fun getProblem(problem_id: String, callback: (PV?) -> Unit)
    fun voteProblem(problem_id: String, upvote: Long, downvote: Long)
    fun setProblem(user_id: String, problem: PV)

    fun getProblemID(callback: (String) -> Unit)

    fun setUserProblems(user_id: String, voted: Voted)
    fun getUserProblems(user_id: String, callback: (Voted) -> Unit)

    fun setVocabulary(vocab: VocabData)
    fun getVocabulary(callback: (ArrayList<VocabData>) -> Unit)

    fun getElections(callback: (ArrayList<EV>) -> Unit)
    fun setElections(elections: EV)

    fun voteElection(id: String)
    fun unvoteElection(id: String)
    fun getElectionVotes(callback: (Vote) -> Unit)

    fun setUserElections(user_id: String, voted: Voted)
    fun getUserElections(user_id: String, callback: (Voted) -> Unit)

    fun setUserTimestamp(user_id: String)
    fun getUserTimestamp(user_id: String, callback: (TM?) -> Unit)

}