package com.example.maraicher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.EditText;
import android.util.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    public boolean estConnecte = false;

    private EditText nomEditText;
    private EditText motDePasseEditText;
    private CheckBox nouveauClientCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("MainActivity", "onCreate executed");

        nomEditText = findViewById(R.id.nomEditText);
        motDePasseEditText = findViewById(R.id.motDePasseEditText);
        nouveauClientCheckbox = findViewById(R.id.nouveauClientCheckBox);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lors d'un clic sur le bouton login, établir la connexion avec le serveur
                new EtablissementConnection().execute();
            }
        });
    }

    private class EtablissementConnection extends AsyncTask<Void, Void, String> {
        public String reponse;
        private String user;
        private String mdp;
        private boolean EstNouveau;

        private static final String SERVEUR_IP = "192.168.100.15";
        private static final int SERVEUR_PORT = 50406;

        @Override
        protected String doInBackground(Void... voids) {
            user = nomEditText.getText().toString();
            mdp = motDePasseEditText.getText().toString();
            EstNouveau = nouveauClientCheckbox.isChecked();



            Log.d("EtablissementConnection", "Envoi de la requête au serveur");

            Socket socket = null;
            try {
                // Établir la connexion avec le serveur
                socket = new Socket(SERVEUR_IP, SERVEUR_PORT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Ajouter l'instance de Socket au singleton
            Singleton.getInstance().setSocket(socket);
            // Construire la requête
            String requete = "LOGIN#" + user + "#" + mdp + "#" + (EstNouveau ? "1" : "0");

            // Envoyer la requête au serveur
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());


            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int bytesRead;
            byte[] data = new byte[1500];




            // Attendre la réponse du serveur
            byte[] reponseBytes = new byte[1500];
            bytesRead = tcpClient.receive(Singleton.getInstance().getSocket(), reponseBytes);

            // Convertir la réponse en une chaîne de caractères
            reponse = new String(reponseBytes, 0, bytesRead);
            //reponse = new String(reponseBytes, 0, bytesRead, "UTF-8");


            Log.d("EtablissementConnection", "Réponse reçue du serveur : " + reponse);

            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {

            String res = OVESP.OVESP(reponse);


            Log.d("EtablissementConnection", "Valeur de res : " + res);


            if (Integer.parseInt(res) != 0) {
                // Connexion réussie, vous pouvez rediriger vers une autre activité, afficher un message, etc.

                // Lancer le thread ThPremierArticle pour obtenir la réponse du serveur
               runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new ThPremierArticle().execute();
                    }
                });
            } else {
                // Afficher un message d'erreur
                Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ThPremierArticle extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            // Récupérer la socket du singleton
            Socket socket = Singleton.getInstance().getSocket();

            // Construire la requête
            String requete = "ARTSUIVANT#0";

            Log.d("ThPremierArticle", "Envoi de la requête au serveur");

            // Envoyer la requête au serveur
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());

            // Attendre la réponse du serveur
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(Singleton.getInstance().getSocket(), reponseBytes);

            // Convertir la réponse en une chaîne de caractères
            reponse = new String(reponseBytes, 0, bytesRead);

            Log.d("ThPremierArticle", "Réponse reçue du serveur : " + reponse);

            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {
            String resultatOVESP = OVESP.OVESP(reponse);


            if ("vrai".equals(resultatOVESP)) {
                // Changer de vue vers LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                // Afficher un message d'erreur
                Toast.makeText(MainActivity.this, "Erreur OVESP", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


