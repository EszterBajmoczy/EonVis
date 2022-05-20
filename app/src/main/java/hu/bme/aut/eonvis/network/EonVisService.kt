package hu.bme.aut.eonvis.network

import android.net.ConnectivityManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.eonvis.data.model.PowerConsume


class EonVisService constructor(private val mAuth: FirebaseAuth, private var db: FirebaseFirestore)  {
    private var userId: String? = null

    init {
        userId = mAuth.currentUser?.uid
    }

    fun login(username: String, password: String, loginCallback: (Boolean) -> Unit) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener { task ->
            loginCallback(task.isSuccessful)
            userId = mAuth.currentUser?.uid
        }
    }

    fun isLoggedIn(callback: (Boolean) -> Unit) {
        callback(mAuth.currentUser != null)
    }

    fun getPowerConsumes(callback: (PowerConsume) -> Unit) {
        db.collection(userId!!).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (firebaseData in task.result!!.documents) {
                    try {
                        firebaseData.reference.collection("tags")
                            .document("tags")
                            .get()
                            .addOnCompleteListener { tagData ->
                                val data = PowerConsume(
                                    id = firebaseData.getLong("id")!!,
                                    incoming = firebaseData.getDouble("incoming")!!,
                                    outgoing = firebaseData.getDouble("outgoing")!!)
                                val tagList = ArrayList<String>()
                                tagData.result.data?.values?.forEach { tag -> tagList.add(tag.toString()) }
                                data.addTags(tagList)

                                callback(data)
                            }
                    } catch (e: Exception){
                        Log.d("Firebase", "Invalid data format.")
                    }
                }
            } else {
                Log.d("Firebase", "No data for user")
            }
        }
    }

    fun addTag(id: Long, tag: String, callback: (Boolean) -> Unit) {
        val data: MutableMap<String, Any> = HashMap()
        data[tag] = tag

        db.collection(userId!!).document(id.toString()).collection("tags").document("tags")
            .update(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun removeTag(id: Long, tag: String, callback: (Boolean) -> Unit) {
        val deleteTag: MutableMap<String, Any> = HashMap()
        deleteTag[tag] = FieldValue.delete()

        db.collection(userId!!).document(id.toString()).collection("tags").document("tags").update(deleteTag).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true)
            } else {
                callback(false)
            }
        }
    }

    fun getTags(callback: (List<String>) -> Unit) {
        db.collection(userId!!)
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    for (dataDoc in task.result!!.documents) {
                        dataDoc.reference.collection("tags")
                            .document("tags")
                            .get()
                            .addOnCompleteListener { data ->
                                val list = ArrayList<String>()
                                data.result.data?.values?.forEach { tag -> list.add(tag.toString()) }
                                callback(list)
                        }
                    }
                } else {
                    Log.d("Firebase", "Could not get documents.")
                }
            }
    }
}