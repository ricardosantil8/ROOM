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

    private lateinit var studentEdit: EditText
    private lateinit var escolaEdit: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        studentEdit = findViewById(R.id.student)
        escolaEdit = findViewById(R.id.escola)

        val intents = intent.extras
        var ids = -10

        if(intent != null)
        {
            var aluno = intent.getStringExtra("Aluno")
            var escola = intent.getStringExtra("Escola")
            ids = intent.getIntExtra("id",-10)

            studentEdit.setText(aluno)
            escolaEdit.setText(escola)
        }


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(studentEdit.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {

                if(ids != -10){ replyIntent.putExtra(EXTRA_REPLY_ID, ids)}

                replyIntent.putExtra(EXTRA_REPLY_STUDENT, studentEdit.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_ESCOLA, escolaEdit.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_STUDENT = "com.example.android.aluno"
        const val EXTRA_REPLY_ESCOLA = "com.example.android.escola"
        const val EXTRA_REPLY_ID = "com.example.android.id"
    }
}


