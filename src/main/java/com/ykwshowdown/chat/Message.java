package com.ykwshowdown.chat;

import java.time.LocalTime;

/** Un message du chat. */
public record Message(String auteur, String texte, LocalTime heure) {

    public static Message maintenant(String auteur, String texte) {
        return new Message(auteur, texte, LocalTime.now());
    }
}
