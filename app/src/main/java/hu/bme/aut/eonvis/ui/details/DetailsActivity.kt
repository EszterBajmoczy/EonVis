package hu.bme.aut.eonvis.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.eonvis.R
import javax.inject.Inject


class DetailsActivity : AppCompatActivity() {
    @Inject
    lateinit var detailsViewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val intent = intent
        val id = intent.getLongExtra("id", 0)
    }
}