import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

public class simplissimeLocal {

	private static txnscript txn = null ;

    public static void main(String[] args)
	{
		
        try {
            Options opt = new Options();

            opt.addOption("h", false, "Aide de cette application en mode ligne de commande");
            opt.addOption("c", false, "[C]rud : ajouter une ville avec le nom et le code postal");
            opt.addOption("r", false, "c[R]ud : retrouver une ville en fonction d'un parametre (id, nom, code postal)");
            opt.addOption("u", false, "cr[U]d : mettre a jour une ville avec l'id, le nom et le code postal > simplissimeLocal -u -id 21 -nom MARRAKECH -cp 40160");
            opt.addOption("d", false, "cru[D] : effacer une ville en fonction du parametre fourni (id, nom, code postal)");
            opt.addOption("nom", true, "nom de la ville" );
            opt.addOption("cp", true, "code postal de la ville" );
            opt.addOption("id", true, "identifiant de la ville" );
			
            DefaultParser parser = new DefaultParser();
            CommandLine cl = parser.parse(opt, args);

            if ( cl.hasOption('h') ) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("Aide de simplissime", opt);
            }


			String display = "" ;
			txn = txnscript.getTxnscript() ;
			
			// java -cp ".\postgresql-42.2.5.jar;commons-cli-1.4.jar;." simplissimeLocal -u -id 178 -nom MER -cp 41500
            if ( cl.hasOption('u') )
			{
					System.out.println( "\n\nMettre a jour encore le code postal dans la base\n" ) ;
					String sId = cl.getOptionValue("id") ;
					Integer id = Integer.parseInt(sId);
					String nom = cl.getOptionValue("nom") ;
					String sCp = cl.getOptionValue("cp") ;
					Integer cp = Integer.parseInt(sCp);
					display = txn.updateVille ( id, nom, cp ) ;
					System.out.println( display ) ;
            }
            else
			{

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
        catch (ParseException e) {
            e.printStackTrace();
        }

	}
}
