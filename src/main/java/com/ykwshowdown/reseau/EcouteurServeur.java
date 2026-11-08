package com.ykwshowdown.reseau;

/**
 * Recoit les lignes envoyees par le serveur.
 * Appele sur le thread Swing : on peut modifier l'interface directement.
 */
public interface EcouteurServeur {
    void messageRecu(String ligne);
}
