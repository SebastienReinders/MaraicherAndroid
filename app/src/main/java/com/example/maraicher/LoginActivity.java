package com.example.maraicher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    public int ArticleEncours = 0;
    public static Socket Masocket = null;
    public String Reponse = "";
    public String NomArticle ="";
    public int QuArticle = 0;
    public float PrixArticle = 0;
    public int QuDemande = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.panier);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la nouvelle activité lors du clic sur le bouton "login"
                Intent intent = new Intent(LoginActivity.this, PanierActivity.class);
                startActivity(intent);
            }
        });

        // Appel du thread pour changer l'article
        new ThArtticleChange().execute(ArticleEncours, MainActivity.getSockCli());
    }

    private class ThArtticleChange extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            int articleEnCours = (int) params[0];
            Socket socket = (Socket) params[1];

            // Construire la requête
            String requete = "ARTICLESUIVANT#" + articleEnCours;

            // Envoyer la requête au serveur
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());

            // Attendre la réponse du serveur
            byte[] reponse = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(reponse);

            // Convertir la réponse en une chaîne de caractères
            String reponseStr = new String(reponse, 0, bytesRead);

            // Mettre à jour la variable Reponse avec le résultat du serveur
            Reponse = reponseStr;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Afficher le Toast avec la réponse du serveur
            Toast.makeText(LoginActivity.this, Reponse, Toast.LENGTH_SHORT).show();
        }
    }

}
