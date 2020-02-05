/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.OutputKeys;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import objects.Departament;
import org.exist.xmldb.EXistXQueryService;
import org.w3c.dom.Element;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;


/**
 *
 * @author rvallez
 */
public class DepartamentDAO extends DAO {
    
    //Connexió pel driver exist.xmldb (Podria anar a DAO)
    public static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    public final static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    public final static String COLLECTION = "/db/empresa"; ///empresa.xml
    public final static String USERNAME = "admin";
    public final static String PASSWORD = "";
    
    public DepartamentDAO () throws XQException {
        super();
    }
    
    public ArrayList<Departament> getDepartaments() throws XQException, XMLStreamException {
        
        ArrayList<Departament> depList = new ArrayList();
        
        XQConnection conn = this.getConnection();

        XQPreparedExpression xqpe =
                conn.prepareExpression("for $a in doc(\""+ this.getDbFile() +"\")//dept\n" +
                        "order by $a/nom ascending\n" +
                        "return\n" +
                        "<dept>{\n" +
                        "$a/@codi,\n" +
                        "$a/nom,\n" +
                        "$a/localitat\n" +
                        "}</dept>");

        XQResultSequence rs = xqpe.executeQuery();
        
        while (rs.next()) {
            depList.add(generateDepartament(rs));
        }
        
        this.closeConnection();
        
        return depList;
    }
    
    
    public Departament getDepartamentByName(String nom) throws XQException {
        
        Departament dep = null;
        
        XQConnection conn = getConnection();
        
        XQPreparedExpression xqpe =
                conn.prepareExpression("" +
                    " declare variable $nom as xs:string external; \n" +
                    " for $a in doc(\"/db/empresa/empresa.xml\")//dept[nom = $nom]\n" +
                    " return $a");

        XQItemType xsstring = 
                conn.createAtomicType(XQItemType.XQBASETYPE_STRING);
        xqpe.bindObject(new QName("nom"), nom , xsstring);
        
        XQResultSequence rs = xqpe.executeQuery();
        
        if(rs.next()) {
           dep = generateDepartament(rs);
        }
        
        this.closeConnection();
        
        return dep;
    }
    
    
    
    public Departament generateDepartament(XQResultSequence rs) throws XQException {
        Element e = (Element) rs.getNode();
        
        return new Departament(
                e.getAttribute("codi"),
                e.getElementsByTagName("nom").item(0).getTextContent(),
                e.getElementsByTagName("localitat").item(0).getTextContent()
        );
    }
    
    public void insert(Departament dep) throws XQException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

            String cadena = "update insert " +
            "<dept codi=\""+ dep.getCodi() +"\">" +
            "<nom>"+ dep.getNom()+"</nom>" +
            "<localitat>"+ dep.getLocalitat()+ "</localitat>" +
            "</dept>" +
            " into doc(\""+this.getDbFile()+"\")//departaments";

            this.executeUpdateInsertQuery(cadena);
    }
    
    /**
     * Execució de XQuery que impliquen un UPDATE, DELETE o INSERT
     * (Es podria ficar a la classe DAO)
     * 
     * @param sQuery
     * @throws XMLDBException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    private void executeUpdateInsertQuery(String sQuery) throws XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        
        Class cl = Class.forName(DRIVER);
        Database database = (Database) cl.newInstance();
        DatabaseManager.registerDatabase(database);
        Collection col = DatabaseManager.getCollection(URI + COLLECTION, USERNAME, PASSWORD);
        
        EXistXQueryService service = (EXistXQueryService) col.getService("XQueryService", "1.0");
        
        // set pretty-printing on
        service.setProperty(OutputKeys.INDENT, "yes");
        service.setProperty(OutputKeys.ENCODING, "UTF-8");
        service.query(sQuery);
        
        col.close();
    }
    
}
