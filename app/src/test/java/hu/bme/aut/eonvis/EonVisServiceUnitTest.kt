package hu.bme.aut.eonvis

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import hu.bme.aut.eonvis.network.EonVisService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class EonVisServiceUnitTest {
    private val _sut: EonVisService

    private val _firebaseAuth: FirebaseAuth = mockk<FirebaseAuth>()
    private val _firestore: FirebaseFirestore = mockk<FirebaseFirestore>()

    private val userId = "userId"

    init {
        val user = mockk<FirebaseUser>()

        every { _firebaseAuth.currentUser } returns user
        every { user.uid } returns userId

        _sut = EonVisService(_firebaseAuth, _firestore)
    }

    @Test
    fun login() {
        val username = "username"
        val password = "password"

        val authResult = mockk<Task<AuthResult>>(relaxed = true)

        every { _firebaseAuth.signInWithEmailAndPassword(username, password) } returns authResult

        _sut.login(username = username, password = password) { }

        verify {
            _firebaseAuth.signInWithEmailAndPassword(username, password)
        }
    }

    @Test
    fun getPowerConsumes_calls_users_data() {
        val collectionReference = mockk<CollectionReference>()
        val querySnapshot = mockk<Task<QuerySnapshot>>(relaxed = true)

        every { _firestore.collection(userId) } returns collectionReference
        every { collectionReference.get() } returns querySnapshot

        _sut.getPowerConsumes { }

        verify {
            _firestore.collection(userId)
            collectionReference.get()
        }
    }

    @Test
    fun addTag() {
        val id:Long = 1
        val tag = "Tag"
        val data: MutableMap<String, Any> = HashMap()
        data[tag] = tag

        val collectionReference = mockk<CollectionReference>()
        val tagCollectionReference = mockk<CollectionReference>()
        val documentReference = mockk<DocumentReference>(relaxed = true)

        every { _firestore.collection(userId) } returns collectionReference
        every { collectionReference.document(id.toString()) } returns documentReference
        every { documentReference.collection("tags") } returns tagCollectionReference
        every { tagCollectionReference.document("tags") } returns documentReference
        every { documentReference.update(data) } returns mockk<Task<Void>>()

        _sut.addTag(id, tag)

        verify {
            _firestore.collection(userId)
            collectionReference.document(id.toString())
            documentReference.collection("tags")
            tagCollectionReference.document("tags")
            documentReference.update(data)
        }
    }

    @Test
    fun removeTag() {
        val id:Long = 1
        val tag = "Tag"
        val deleteTag: MutableMap<String, Any> = HashMap()
        deleteTag[tag] = FieldValue.delete()

        val collectionReference = mockk<CollectionReference>()
        val tagCollectionReference = mockk<CollectionReference>()
        val documentReference = mockk<DocumentReference>(relaxed = true)

        every { _firestore.collection(userId) } returns collectionReference
        every { collectionReference.document(id.toString()) } returns documentReference
        every { documentReference.collection("tags") } returns tagCollectionReference
        every { tagCollectionReference.document("tags") } returns documentReference
        every { documentReference.update(deleteTag) } returns mockk<Task<Void>>()

        _sut.removeTag(id, tag)

        verify {
            _firestore.collection(userId)
            collectionReference.document(id.toString())
            documentReference.collection("tags")
            tagCollectionReference.document("tags")
            documentReference.update(deleteTag)
        }
    }

    @Test
    fun getTags() {
        val collectionReference = mockk<CollectionReference>()
        val querySnapshot = mockk<Task<QuerySnapshot>>(relaxed = true)

        every { _firestore.collection(userId) } returns collectionReference
        every { collectionReference.get() } returns querySnapshot

        _sut.getTags { }

        verify {
            _firestore.collection(userId)
            collectionReference.get()
        }
    }
}