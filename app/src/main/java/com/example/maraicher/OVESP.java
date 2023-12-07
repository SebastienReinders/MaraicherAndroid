package com.example.maraicher;

import android.util.Log;
import java.util.ArrayList;
import java.util.Vector;

public class OVESP {
    public static ArrayList<Articles> nouveauPanier = new ArrayList<>();

    public static String OVESP(String requete) {
        // Diviser la requête en parties en utilisant #
        String[] parties = requete.split("#");

        // Vérifier la première partie de la requête
        if (parties.length > 0) {
            String premierePartie = parties[0];

            // Comparer avec la chaîne spécifique "LOGIN2"
            if ("LOGIN2".equals(premierePartie)) {
                // Vérifier si la partie suivante existe et est égale à "0"
                if (parties.length > 1 && "0".equals(parties[1])) {
                    return "0";
                } else {
                    return parties[1];
                }
            } else if ("ARTSUIVANT2".equals(premierePartie)) {
                if (parties.length >= 6) {
                    try {
                        // Récupérer les valeurs de la requête
                        int numArticleEncours = Integer.parseInt(parties[1]);
                        String nomArticle = parties[2];
                        String imageArticle = parties[3];
                        imageArticle = imageArticle.toLowerCase();
                        int quantiteArticle = Integer.parseInt(parties[4]);
                        float prixArticle = Float.parseFloat(parties[5]) / 100.0f;  // Diviser par 100 pour obtenir le prix correct

                        // Mettre à jour l'article courant dans le singleton
                        Singleton singleton = Singleton.getInstance();
                        singleton.setNumArticleEncours(numArticleEncours);

                        Articles articleCourant = new Articles(nomArticle, quantiteArticle, prixArticle, 0, imageArticle);
                        singleton.setArticleCourant(articleCourant);

                        Log.d("OVESP", "Num Article Encours : " + singleton.getNumArticleEncours());
                        Log.d("OVESP", "Nom Article : " + singleton.getArticleCourant().getNomArticle());
                        Log.d("OVESP", "Image Article : " + singleton.getArticleCourant().getImage());
                        Log.d("OVESP", "Quantité Article : " + singleton.getArticleCourant().getQuantiteArticle());
                        Log.d("OVESP", "Prix Article : " + singleton.getArticleCourant().getPrixArticle());


                        return "vrai";

                    } catch (NumberFormatException e) {

                        return "faux";
                    }

                } else {
                    return "0";
                }
            } else if ("ACHAT2".equals(premierePartie)) {

                if (requete.equals("ACHAT2#KO")) //Pas bon m'fi
                {
                    return "hahaha mais encore c'est ca oui ou non ? Je m'en fichie cette chaine n'est pas vérifée ";
                }
                else // La tu tiens le bon bout
                {

                    Articles article = new Articles(Singleton.getInstance().getArticleCourant().getNomArticle(),Singleton.getInstance().getQuDemande(),Singleton.getInstance().getArticleCourant().getPrixArticle(),Singleton.getInstance().getQuDemande(),"");

                    Singleton.getInstance().getPanier().add(article);

                    Singleton.getInstance().calculTotal();

                    return "vrai";
                }



               // return "BLABLA";
            }
            else if ("SUPPOK".equals(premierePartie)) {
               //     Vector<Articles> nouveauPanier = new Vector<>();
                   // Singleton.getInstance().setPanier(nouveauPanier);
                Singleton.getInstance().getPanier().clear();
                    //ici je recuperer ce qui suit donc le nbre d articles puis les articles (intitule, prix, quantite,
                    parties = requete.split("#");
                int qu = 0;
                float prix = 0;;

                for (int i = 2; i < parties.length; i += 3) {

                    String nom = parties[i];
                    String stringPrix = parties[i + 1];
                    String stringQuantite = parties[i + 2];
                    try {
                        qu = Integer.parseInt(stringQuantite);
                    } catch (NumberFormatException e) {
                        // Gérer l'exception si la chaîne ne peut pas être convertie en float

                    }


                    try {
                         prix = Float.parseFloat(stringPrix)/100.0f;
                        // Utiliser le nombre entier ici
                    } catch (NumberFormatException e) {
                        // Gérer l'exception si la chaîne ne peut pas être convertie en int

                    }


                    Articles article = new Articles(nom, qu, prix, qu, "");
                    Singleton.getInstance().getPanier().add(article);
                }
                return "ok";
            }
            else
            {
                return "0";
            }
        }
        return "0";
    }
}