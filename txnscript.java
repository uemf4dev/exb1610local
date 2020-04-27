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

public class txnscript
{
	
	// coller ici les 4 lignes obtenues sur https://jdbc4uemf.herokuapp.com/hello?pass=xxx
	private static String jdbcPass = "" ;
	private static String jdbcMachine = "" ;
	private static String jdbcDatabase = "" ;
	private static String jdbcUser = "" ;
	
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
// ************************************** END OF SIMPLISSIME ***************************************

  
}
