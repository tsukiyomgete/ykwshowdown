package com.ykwshowdown.connexion;

import com.ykwshowdown.database.DataBase;
import com.ykwshowdown.user.User;

public class Connexion {
    private User userConnecte;

    public boolean connecter(String pseudo, String mdp) {
        if (DataBase.getMdp(pseudo, mdp)) {
            userConnecte = new User(pseudo, mdp);
            return true;
        }
        return false;
    }

    public User getUserConnecte() { return userConnecte; }
}
