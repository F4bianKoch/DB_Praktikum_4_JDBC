package com.dbInternship;

import java.io.*;

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
    public static void main (String args[]) {
        if (args.length != 0) {
            System.out.println ("Usage: java supermarkt");
            System.exit(1);
        }

        Store store = new Store();
        String aufgabe = null;
        BufferedReader userin = new BufferedReader (new InputStreamReader (System.in));

        do {
            System.out.println("Geben Sie ein: Waren oder Bestellungen oder Bestellen oder #!");

            try {
                aufgabe = userin.readLine();
            } catch (Exception e) {
                System.out.println("Error while reading user Input!");
                e.printStackTrace();
                break;
            }

            if (aufgabe.equals ("Waren")) {
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

    public void alleWaren () {
        return;
    }
        
    public void alleBestellungen() {
        return;
    }
        
    public void bestellen() {
        return;
    }
}

