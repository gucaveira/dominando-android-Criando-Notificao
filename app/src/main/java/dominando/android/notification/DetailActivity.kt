package dominando.android.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dominando.android.notification.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtMessage.text = intent.getStringExtra(EXTRA_MESSAGE)
    }

    companion object {
        const val EXTRA_MESSAGE = "message"
    }
}