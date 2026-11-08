package com.ykwshowdown.chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ykwshowdown.reseau.ConnexionServeur;

/**
 * Ordre
 *   client -> serveur  : /chat/<texte>
 *   serveur -> clients : /chat/<pseudo>/<texte>   et   /info/<texte>
 */
public class ModeleChat {

    public static final int TAILLE_MAX = 200;

    private final List<Message> messages = new ArrayList<>();
    private final List<ObservateurChat> observateurs = new ArrayList<>();
    private final ConnexionServeur serveur;   // null = hors ligne
    private String pseudo = "Invite";          // utilise seulement hors ligne

    public ModeleChat(ConnexionServeur serveur) {
        this.serveur = serveur;
    }

    // ---------- Actions ----------

    /** Envoie un message. */
    public void envoyer(String texte) {
        texte = texte.trim();
        if (texte.isEmpty()) {
            return;
        }
        if (texte.length() > TAILLE_MAX) {
            texte = texte.substring(0, TAILLE_MAX);
        }
        if (serveur != null) {
            serveur.envoyer("/chat/" + texte);
        } else {
            ajouter(Message.maintenant(pseudo, texte));   // hors ligne : affichage direct
        }
    }

    /**
     * Traite une ligne recue du serveur.
     * @return true si la ligne concernait le chat
     */
    public boolean recevoir(String ligne) {
        if (ligne.startsWith("/chat/")) {
            String[] m = ligne.substring(1).split("/", 3);   // ["chat", pseudo, texte]
            if (m.length == 3) {
                ajouter(Message.maintenant(m[1], m[2]));
            }
            return true;
        }
        if (ligne.startsWith("/info/")) {
            ajouter(Message.maintenant("Systeme", ligne.substring("/info/".length())));
            return true;
        }
        return false;
    }

    public void ajouter(Message message) {
        messages.add(message);
        prevenirObservateurs();
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    // ---------- Lecture ----------

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }



    public void addObservateur(ObservateurChat o) {
        observateurs.add(o);
        o.chatModifie();
    }

    private void prevenirObservateurs() {
        for (ObservateurChat o : observateurs) {
            o.chatModifie();
        }
    }
}
