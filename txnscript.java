// for more information, please visit : http://prodageo.insa-rouen.fr/wiki/pmwiki.php?n=FilRouge.CoderTransactionScript

// package lib ;

import java.util.Date;

// import libinsa.txnscriptUtil ;
// import libinsa.insaLogger ;

// types retournés par les opérations JDBC
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
// import java.sql.ResultSet;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class txnscript
{
	
	// coller ici les 4 lignes obtenues sur https://jdbc4uemf.herokuapp.com/admin?pass=xxx
/*
	private static String jdbcPass = "" ;
	private static String jdbcMachine = "" ;
	private static String jdbcDatabase = "" ;
	private static String jdbcUser = "" ;
*/

private static String jdbcPass = "a1b7022433ceb48f90e2759a4319f73d3af2bbdee4f214477c90588caf8ae71f" ;
private static String jdbcMachine = "ec2-54-246-121-32.eu-west-1.compute.amazonaws.com" ;
private static String jdbcDatabase = "d6v79l0erm7t35" ;
private static String jdbcUser = "rxfftsrckuwnsp" ;

	private static String jdbcUrl = "jdbc:postgresql://" + jdbcMachine + ":5432/" + jdbcDatabase + "?user=" + jdbcUser + "&password=" + jdbcPass + "&sslmode=require" ;
	private static String saut_de_ligne = "\n" ;

	static Connection connection = null ;
	static Statement stmt = null ;
	// private static insaLogger logger = insaLogger.getLogger(txnscript.class);
	
    private txnscript()
    {
	// recuperer la JDBC URL
	try {
		// jdbcUrl = System.getenv("JDBC_DATABASE_URL");
		connection = DriverManager.getConnection(jdbcUrl);
	}
	catch (Exception e)
	{
		// logger.error ( "JDBC_DATABASE_URL : " + jdbcUrl ) ;
		
	}   
	// ouvrir une connection

    }   

    public static txnscript getTxnscript() {
        return new txnscript();
    }
	
	
	
	
// ************************************** FCTS OF SIMPLISSIME ***************************************


	
    public static boolean check()
    {
        boolean result = false ;
		StringBuilder out = new StringBuilder() ;
		
	// https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
        // auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver class via Class.forName

        // register JDBC driver, optional since java 1.6
		
	if ( jdbcUrl.isEmpty() )
	{
		out.append("<p>Le mot de passe pour accéder à la base est vide. Connectez-vous à https://jdbc4uemf.herokuapp.com/hello?pass=xxx pour avoir les 4 paramètres de connexion de VOTRE base de données.</p>" ) ;
	}
	
	// vérifier dispo de la library postgreSQL
        try
	{
            Class.forName("org.postgresql.Driver");
        }
	catch (ClassNotFoundException e)
	{
            out.append("<p>La bibliothèque PostgreSQL est absente !</p>");
            e.printStackTrace();
        }

        // auto close connection
        /* try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "password")) { */
					
	try
	{
		Connection conn = DriverManager.getConnection( jdbcUrl );
		if (conn != null)
		{
			out.append("<p>Connected to the database!</p>\n");
			result = true ;				
		} else {
			out.append("<p>Failed to make connection!</p>");
		}
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	return result ;
    }



    public static String list()
    {
        // String sql = "INSERT INTO Villes (nom, code_postal) VALUES(?,?)";
	String result = "" ;
		
	String sql = "SELECT * FROM Villes";

	try
	{
            PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
	    {
                // Integer id = resultSet.getInt("ID");
                Integer codePostal = resultSet.getInt("code_postal");
				result = result + "/" + codePostal ;
                String name = resultSet.getString("Nom");
				result = result + "/" + name ;
		result = result + saut_de_ligne ;
                // Timestamp createdDate = resultSet.getTimestamp("CREATED_DATE");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
	return result ;
    }
	
	

    public static String insertVille (String nom, Integer codePostal)
    {
        String result = "" ;
		
	String sql = "INSERT INTO Villes (nom, code_postal) VALUES(?,?)" ;

	try
	{
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
            pstmt.setString(1, nom);
            pstmt.setDouble(2, codePostal);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
	result = result + codePostal ;
	result = result + "/" + nom ;
	result = result + saut_de_ligne ;		
	return result ;
    }
	
	public static boolean loadDml ( String filePath)
	{
		return playSqlInsert ( readDml ( filePath ) ) ;
	}
	
    public static boolean playSqlInsert (String sql)
    {
		
		boolean result = true ;

		try
		{
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
			result = false ;
        }
		
		return result ;
    }

    public static boolean deleteTableContent (String tableName)
    {
		
		boolean result = true ;
		String sql = "DELETE FROM " + tableName + ";" ;

		try
		{
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
			result = false ;
        }
		return result ;
    }
    
	// readLineByLineJava8
	private static String readDml (String filePath) 
	{
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
		{
			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}

	
// ************************************** END OF SIMPLISSIME ***************************************

  
}
