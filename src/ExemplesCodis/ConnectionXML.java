
package ExemplesCodis;


import org.exist.xmldb.EXistXQueryService;
import org.exist.xmldb.XmldbURI;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;

public class ConnectionXML {

    //Definimos la URL con la que vamos a trabajar
    String connectionString =  "xmldb:exist://localhost:9080/exist/xmlrpc" ;

    //Inicializamos los parámetros necesarios
    private  ResourceSet result;
    String selectSql="";
    List<Float> tiempos = new ArrayList<Float>();
    public List<Float> HacerConsulta(String consulta) {

        try {
            //Iniciamos el driver
            Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
            //Abrimos la conexión
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);

            System.out.println(consulta);


            // get root-collection
            Collection col =
//                    DatabaseManager.getCollection(connectionString + XmldbURI.ROOT_COLLECTION, "admin", "name");
                    DatabaseManager.getCollection(connectionString, "admin", "name");
            // get query-service
            EXistXQueryService service =
                    (EXistXQueryService) col.getService("XQueryService", "1.0");

            // set pretty-printing on
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            CompiledExpression compiled = service.compile(consulta);
            //Guardamos la consulta que queremos en una variable
            long start = System.currentTimeMillis();
            Thread.sleep(2000);
            result = service.execute(compiled);

            long elapsedTimeMillis = System.currentTimeMillis() - start;
            float elapsedTimeSec = elapsedTimeMillis / 1000F;
            tiempos.add(elapsedTimeSec);
        } catch ( Exception e ) {
            e.printStackTrace();
        }


        return tiempos;
    }
}

