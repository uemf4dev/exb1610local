// package com.mkyong.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement ;
import java.sql.ResultSet;

public class simplissimeLocal {


	private static txnscript txn = txnscript.getTxnscript() ;

    public static void main(String[] args) {

		String display = "" ;
		
		
		boolean isOk = txn.check() ;
		if ( isOk )
		{
			System.out.println( "\n\nEtat initial de la base (0)\n" ) ;
			display = txn.list () ;
			System.out.println( display ) ;
			
			System.out.println( "\n\nAjout d'un enregistrement dans la base\n" ) ;
			display = txn.insertVille ( "FES", 28000 ) ;
			System.out.println( display ) ;
			System.out.println( "\n\nNouvel etat de la base (1)\n" ) ;
			display = txn.list () ;
			
			System.out.println( display ) ;
			System.out.println( "\n\nRecherche dans la base\n" ) ;
			display = txn.searchByCodePostal ( 10251 ) ;
			System.out.println( display ) ;
			
			System.out.println( "\n\nMettre a jour le code postal de MARRAKECH dans la base\n" ) ;
			display = txn.updateVille ( 142, "MARRAKECH", 40160000 ) ;
			System.out.println( display ) ;
			System.out.println( "\n\nNouvel etat de la base (2)\n" ) ;
			display = txn.list () ;
			System.out.println( display ) ;
			
			System.out.println( "\n\nMettre a jour encore le code postal de MARRAKECH dans la base\n" ) ;
			display = txn.updateVille ( 142, "MARRAKECH", 40160 ) ;
			System.out.println( display ) ;
			System.out.println( "\n\nNouvel etat de la base (3)\n" ) ;
			display = txn.list () ;
			System.out.println( display ) ;

			display = txn.close() ;
			System.out.println( display ) ;
		}
	}
}
