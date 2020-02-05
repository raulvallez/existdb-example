package ExemplesCodis;

import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;
import org.xmldb.api.*;


public class RetrieveExample {
    
    public static void main(String args[]) throws Exception {
        String driver = "org.exist.xmldb.DatabaseImpl"; //Driver
        Class cl = Class.forName(driver); //Cargar Driver
        Database database = (Database) cl.newInstance(); //Instancia de la BD
        DatabaseManager.registerDatabase(database); //Registrar DB
        database.setProperty("create-database", "true");
        Collection col = DatabaseManager.getCollection("xmldb:exist://localhost:8080/exist/xmlrpc/db/", "<<USER>>", "<<PASSWORD>>");
        col.setProperty("pretty", "true");
        col.setProperty("encoding", "ISO-8859-1");
        XMLResource res = (XMLResource) col.getResource("prueba.xml");
        if (res == null) {
            System.err.println("could not retrieve document " + args[0] + "!");
            return;
        }
        System.out.println((String) res.getContent()); //Recupero la inf.
    }
}