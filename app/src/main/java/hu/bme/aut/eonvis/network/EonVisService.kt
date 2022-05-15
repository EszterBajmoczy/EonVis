package hu.bme.aut.eonvis.network

import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


class EonVisService @Inject constructor()  {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login(username: String, password: String, loginCallback: (Boolean) -> Unit) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener { task ->
            loginCallback(task.isSuccessful)
        }
    }

    fun isLoggedIn(callback: (Boolean) -> Unit) {
        callback(mAuth.currentUser != null)
    }
}