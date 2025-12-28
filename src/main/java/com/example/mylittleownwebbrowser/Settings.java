package com.example.mylittleownwebbrowser;

import java.io.*;

public class Settings {
    private String startseite;
    private static final String CONFIGFILE = "settings.cfg";

    public Settings(String startseite) {
        this.startseite = startseite;
        File file1 = new File(CONFIGFILE);
        if (!file1.exists() && !file1.isDirectory()) {
            saveAllConfigurations();
        }
    }

    public String getStartseite() {
        return startseite;
    }

    public void setStartseite(String startseite) {
        this.startseite = startseite;
    }

    public void saveAllConfigurations() {
        try (FileWriter writer = new FileWriter(CONFIGFILE, false)) {
            writer.write(startseite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadfallconfigurations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIGFILE))) {
            String zeile1 = reader.readLine();
            startseite = zeile1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
