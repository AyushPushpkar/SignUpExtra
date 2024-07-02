package com.example.signup

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.signup.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private val binding : ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnnotify1.setOnClickListener {
            val notification = NotificationCompat.Builder(this,App().CHANNEL_ID1)

            intent = Intent(this,NavDrawerActivity::class.java)
//            intent = Intent(this,BroadCast::class.java)
//            intent = Intent(this,MyIntentService::class.java)

            intent.putExtra("DATA_REC",binding.contentnot.text.toString())
            val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            val pendingIntent = PendingIntent.getBroadcast(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
//            val pendingIntent = PendingIntent.getService(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)

            notification.setContentTitle(binding.titlenot.text.toString())
            notification.setContentText(binding.contentnot.text.toString())
            notification.setSmallIcon(R.drawable.baseline_comment_24)
                notification.setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.CYAN)
                    .setContentIntent(pendingIntent)
                    .addAction(R.drawable.baseline_comment_24,"Back",pendingIntent)
                    .addAction(R.drawable.baseline_comment_24,"Play",null)
                    .addAction(R.drawable.baseline_comment_24,"Next",null)
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)
                .build()

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1,notification.build())

        }

        binding.btnnotify2.setOnClickListener {
            val notification = NotificationCompat.Builder(this,App().CHANNEL_ID2)

            notification.setContentTitle(binding.titlenot.text.toString())
            notification.setContentText(binding.contentnot.text.toString())
            notification.setSmallIcon(R.drawable.baseline_comment_24)
            notification.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.CYAN)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .build()

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(2,notification.build())

        }

    }

    private fun enableEdgeToEdge(){

    }
}