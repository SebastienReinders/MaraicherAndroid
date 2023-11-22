package com.example.maraicher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    public boolean estConnecte = false;
    public static Socket sockCli = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lors d'un clic sur le bouton login, établir la connexion avec le serveur
                new EtablissementConnection().execute();

            }
        });
    }

    private class EtablissementConnection extends AsyncTask<Void, Void, Socket> {

        private static final String SERVEUR_IP = "192.168.100.8";
        private static final int SERVEUR_PORT = 50406;

        @Override
        protected Socket doInBackground(Void... voids) {
            Socket socket = null;
            try {
                // Établir la connexion avec le serveur
                socket = new Socket(SERVEUR_IP, SERVEUR_PORT);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return socket;
        }

        @Override
        protected void onPostExecute(Socket result) {
            // Mettre à jour la variable sockCli de la classe MainActivity avec le résultat du thread
            sockCli = result;

            // Planifier la disparition du Toast après 5 secondes
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).cancel();
                        }
                    },
                    5000
            );

            // Une fois la connexion établie, appeler le thread ThConnection
            new ThConnection().execute(sockCli);
        }
    }

    private class ThConnection extends AsyncTask<Socket, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Socket... sockets) {
            // Récupérer la socket passée en paramètre
            Socket socket = sockets[0];

            // Récupérer les valeurs des champs
            EditText nomEditText = findViewById(R.id.nomEditText);
            EditText motDePasseEditText = findViewById(R.id.motDePasseEditText);
            CheckBox nouveauClientCheckBox = findViewById(R.id.nouveauClientCheckBox);

            String nomUtilisateur = nomEditText.getText().toString();
            String motDePasse = motDePasseEditText.getText().toString();
            boolean caseCochee = nouveauClientCheckBox.isChecked();

            // Construire la requête
            String requete = "LOGIN#" + nomUtilisateur + "#" + motDePasse + "#" + (caseCochee ? "1" : "0");

            // Envoyer la requête au serveur
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());

            // Attendre la réponse du serveur
            byte[] reponse = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(reponse);

            // Convertir la réponse en une chaîne de caractères
            String reponseStr = new String(reponse, 0, bytesRead);

            // Appeler OVESP pour traiter la réponse
            String resultatOVESP = OVESP.OVESP(reponseStr);

            // Retourner vrai ou faux en fonction du résultat d'OVESP
            return "vrai".equals(resultatOVESP);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Mettre à jour la variable estConnecte de la classe MainActivity avec le résultat du thread
            estConnecte = result;

            // Afficher un Toast en fonction du résultat
            if (estConnecte) {
                // Si la connexion est réussie, basculer vers une autre activité
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                // Sinon, afficher un Toast d'erreur de connexion
                Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Socket getSockCli() {
        return sockCli;
    }

}