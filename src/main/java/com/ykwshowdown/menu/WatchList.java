package com.ykwshowdown.menu;

import java.util.*;
import com.ykwshowdown.yokai.*;
import com.ykwshowdown.user.*;

public class WatchList {

    final int MAXTEAM = 20;
    ArrayList<WatchBuilder> yourTeam;
    int nbTeam;
    User userActuel;

    Scanner sc = new Scanner(System.in);

    public WatchList() {
        yourTeam = new ArrayList<WatchBuilder>();
        nbTeam = 0;

    }

    public void menu(User userx) {
        int choix = -1;
        userActuel = userx;

        while (choix != 4) {
            System.out.println("    MENU DES EQUIPES    ");
            System.out.println("1) Crée une équipe");
            System.out.println("2) Afficher les équipes");
            System.out.println("3) Importer une équipe");
            System.out.println("4) Selectionner une équipe");
            System.out.println("5) Quitter");
            choix = Integer.parseInt(sc.nextLine());

            if (choix == 1) {
                addTeam();
            }
            if (choix == 2) {
                printTeam();
            }
            if (choix == 3) {
                importTeam();
            }
            if (choix == 4) {
                selectTeam();
            }
            if (choix == 5) {
                System.out.println("Bye !");
            }

        }

    }

    // ===== TODO rename =====

    public int getTeamCount() {
        return yourTeam.size();
    }

    public WatchBuilder getTeam(int index) {
        return yourTeam.get(index);
    }

    /** TODO rename */
    public WatchBuilder newTeam() {
        if (yourTeam.size() >= MAXTEAM) {
            return null;
        }
        int n = 1;
        while (hasTeamNamed("Untitled " + n)) {
            n++;
        }
        WatchBuilder team = new WatchBuilder("Untitled " + n);
        team.setFormat(Tier.OU);
        yourTeam.add(team);
        nbTeam = yourTeam.size();
        return team;
    }

    /** TODO rename */
    public WatchBuilder copyTeam(int index) {
        if (yourTeam.size() >= MAXTEAM) {
            return null;
        }
        WatchBuilder copie = yourTeam.get(index).copy();
        copie.setTeamName("Copy of " + yourTeam.get(index).GetTeamName());
        yourTeam.add(index + 1, copie);
        nbTeam = yourTeam.size();
        return copie;
    }

    public void removeTeam(int index) {
        yourTeam.remove(index);
        nbTeam = yourTeam.size();
    }

