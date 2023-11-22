package com.example.maraicher;

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
                    return "faux";
                } else {
                    return "vrai";
                }
            } else {
                return "Erreur";
            }
        } else {
            return "Erreur";
        }
    }
}
