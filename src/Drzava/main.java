package Drzava;
/*Napiši Java konzolnu aplikaciju sa sljedećim funkcionalnostima:
Korisniku se prikazuje izbornik sa sljedećim opcijama:
1 – nova država
2 - izmjena postojeće države
3 - brisanje postojeće države
4 – prikaz svih država sortiranih po nazivu
5 – kraj

Opcije 1 do 4 odnose se na CRUD operacije and tablicom Drzava u bazi AdventureWorks
Odabirom opcije 2 i 3, od korisnika je potrebno tražiti ID države koje
želite izmijeniti ili pobrisati
Napomena: brišite i mijenjajte samo one države koje ste Vi ubacili (one za koje je IdDrzava veći od 3)
 */


import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            Scanner sc = new Scanner(System.in);
            int izbor = 0;
            while (izbor != 5) {
                System.out.println("1 – nova država");
                System.out.println("2 - izmjena postojeće države");
                System.out.println("3 - brisanje postojeće države");
                System.out.println("4 – prikaz svih država sortiranih po nazivu");
                System.out.println("5 – kraj");
                System.out.println("Unesite radnju koju zelite izvršiti");

                if (sc.hasNextInt()) {
                    izbor = sc.nextInt();
                    sc.nextLine(); // obavezno očisti Enter znak
                } else {
                    System.out.println("Pogrešan unos. Molimo unesite broj.");
                    sc.nextLine(); // očisti unos
                    continue;
                }

                if (izbor == 1) {
                    System.out.println("Unesite naziv nove drzave:");
                    String nazivDrzave = sc.nextLine();
                    int rowAffected = stmt.executeUpdate("INSERT INTO Drzava ( Naziv) VALUEs ('" + nazivDrzave + "')");
                    System.out.println("Nova država je uspješno dodana.");
                }
                if (izbor == 2) {
                    System.out.println("Izmjenite naziv postojece drzave");

                    ResultSet rs = stmt.executeQuery("SELECT IDDrzava, Naziv FROM DRZAVA");
                    while (rs.next()) {
                        System.out.printf("%d - %s\n", rs.getInt("IDDrzava"), rs.getString("Naziv"));
                    }
                    System.out.println("Unesite ID drzave kojoj zelite promijeniti naziv");
                    String IDDrzave = sc.nextLine();
                    System.out.println("Unesite novi naziv drzave:");
                    String noviNaziv = sc.nextLine();
                    int rowAffected = stmt.executeUpdate("UPDATE Drzava SET Naziv=('" + noviNaziv + "') Where IDDrzava=('" + IDDrzave + "')");
                    System.out.println("Uspješno promijenjen naziv dzave");
                }
                if (izbor == 3) {
                    System.out.println("Unesite IDDrzave koju zelite pobrisati");
                    ResultSet rs = stmt.executeQuery("SELECT IDDrzava, Naziv FROM DRZAVA");
                    while (rs.next()) {
                        System.out.printf("%d - %s\n", rs.getInt("IDDrzava"), rs.getString("Naziv"));
                    }
                    String IDDrzave = sc.nextLine();
                    int rowAffected = stmt.executeUpdate("Delete Drzava  Where IDDrzava=('" + IDDrzave + "')");
                    System.out.println("Uspješno ste pobrisali drzavu sa ID-jem:" + IDDrzave);
                }
                if (izbor == 4) {
                    System.out.println("Drzave sortiranr po nazivu");
                    ResultSet rs = stmt.executeQuery("SELECT IDDrzava, Naziv FROM DRZAVA order by Naziv");
                    while (rs.next()) {
                        System.out.printf("%d - %s\n", rs.getInt("IDDrzava"), rs.getString("Naziv"));
                    }
                }
                if (izbor == 5) {
                    System.out.println("Kraj programa. ");
                    break; // izađi iz while petlje
                }
            }

            } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja na bazu podataka");
            e.printStackTrace();


        }
    }

    private static DataSource createDataSource() {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        //ds.setPortNumber(1433);
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt(false);

        return ds;
    }
}