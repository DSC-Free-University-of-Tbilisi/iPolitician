package com.example.ipolitician.firebase

import com.example.ipolitician.structures.*

interface DataAPInterface {

    fun getUsers(callback: (List<User>, List<String>) -> Unit)
    fun getUser(user_id: String, callback: (User?) -> Unit)
    fun setUser(user_id: String, user: User)

    fun getQuestions(callback: (ArrayList<QA>) -> Unit)

    fun getSubmission(user_id: String, callback: (Selected) -> Unit)
    fun setSubmission(user_id: String, selected: Selected)

    fun getParties(callback: (ArrayList<Party>) -> Unit)

    fun getProblems(callback: (ArrayList<PV>) -> Unit)
    fun getProblem(problem_id: String, callback: (PV?) -> Unit)
    fun voteProblem(problem_id: String, upvote: Int, downvote: Int)
    fun setProblem(problem: PV)

    fun getProblemID(callback: (String) -> Unit)

    fun setUserProblems(user_id: String, voted: Voted)
    fun getUserProblems(user_id: String, callback: (Voted) -> Unit)

    fun setVocabulary(vocab: VocabData)
    fun getVocabulary(callback: (ArrayList<VocabData>) -> Unit)
}