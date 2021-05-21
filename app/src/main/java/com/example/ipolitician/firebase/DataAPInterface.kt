package com.example.ipolitician.firebase

import com.example.ipolitician.structures.*

interface DataAPInterface {

    fun getUsers(callback: (List<User>) -> Unit)
    fun getUser(user_id: String, callback: (User?) -> Unit)
    fun setUser(user_id: String, user: User)

    fun getQuestions(callback: (ArrayList<QA>) -> Unit)

    fun getSubmissions(user_id: String, callback: (Selected) -> Unit)
    fun setSubmissions(user_id: String, selected: Selected)

    fun getParties(callback: (ArrayList<Party>) -> Unit)

    fun getProblems(callback: (ArrayList<PV>) -> Unit)
    fun getProblem(problem_id: String, callback: (PV?) -> Unit)
    fun setProblem(problem: PV)
    fun voteProblem(problem_id: String, vote: Int)

    fun setUserProblems(user_id: String, voted: Voted)
    fun getUserProblems(user_id: String, callback: (Voted) -> Unit)

}