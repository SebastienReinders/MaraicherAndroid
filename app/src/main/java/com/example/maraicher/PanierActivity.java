// MainActivity.java
package com.example.maraicher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatActivity;

public class PanierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);
    }

    public void onTableRowClick(View view) {
        // Récupérez les données de la ligne cliquée ici
        TextView textViewArticle = view.findViewById(R.id.textViewArticle);
        String article = textViewArticle.getText().toString();

        // Faites quelque chose avec les données récupérées
        Toast.makeText(this, "Ligne cliquée : " + article, Toast.LENGTH_SHORT).show();
    }

}
