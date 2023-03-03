package dominando.android.notification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dominando.android.notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSimple.setOnClickListener { NotificationUtils.notificationSimple(this) }
        binding.btnTapAction.setOnClickListener { NotificationUtils.notificationWithTapAction(this) }
        binding.btnBigText.setOnClickListener { NotificationUtils.notificationBigText(this) }
        binding.btnActionButton.setOnClickListener { NotificationUtils.notificationWithButtonAction(this) }
        binding.btnDirectReply.setOnClickListener { NotificationUtils.notificationAutoReply(this) }
        binding.btnInbox.setOnClickListener { NotificationUtils.notificationInbox(this) }
        binding.btnHeadsUp.setOnClickListener { NotificationUtils.notificationHeadsUp(this) }
    }
}