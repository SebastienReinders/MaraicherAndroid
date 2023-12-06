package com.example.maraicher;

public class Articles {
    private String nomArticle;
    private int ArticleEnCours = 1;
    private int quantiteArticle;
    private float prixArticle;
    private int quDemande;
    private String image = "";

    // Constructeur
    public Articles(String nomArticle, int quantiteArticle, float prixArticle, int quDemande, String image) {
        this.nomArticle = nomArticle;
        this.quantiteArticle = quantiteArticle;
        this.prixArticle = prixArticle;
        this.quDemande = quDemande;
        this.image = image;
    }

    // Getters
    public String getNomArticle() {
        return nomArticle;
    }

    public int getQuantiteArticle() {
        return quantiteArticle;
    }

    public String getQuantiteEnString()
    {
        return String.valueOf(quantiteArticle);
    }

    public float getPrixArticle() {
        return prixArticle;
    }

    public String getPrixEnString()
    {
        return String.format("%.2f",prixArticle);
    }

    public int getQuDemande() {
        return quDemande;
    }

    // Setters
    public void setNomArticle(String nomArticle) {
        this.nomArticle = nomArticle;
    }

    public void setQuantiteArticle(int quantiteArticle) {
        this.quantiteArticle = quantiteArticle;
    }

    public void setPrixArticle(float prixArticle) {
        this.prixArticle = prixArticle;
    }

    public void setQuDemande(int quDemande) {
        this.quDemande = quDemande;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
