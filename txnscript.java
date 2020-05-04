// for more information, please visit : http://prodageo.insa-rouen.fr/wiki/pmwiki.php?n=FilRouge.CoderTransactionScript

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
	private static String jdbcPass = "" ;
	private static String jdbcMachine = "" ;
	private static String jdbcDatabase = "" ;
	private static String jdbcUser = "" ;

	private static String jdbcUrl = "jdbc:postgresql://" + jdbcMachine + ":5432/" + jdbcDatabase + "?user=" + jdbcUser + "&password=" + jdbcPass + "&sslmode=require" ;
	private static String saut_de_ligne = "\n" ;

	static Connection cnx = null ;
	static Statement stmt = null ;
	static PreparedStatement pstmt = null ;
	static ResultSet resultSet = null ;
	// private static insaLogger logger = insaLogger.getLogger(txnscript.class);
	
    private txnscript()
    {
		// recuperer la JDBC URL
		try
		{
			// jdbcUrl = System.getenv("JDBC_DATABASE_URL");
			cnx = DriverManager.getConnection(jdbcUrl);
		}
		catch (Exception e)
		{
			// logger.error ( "JDBC_DATABASE_URL : " + jdbcUrl ) ;
			
		}   

    }   

	public static String getJdbcUrl ()
	{
		return jdbcUrl ;
	}

    public static txnscript getTxnscript() {
        return new txnscript();
    }
	
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

	
    public static String insertVille (String nom, Integer codePostal)
    {
        String result = "" ;
		
		String sql = "INSERT INTO Villes (nom, code_postal) VALUES(?,?)" ;

		try
		{
				PreparedStatement pstmt = cnx.prepareStatement(sql) ;
				pstmt.setString(1, nom);
				pstmt.setDouble(2, codePostal);
				pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
			
		result = result + codePostal ;
		result = result + "/" + nom ;
		result = result + saut_de_ligne ;		
		return result ;
    }
	
    public static String list()
    {
		String result = "" ;
			
		String sql = "SELECT * FROM Villes";

		try
		{
			PreparedStatement preparedStatement = cnx.prepareStatement(sql) ;
			resultSet = preparedStatement.executeQuery();

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


    public static String searchByCodePostal(Integer searchedCodePostal)
    {
		String result = "" ;
			
		String sql = "SELECT * FROM Villes WHERE code_postal = ?" ;

		try
		{
			PreparedStatement preparedStatement = cnx.prepareStatement(sql) ;
			preparedStatement.setInt(1, searchedCodePostal);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Integer id = resultSet.getInt("id");
				result = result + "/" + id ;
				Integer codePostal = resultSet.getInt("code_postal");
				result = result + "/" + codePostal ;
				String name = resultSet.getString("nom");
				result = result + "/" + name ;
				result = result + saut_de_ligne ;
			}
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
		return result ;
    }

	
	
    public static String updateVille (Integer id, String nom, Integer codePostal)
    {
        String result = "" ;
		
		String sql = "UPDATE Villes SET nom = ?, code_postal = ? WHERE id = ?" ;

		try
		{
				PreparedStatement pstmt = cnx.prepareStatement(sql) ;
				pstmt.setString(1, nom);
				pstmt.setDouble(2, codePostal);
				pstmt.setInt(3, id);
				pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
			
		result = result + id ;
		result = result + "/" + codePostal ;
		result = result + "/" + nom ;
		result = result + saut_de_ligne ;		
		return result ;
    }
	

	
	
    public static String close()
    {
		String result = "" ;
		boolean isClotureOK = true ;
	
		try
		{
			if(resultSet!=null)
			{
				resultSet.close();
				resultSet=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}
		
		try
		{
			if(stmt!=null)
			{
				stmt.close();
				stmt=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}
		
		try
		{
			if(pstmt!=null)
			{
				pstmt.close();
				pstmt=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}

		try
		{
			if(cnx!=null)
			{
				cnx.close();
				cnx=null;
			}
		}
		catch (SQLException e)
		{
			isClotureOK = false ;
			e.printStackTrace();
		}

		if ( isClotureOK == true )
		{
			result = "Cloture objets JDBC OK" ;
		}
		else
		{
			result = "Cloture objets JDBC KO" ;
		}

		return result ;
	}
}
