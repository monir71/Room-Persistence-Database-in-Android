package com.example.roompersitencelibrary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText title, amount;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.title);
        amount = findViewById(R.id.amount);
        submitBtn = findViewById(R.id.submitBtn);

        DatabaseHelper databaseHelper = DatabaseHelper.getDB(this);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String amountText = amount.getText().toString();

                databaseHelper.expenseDao().addTx(new Expense(titleText, amountText));

                title.setText("");
                amount.setText("");

                ArrayList<Expense> data = (ArrayList<Expense>) databaseHelper.expenseDao().getAllExpenses();
                for(int i = 0; i < data.size(); i++)
                {
                    Log.d("Data : ", "Id: " + data.get(i).getId() + " Title: " +
                            data.get(i).getTitle() + " Amount: " + data.get(i).getAmount());
                }
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}