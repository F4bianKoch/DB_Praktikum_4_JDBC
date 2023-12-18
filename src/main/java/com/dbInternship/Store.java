package com.dbInternship;

import java.io.*;
import java.sql.*;

import com.utils.DbUtils;

/**
 * Main class for store front.
 * 
 * Provides a simple CLI UI for the store database.
 * 
 * Possible inputs:
 *  1. "Waren" - prints out all goods
 *  2. "Bestellungen" - prints out all orders from given Customers
 *  3. "Bestellung" - UI for generating a new order
 */
public class Store {
    public static void main(String args[]) {
        if (args.length != 0) {
            System.out.println("Usage: java supermarkt");
            System.exit(1);
        }

        Store store = new Store();
        String aufgabe = null;
        BufferedReader userin = new BufferedReader(new InputStreamReader (System.in));

        do {
            System.out.println("Geben Sie ein: Waren oder Bestellungen oder Bestellen oder #!");

            try {
                aufgabe = userin.readLine();
            } catch (Exception e) {
                System.out.println("Error while reading user Input!");
                e.printStackTrace();
                break;
            }

            if (aufgabe.equals("Waren")) {
                store.alleWaren();
            } 
            if (aufgabe.equals("Bestellungen")) {
                store.alleBestellungen();
            } 
            if (aufgabe.equals("Bestellen")) {
                store.bestellen();
            }

            System.out.println("--------------------------------------------------------------");

        } while (!aufgabe.startsWith("#") || aufgabe == null);
    }

    public void alleWaren() {
        try (Connection conn = DbUtils.connect()) {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                "select w.bezeichnung as Bezeichnung, w.preis as Preis, w.vorrat as Vorrat, k.kname as Kategorie"
                + " from store.ware w"
                + " inner join store.kategorie k on w.katid = k.katid"
            );

            System.out.println("\nBezeichnung \tPreis \tVorrat \tKategorie");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                System.out.print(rs.getString("Bezeichnung") + "\t");
                System.out.print(rs.getString("Preis") + "\t");
                System.out.print(rs.getString("Vorrat") + "\t");
                System.out.print(rs.getString("Kategorie") + "\n");
            }

        } catch (SQLException e) {
            System.out.println("Internal SQL Error: ");
            e.printStackTrace();
        }
    }
        
    public void alleBestellungen() {
        return;
    }
        
    public void bestellen() {
        return;
    }
}

