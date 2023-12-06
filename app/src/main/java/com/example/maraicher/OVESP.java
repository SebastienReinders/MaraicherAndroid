package com.example.maraicher;

import android.util.Log;

public class OVESP {

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

                    Articles article = new Articles(Singleton.getInstance().getArticleCourant().getNomArticle(),0,Singleton.getInstance().getArticleCourant().getPrixArticle(),Singleton.getInstance().getQuDemande(),"");

                    Singleton.getInstance().getPanier().add(article);

                    Singleton.getInstance().calculTotal();

                    return "vrai";
                }



               // return "BLABLA";
            } else
            {
                return "0";
            }
        }
        return "0";
    }
}