    private boolean hasTeamNamed(String name) {
        for (WatchBuilder t : yourTeam) {
            if (t.GetTeamName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public User selectTeam() {
        System.out.println("Selectionne ton équipe");
        String nomTeam = sc.nextLine();
        for (int i = 0; i < yourTeam.size(); i++) {
            if (yourTeam.get(i) != null && nomTeam.equalsIgnoreCase(yourTeam.get(i).GetTeamName())) {
                userActuel.setTeam(yourTeam.get(i));
                return userActuel;
            }
        }
        System.out.println("Aucune équipe trouvée");
        return null;
    }

    public void addTeam() {
        if (nbTeam < MAXTEAM) {
            System.out.println("Choisissez le nom de votre équipe :");
            String nomTeam = sc.nextLine();

            yourTeam.add(nbTeam, new WatchBuilder(nomTeam));
            nbTeam++;
            System.out.println("L'équipe " + nomTeam + " a été crée! ");
            // ← supprime printTeam() ici
        } else {
            System.out.println("Nombre maximum d'équipe atteintes !");
        }
    }

    public void importTeam() {
        System.out.println("Veuillez copiez coller votre équipe ci dessous svp (ligne vide pour terminer)");
        StringBuilder sb = new StringBuilder();
        String line;
        while (!(line = sc.nextLine()).isEmpty()) {
            sb.append(line).append("\n");
        }
        WatchBuilder yT = new WatchBuilder();
        yT.importTeam(sb.toString());
        yourTeam.add(yT);
        nbTeam++;
        while (sc.hasNextLine() && !sc.nextLine().isEmpty()) {
        }
    }

    public void printTeam() {
        int choix = -1;
        while (choix != 3) {
            if (nbTeam == 0) {
                System.out.println("Aucune équipe n'est encore disponible");
                return;
            }
            System.out.println("    TES EQUIPES     ");
            System.out.printf("%-10s %-5s%n", "Nom de l'équipe", "Nombre de yokai");
            for (int i = 0; i < nbTeam; i++) {
                WatchBuilder wT = yourTeam.get(i);
                System.out.printf("%-10s %-5d%n", wT.GetTeamName(), wT.GetTeamSize());
            }
            System.out.println("        ");
            System.out.println("        ");
            System.out.println("        ");
            System.out.println("        ");

            System.out.println("Voici toutes les options à ta disposition :");
            System.out.println("1) Modifier");
            System.out.println("2) Afficher");
            System.out.println("3) Importer une équipe");
            System.out.println("4) Quitter");
            choix = Integer.parseInt(sc.nextLine());
            if (choix == 1) {
                modifyTeam();
                break;
            }
            if (choix == 2) {
                System.out.println("Quel équipe souhaitez vous observez ?");
                String str = sc.nextLine();
                for (int i = 0; i < yourTeam.size(); i++) {
                    if (yourTeam.get(i) != null && str.equalsIgnoreCase(yourTeam.get(i).GetTeamName())) {
                        System.out.println(yourTeam.get(i));
                        System.out.println("Voulez vous plus de détails sur l'équipe ?");
                        str = sc.nextLine();
                        switch (str) {
                            case "Oui":
                                yourTeam.get(i).print();
                                break;
                            default:
                                return;
                        }
                        return;
                    }
                }
                System.out.println("Aucune équipe trouvée");
            }
            if (choix == 3) {
                importTeam();
            } else if (choix == 4) {
                break;
            }
        }
    }

    public void modifyTeam() {
        System.out.println("Quel équipe souhaitez vous modifier ?");
        String nomTeam = sc.nextLine();
        for (int i = 0; i < yourTeam.size(); i++) {
            if (yourTeam.get(i) != null && nomTeam.equalsIgnoreCase(yourTeam.get(i).GetTeamName())) {
                System.out.println("Quel action souhaitez vous faire ?");
                System.out.println("1) Ajouter des Yokai");
                System.out.println("2) Modifier les IV d'un Yokai");
                System.out.println("3) Remplacer des yokai");
                System.out.println("4) Changer l'equipement d'un Yokai");
                System.out.println("5) Changer l'attitude d'un Yokai");
                System.out.println("Sinon Rien");
                int choix = Integer.parseInt(sc.nextLine());

                switch (choix) {
                    case 1:
                        addYokaiToTeam(i);
                        break;
                    case 2:
                        setYokaiIvTeam(i);
                        break;
                    case 3:
                        replaceYokaiTeam(i);
                        break;
                    case 4:
                        setYokaiEquipmentTeam(i);
                        break;
                    case 5:
                        setYokaiAttitudeTeam(i);
                        break;
                    default:
                        return;
                }
            }

        }
        System.out.println("Aucune équipe trouvée");
        return;
    }

    public void addYokaiToTeam(int x) {
        System.out.println(yourTeam.get(x));
        while (yourTeam.get(x).GetTeamSize() < 6) {

            System.out.println("Slots restants : " + (6 - yourTeam.get(x).GetTeamSize()));
            System.out.println("Commande : add <rang> <nomYokai> | quit pour terminer");
            String cmd = sc.nextLine();

            if (cmd.equalsIgnoreCase("quit")) {
                break;
            }

            String[] parts = cmd.split(" ");
            if (parts[0].equalsIgnoreCase("add")) {
                int rang = Integer.parseInt(parts[1]);
                String nomYokai = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                yourTeam.get(x).addYokai(rang, nomYokai);
            }
        }
        return;
    }

    public void setYokaiIvTeam(int x) {
        System.out.println(yourTeam.get(x));
        String cmd;

        do {
            System.out.println("Commandes : setIV <rang> <HP> <STR> <SPR> <DEF> <SPE> | randomIV <rang> | quit pour arrêter");
            System.out.println("Regle : HP/2 + STR + SPR + DEF + SPE <= " + IV.MAXIVPOINTS + " (HP pair)");
            cmd = sc.nextLine().trim();
            if (cmd.equalsIgnoreCase("quit")) {
                break;
            }
            String[] parts = cmd.split("\\s+");
            try {
                if (parts[0].equalsIgnoreCase("setIV") && parts.length == 7) {
                    int rang = Integer.parseInt(parts[1]);
                    yourTeam.get(x).setIV(rang, Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                            Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
                    System.out.println("IV modifies.");
                } else if (parts[0].equalsIgnoreCase("randomIV") && parts.length == 2) {
                    yourTeam.get(x).generateIV(Integer.parseInt(parts[1]));
                    System.out.println("IV generes.");
                } else {
                    System.out.println("Commande inconnue");
                }
            } catch (NumberFormatException e) {
                System.out.println("Les valeurs doivent etre des nombres");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
        yourTeam.get(x).print();
    }

    public void setYokaiEquipmentTeam(int x) {
        System.out.println(yourTeam.get(x));
        String cmd;
        do {
            System.out.println("Commande : equip <rang> <Nom Equipement> | unequip <rang> | quit pour quitter");
            cmd = sc.nextLine().trim();
            if (cmd.equalsIgnoreCase("quit")) {
                break;
            }
            String[] parts = cmd.split(" ");
            try {
                if (parts[0].equalsIgnoreCase("equip") && parts.length >= 3) {
                    String nom = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    if (!yourTeam.get(x).setEquipment(Integer.parseInt(parts[1]), nom)) {
                        System.out.println("Equipement inconnu : " + nom);
                    }
                } else if (parts[0].equalsIgnoreCase("unequip") && parts.length == 2) {
                    yourTeam.get(x).setEquipment(Integer.parseInt(parts[1]), "");
                } else {
                    System.out.println("Commande inconnue");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
        yourTeam.get(x).print();
    }

    public void setYokaiAttitudeTeam(int x) {
        System.out.println(yourTeam.get(x));
        String cmd;
        do {
            System.out.println("Commande : attitude <rang> <Nom Attitude> | quit pour quitter");
            cmd = sc.nextLine().trim();
            if (cmd.equalsIgnoreCase("quit")) {
                break;
            }
            String[] parts = cmd.split(" ");
            try {
                if (parts[0].equalsIgnoreCase("attitude") && parts.length >= 3) {
                    String nom = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    if (!yourTeam.get(x).setAttitude(Integer.parseInt(parts[1]), nom)) {
                        System.out.println("Attitude inconnue : " + nom);
                    }
                } else {
                    System.out.println("Commande inconnue");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
        yourTeam.get(x).print();
    }

    public void replaceYokaiTeam(int x) {
        System.out.println(yourTeam.get(x));
        String cmd;
        do {
            System.out.println("Commande : replace <rang> <Nom Yokai> | quit pour quitter");
            cmd = sc.nextLine();
            if (!cmd.equalsIgnoreCase("quit")) {
                String[] parts = cmd.split(" ");
                if (parts[0].equalsIgnoreCase("replace")) {
                    int rang = Integer.parseInt(parts[1]);
                    String nomYokai = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    yourTeam.get(x).replaceYokai(rang, nomYokai);
                }
            }
        } while (!cmd.equalsIgnoreCase("quit"));
    }
}
