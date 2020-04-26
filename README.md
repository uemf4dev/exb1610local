# exb1610local
En résumé, il s'agit de la mise en oeuvre de la partie cliente de _simplissime_, l'application du domaine _carnet d'adresses_.
A exécuter en local, sur un ordinateur équipé de Java 8 minimum et qui accède à une _machine Heroku_ opérant un SGBD _PostgreSQL_ avec une base Carnet_D_Adresses.

## Mode opératoire

### Cas 1 : java et git installé
```
> git clone https://github.com/prodageo/exb1610local.git
> javac simplissime.java
> java -cp ".\postgresql-42.2.5.jar;." simplissime
```

### Cas 2 : java installé, git non installé
```
> c:
> mkdir c:\temp
> cd c:\temp
navigateur:github.io>  cliquer sur le bouton _Clone or download_ et sauver le fichier dans le répertoire c:\temp sous le nom exb1610local-master.zip
> jar -xf exb1610local-master.zip
```

### Autre cas
Voir exb1610web
