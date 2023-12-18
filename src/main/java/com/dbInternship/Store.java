package com.dbInternship;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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
        BufferedReader userin = new BufferedReader(new InputStreamReader(System.in));

        do {
            System.out.println("Geben Sie ein: Waren oder Bestellungen oder Bestellen oder #!");

            try {
                aufgabe = userin.readLine();
            } catch (Exception e) {
                System.out.println("Falscher Input");
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
        String[] custNum = null;

        try (Connection conn = DbUtils.connect()){
            PreparedStatement stmt = conn.prepareStatement(
                "select b.bestid as bid, b.datum as date, b.status as status, k.kname as cid "
                + "from store.bestellung b "
                + "inner join store.kunde k on k.kundeid = b.kundid "
                + "where b.kundid = ?" 
            ); 

            BufferedReader userin = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Bitte Kundennummer(n) (seperiert in Kommata) angeben: ");  
            try {
                custNum = userin.readLine().split(",");
            } catch (Exception e) {
                System.out.println("Fehler beim Einlesen!");
                e.printStackTrace();
            }
            
            System.out.println("\nBestell ID \tDatum \t\tStatus \tKunde");
            System.out.println("--------------------------------------------------------------");

            for (String customer : custNum) {
                stmt.setInt(1, Integer.valueOf(customer.trim()));
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) {
                    System.out.print(rs.getString("bid") + "\t\t");
                    System.out.print(rs.getString("date") + "\t");
                    System.out.print(rs.getString("status") + "\t");
                    System.out.print(rs.getString("cid") + "\n");
                }
            }

        } catch (SQLException | NumberFormatException e) {
            System.out.println("Datenbankfehler: Bitte Angabe ueberpruefen!");
            for (String customer : custNum) {
                System.out.println(customer);
            }
            e.printStackTrace();
        }


    }
        
    public void bestellen() {
        try (Connection conn = DbUtils.connect()) {
            BufferedReader userin = new BufferedReader(new InputStreamReader(System.in));
            ArrayList<String> products = null;
            ArrayList<String> quantitys = null;
            String custName = null;

            try {

                System.out.println("Bitte Kundenname eingeben: ");  
                custName = userin.readLine();

                System.out.println("Bitte Produkte eingeben (kommaseperiert): ");  
                products = new ArrayList<>(Arrays.asList(userin.readLine().split(",")));

                System.out.println("Bitte Anzahl eingeben: (kommaseperiert)");  
                quantitys = new ArrayList<>(Arrays.asList(userin.readLine().split(",")));

            } catch (IOException e) {
                System.out.println("Fehler beim Einlesen!");
                e.printStackTrace();
            }

            Statement stmt = conn.createStatement();
            stmt.execute(
                "insert into store.bestellung "
                + "values("
                    + "nextval('store.bestellid'), "
                    + "CURRENT_DATE, "
                    + "0, "
                    + "(select k.kundeid from store.kunde k where k.kname = '" + custName + "')"
                + ")"
            );

            PreparedStatement pstmt = conn.prepareStatement(
                "insert into store.enthaelt "
                + "values("
                    + "currval('store.bestellid'), "
                    + "(select w.warenid from store.ware w where w.bezeichnung = ?), "
                    + "?"
                + ")"
            );

            for (int i = 0; i < products.size(); i++) {
                pstmt.setString(1, products.get(i).trim());
                pstmt.setInt(2, Integer.valueOf(quantitys.get(i).trim()));

                pstmt.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

