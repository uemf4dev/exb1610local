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
			System.out.println( "\n\nEtat de la base\n" ) ;
			display = txn.list () ;
			System.out.println( display ) ;
			System.out.println( "\n\nAjout d'un enregistrement dans la base\n" ) ;
			display = txn.insertVille ( "FES", 28000 ) ;
			System.out.println( display ) ;
			System.out.println( "\n\nNouvel etat de la base\n" ) ;
			display = txn.list () ;
			System.out.println( display ) ;
		}
		
	}

	
}
