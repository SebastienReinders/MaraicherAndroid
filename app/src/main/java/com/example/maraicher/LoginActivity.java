package com.example.maraicher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    private String imageName = "";
    private ImageView imageView;
    private int resID;
    private String nomArticle = "";
    private TextView nomArticleTextView;
    private String prix = "";
    private TextView prixArticleTextView;
    private String qu = "";
    private TextView quArticleTextView;

    private String quDemandee = "";

    private EditText quDemandeeEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


         imageView = findViewById(R.id.imageViewArticle);

        // Obtenir le nom de l'image de l'objet articleCourant
        imageName = Singleton.getInstance().getArticleCourant().getImage();
        int dotIndex = imageName.lastIndexOf('.');

      //  String imageName;
        if (dotIndex != -1) {
            imageName = imageName.substring(0, dotIndex);
        } else {
            imageName = imageName; // Si aucun point n'est trouvé, gardez le nom tel quel
        }



        // Obtenir l'ID de ressource de l'image
        resID = getResources().getIdentifier(imageName, "drawable", getPackageName());

        // Définir l'image pour l'ImageView
        imageView.setImageResource(resID);

        nomArticle = Singleton.getInstance().getArticleCourant().getNomArticle();
        nomArticleTextView = findViewById(R.id.nomArticleTextView);
        nomArticleTextView.setText(nomArticle);

        prix = Singleton.getInstance().getArticleCourant().getPrixEnString();
        //prixArticleTextView = findViewById(R.id.prixArticleTextView);
        prixArticleTextView = findViewById(R.id.prixArticleTextView);
        prixArticleTextView.setText(prix);

        qu = Singleton.getInstance().getArticleCourant().getQuantiteEnString();
        quArticleTextView = findViewById(R.id.quArticleTextView);
        quArticleTextView.setText(qu);



        Button suivant = findViewById(R.id.Bouttonsuivant);
        Button precedant = findViewById(R.id.Bouttonprecedant);
        Button achat = findViewById(R.id.Bouttonacheter);

        Button panierBoutton = findViewById(R.id.panier);





        panierBoutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la nouvelle activité lors du clic sur le bouton "login"
                Intent intent = new Intent(LoginActivity.this, PanierActivity.class);
                startActivity(intent);

            }
        });



        suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lors d'un clic sur le bouton login, établir la connexion avec le serveur
                new ThArticleSuivantPrecedant().execute();
            }
        });


        precedant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lors d'un clic sur le bouton login, établir la connexion avec le serveur
            if (Singleton.getInstance().getNumArticleEncours() < 1)
                {
                    Toast.makeText(LoginActivity.this, "Erreur precedant", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Singleton.getInstance().setNumArticleEncours(Singleton.getInstance().getNumArticleEncours()-2);
                }
                new ThArticleSuivantPrecedant().execute();
            }
        });

        achat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                quDemandeeEditText = findViewById(R.id.quDemandeeEditText);

                quDemandee = quDemandeeEditText.getText().toString();

                try{
                    Singleton.getInstance().setQuDemande(Integer.parseInt(quDemandee));
                }catch (NumberFormatException e){
                    Singleton.getInstance().setQuDemande(0);
                }
                if (Singleton.getInstance().getQuDemande() > 0)
                {
                    new ThAchat().execute();
                }
                else
                {
                Toast.makeText(LoginActivity.this, "Met un chiffffffffre ", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    private class ThArticleSuivantPrecedant extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            // Récupérer la socket du singleton
            Socket socket = Singleton.getInstance().getSocket();


            int idArt = Singleton.getInstance().getNumArticleEncours();


            // Construire la requête
            String requete = "ARTSUIVANT#" + idArt;


            // Envoyer la requête au serveur
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());

            // Attendre la réponse du serveur
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(Singleton.getInstance().getSocket(), reponseBytes);

            // Convertir la réponse en une chaîne de caractères
            reponse = new String(reponseBytes, 0, bytesRead);


            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {
            String resultatOVESP = OVESP.OVESP(reponse);


            if ("vrai".equals(resultatOVESP)) {
                // Changer de vue vers LoginActivity
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                // Afficher un message d'erreur
                // , "Erreur OVESP", Toast.LENGTH_SHORT).show();
            }
        }
    }









    private class ThAchat extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            // Récupérer la socket du singleton
            Socket socket = Singleton.getInstance().getSocket();


            int idArt = Singleton.getInstance().getNumArticleEncours();



            String  requete = "ACHAT#" + quDemandee + "#" + String.valueOf(idArt);


            // Envoyer la requête au serveur
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());

            // Attendre la réponse du serveur
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(Singleton.getInstance().getSocket(), reponseBytes);

            // Convertir la réponse en une chaîne de caractères
            reponse = new String(reponseBytes, 0, bytesRead);


            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {
            String resultatOVESP = OVESP.OVESP(reponse);


            if ("vrai".equals(resultatOVESP)) {
                Toast.makeText(LoginActivity.this, "A J O U T E :) !", Toast.LENGTH_SHORT).show();
                // Changer de vue vers LoginActivity
                //Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                //startActivity(intent);
            } else {
                // Afficher un message d'erreur
                Toast.makeText(LoginActivity.this, "ajout panier NON effectué !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
