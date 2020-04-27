// package com.mkyong.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement ;
import java.sql.ResultSet;

public class simplissimeLocal {

	// coller ici les 4 lignes obtenues sur https://jdbc4uemf.herokuapp.com/hello?pass=xxx
	private static String jdbcPass = "" ;
	private static String jdbcMachine = "" ;
	private static String jdbcDatabase = "" ;
	private static String jdbcUser = "" ;
	
	private static String jdbcUrl = "jdbc:postgresql://" + jdbcMachine + ":5432/" + jdbcDatabase + "?user=" + jdbcUser + "&password=" + jdbcPass + "&sslmode=require" ;

    public static void main(String[] args) {

		String display = "" ;
		
		boolean isOk = check() ;
		if ( isOk )
		{
			System.out.println( "\n\nEtat de la base\n" ) ;
			display = list () ;
			System.out.println( display ) ;
			System.out.println( "\n\nAjout d'un enregistrement dans la base\n" ) ;
			display = insertVille ( "FES", 28000 ) ;
			System.out.println( display ) ;
			System.out.println( "\n\nNouvel etat de la base\n" ) ;
			display = list () ;
			System.out.println( display ) ;
		}
		
	}

    public static boolean check() {
        boolean result = false ;
		
		// https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
        // auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver class via Class.forName

        // register JDBC driver, optional since java 1.6
		
		if ( jdbcPass.isEmpty() )
		{
			System.out.println("Le mot de passe pour accéder à la base est vide. Connectez-vous à https://jdbc4uemf.herokuapp.com/hello?pass=xxx pour avoir les 4 paramètres de connexion de VOTRE base de données." ) ;
		}
		
        try {
            Class.forName("org.postgresql.Driver");
        }
		catch (ClassNotFoundException e)
		{
            System.out.println("Connected to the database!");
            e.printStackTrace();
        }

        // auto close connection
        /* try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "password")) { */
					
		try (Connection conn = DriverManager.getConnection( jdbcUrl ))
		{

            if (conn != null) {
                System.out.println("Connected to the database!\n");
				result = true ;				
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return result ;
	}



    public static String list() {
        // String sql = "INSERT INTO Villes (nom, code_postal) VALUES(?,?)";
		String result = "" ;
		
		String sql = "SELECT * FROM Villes";


		try (Connection conn = DriverManager.getConnection( jdbcUrl ))
		{
            PreparedStatement preparedStatement = conn.prepareStatement(sql) ;

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                // Integer id = resultSet.getInteger("ID");
                Integer codePostal = resultSet.getInt("code_postal");
				result = result + "/" + codePostal ;
                String name = resultSet.getString("Nom");
				result = result + "/" + name ;
		result = result + "\n" ;
                // Timestamp createdDate = resultSet.getTimestamp("CREATED_DATE");
            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return result ;
	}
	
	

    public static String insertVille (String nom, Integer codePostal) {
        String result = "" ;
		
		String sql = "INSERT INTO Villes (nom, code_postal) VALUES(?,?)" ;

		try (Connection conn = DriverManager.getConnection( jdbcUrl ))
		{
            PreparedStatement pstmt = conn.prepareStatement(sql) ;
            pstmt.setString(1, nom);
            pstmt.setDouble(2, codePostal);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
		result = result + "/" + codePostal ;
		result = result + "/" + nom ;
		
		return result ;
    }

	
}
