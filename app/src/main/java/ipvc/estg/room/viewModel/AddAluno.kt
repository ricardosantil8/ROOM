package ipvc.estg.room.viewModel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.room.R

class AddAluno : AppCompatActivity() {

    private lateinit var studentText: EditText
    private lateinit var escolaText: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        studentText = findViewById(R.id.student)
        escolaText = findViewById(R.id.escola)

        //Botão de criar aluno
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(studentText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_STUDENT, studentText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_ESCOLA, escolaText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_STUDENT = "com.example.android.aluno"
        const val EXTRA_REPLY_ESCOLA = "com.example.android.escola"
    }
}


