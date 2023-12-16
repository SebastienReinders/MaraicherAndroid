package com.example.maraicher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;


import androidx.appcompat.app.AppCompatActivity;

import java.net.Socket;
import java.util.Vector;

public class PanierActivity extends AppCompatActivity {
    private TextView tot;
    private TableLayout tableLayout;

    private TextView pan;

    private TextView artTab;

    private TextView prxTab;

    private TextView qteTab;


    private TextView tot2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        pan = findViewById(R.id.pan);
        artTab = findViewById(R.id.artTab);
        prxTab = findViewById(R.id.prxTab);
        qteTab = findViewById(R.id.qtetab);
        tot2 = findViewById(R.id.tot2);
        Button supp = findViewById(R.id.supprimerArticle);
        Button vider = findViewById(R.id.viderPanier);
        Button confirmer = findViewById(R.id.confirmerAchat);
        Button retour = findViewById(R.id.retour);


        //si langue choisie = francais

        if(Singleton.getInstance().getLangue() == false)
        {
            pan.setText("panier");
            artTab.setText("Articles");
            prxTab.setText("Prix à l'unité");
            qteTab.setText("Quantité");
            tot2.setText("total à payer : ");
            supp.setText("supprimer article");
            vider.setText("vider panier");
            confirmer.setText("confirmer achat");
            retour.setText("retour");

        }
        //si langue choisie = anglais

        else
        {
            pan.setText("basket");
            artTab.setText("Items");
            prxTab.setText("Unit price");
            qteTab.setText("Quantities");
            tot2.setText("total to pay : ");
            supp.setText("delete item");
            vider.setText("clear basket");
            confirmer.setText("confirm purchase");
            retour.setText("back");
        }

        tot = findViewById(R.id.totalPan);
        tableLayout = findViewById(R.id.tableLayoutPanier);

        remplirTableau();


        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ThSupprimerArticle().execute();
            }
        });



        Button logoutBoutton = findViewById(R.id.logoutPanier);
        logoutBoutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ThLogout().execute();
            }
        });





        vider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ThViderPanier().execute();
            }
        });





        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ThConfirmerAchat().execute();
            }
        });




        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PanierActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void remplirTableau() {
        Vector<Articles> articles = Singleton.getInstance().getPanier();

        // Supprimer toutes les lignes sauf la première (les titres des colonnes)
        if (tableLayout.getChildCount() > 1) {
           tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        }

        for (Articles article : articles) {
            if (article.getPrixArticle() != 0 || article.getQuDemande() != 0)
                ajouterLigneTableau(article, articles.indexOf(article));
        }

        // Mettre à jour le total
        Singleton.getInstance().calculTotal();
        float totalPanier = Singleton.getInstance().getTotal();
        String totalApayer = String.valueOf(totalPanier);
        tot.setText(totalApayer);
    }

    private void ajouterLigneTableau(Articles article, int rowIndex) {
        TableRow row = new TableRow(this);

        TextView textViewArticle = new TextView(this);
        textViewArticle.setText(article.getNomArticle());
        textViewArticle.setGravity(Gravity.CENTER);
        row.addView(textViewArticle);

        TextView textViewPrix = new TextView(this);
        textViewPrix.setText(article.getPrixEnString());
        textViewPrix.setGravity(Gravity.CENTER);
        row.addView(textViewPrix);

        TextView textViewQuantite = new TextView(this);
        textViewQuantite.setText(String.valueOf(article.getQuDemande()));
        textViewQuantite.setGravity(Gravity.CENTER);
        row.addView(textViewQuantite);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTableRowClick(view, rowIndex);
            }
        });

        tableLayout.addView(row);
    }

    public void onTableRowClick(View view, int rowIndex) {
        Singleton.getInstance().setNumLigneTableau(rowIndex);
        TableRow row = (TableRow) view;
        TextView textViewArticle = (TextView) row.getChildAt(0);
        String article = textViewArticle.getText().toString();
        Toast.makeText(this, article + ", Index: " + rowIndex, Toast.LENGTH_SHORT).show();
    }

    private class ThSupprimerArticle extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            Socket socket = Singleton.getInstance().getSocket();
            String requete = "SUPPRESSION#" + Singleton.getInstance().getNumLigneTableau();
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());
            Singleton.getInstance().setNumLigneTableau(-1);
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(socket, reponseBytes);
            reponse = new String(reponseBytes, 0, bytesRead);
            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {
            String resultatOVESP = OVESP.OVESP(reponse);

            // Mettre à jour la vue après suppression
            remplirTableau();

        }
    }





    private class ThViderPanier extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            Socket socket = Singleton.getInstance().getSocket();
            String requete = "VIDERPANIER";
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());
            Singleton.getInstance().setNumLigneTableau(-1);
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(socket, reponseBytes);
            reponse = new String(reponseBytes, 0, bytesRead);
            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {

            // Mettre à jour la vue après suppression
            Singleton.getInstance().getPanier().clear();
            remplirTableau();

        }
    }



    private class ThConfirmerAchat extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            Socket socket = Singleton.getInstance().getSocket();
            String requete = "VALIDEPANIER";
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());
            Singleton.getInstance().setNumLigneTableau(-1);
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(socket, reponseBytes);
            reponse = new String(reponseBytes, 0, bytesRead);
            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {

            // Mettre à jour la vue après suppression
            Singleton.getInstance().getPanier().clear();
            remplirTableau();

        }
    }




    private class ThLogout extends AsyncTask<Void, Void, String> {
        private String reponse;

        @Override
        protected String doInBackground(Void... voids) {
            Socket socket = Singleton.getInstance().getSocket();
            String requete = "LOGOUT#" + Singleton.getInstance().getNumLigneTableau();
            TCP tcpClient = new TCP(socket);
            tcpClient.send(requete.getBytes(), requete.length());
            Singleton.getInstance().setNumLigneTableau(-1);
            byte[] reponseBytes = new byte[TCP.TAILLE_MAX_DATA];
            int bytesRead = tcpClient.receive(socket, reponseBytes);
            reponse = new String(reponseBytes, 0, bytesRead);
            return reponse;
        }

        @Override
        protected void onPostExecute(String reponse) {
            Singleton.getInstance().getPanier().clear();
            //gerer deco retour vers loginpage
            // Ouvrir la nouvelle activité lors du clic sur le bouton "login"
            Intent intent = new Intent(PanierActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

}
