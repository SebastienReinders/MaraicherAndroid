package com.example.maraicher;

import java.net.Socket;
import java.util.Vector;

public class Singleton {
    private static Singleton instance;
    private int NumArticleEncours = 0;
    private Socket socket;
    private Articles articleCourant;
    private Vector<Articles> panier;
    private float total;
    private int QuDemande = 0;

    private int numLigneTableau = 1;

    //0 = fr; 1 = en

    public boolean langue = false;

    private Singleton() {
        // Constructeur privé pour éviter l'instanciation directe.
        panier = new Vector<>();
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Articles getArticleCourant() {
        return articleCourant;
    }

    public void setArticleCourant(Articles articleCourant) {
        this.articleCourant = articleCourant;
    }
    public void setQuDemande(int a){
        QuDemande = a;
    }
    public int getQuDemande(){
        return QuDemande;
    }

    public Vector<Articles> getPanier() {
        return panier;
    }

    public void setPanier(Vector<Articles> panier) {
        this.panier = panier;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void calculTotal() {
        total = 0;

        for (Articles article : panier) {
            // Cast du premier quDemande à float avant la multiplication
            float montantArticle = (float) article.getQuDemande() * article.getPrixArticle();
            total += montantArticle;
        }

    }


    public int getNumArticleEncours() {
        return NumArticleEncours;
    }

    public void setNumArticleEncours(int numArticleEncours) {
        NumArticleEncours = numArticleEncours;
    }



    public int getNumLigneTableau() {
        return numLigneTableau;
    }

    public void setNumLigneTableau(int numLigneTableau) {
        this.numLigneTableau = numLigneTableau;
    }

    public boolean getLangue()
    {
        return langue;
    }

    public void setLangue(boolean lg)
    {
        langue = lg;
    }

}
