/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;
import objects.Departament;
import objects.Empleat;
import org.exist.xmldb.EXistXQueryService;
import org.w3c.dom.Element;
import org.xmldb.api.base.*;
import org.xmldb.api.*;




/**
 *
 * @author rvallez
 */
public class EmpleatDAO extends DAO {
    
    //Connexió pel driver exist.xmldb
    public static final String DRIVER = "org.exist.xmldb.DatabaseImpl";
    public final static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
    public final static String COLLECTION = "/db/empresa"; ///empresa.xml
    public final static String USERNAME = "admin";
    public final static String PASSWORD = "";
    
    
    
    public EmpleatDAO() throws XQException {
        super();
    }

    public Empleat getEmpleatFromCodi(String codi) throws XQException {
        
        Empleat emp = null;
        
        XQConnection conn = getConnection();
        
        XQPreparedExpression xqpe =
                conn.prepareExpression("" +
                    "declare variable $id as xs:string external; \n" +
                    "for $a in doc(\"/db/empresa/empresa.xml\")//emp[@codi = $id]\n" +
                    "return\n" +
                    "<emp>{\n" +
                    "$a/@codi,\n" +
                    "$a/@dept,\n" +
                    "$a/@cap,\n" +
                    "$a/cognom,\n" +
                    "$a/ofici,\n" +
                    "$a/dataAlta,\n" +
                    "$a/salari,\n" +
                    "$a/comissio\n" +
                    "}</emp>");

        XQItemType xsstring = conn.createAtomicType(XQItemType.XQBASETYPE_STRING);
        xqpe.bindObject(new QName("id"), codi , xsstring);
        
        XQResultSequence rs = xqpe.executeQuery();
        
        if(rs.next()) {
           emp = generateEmpleat(rs);
        }
        
        this.closeConnection();
        
        return emp;
    }
    
 
    
    public void updateOficiByCode(String codi, String nouOfici) throws XQException, XMLDBException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        String query = 
            "declare variable $codi := \""+codi+"\";\n" +
            "declare variable $nouofici := \""+nouOfici+"\";\n" + 
            "update value doc(\""+this.getDbFile()+"\")//emp[@codi=$codi]/ofici" +
            " \n" +
            "with $nouofici";
        
        this.executeUpdateInsertQuery(query);
        
    }

    public ArrayList<Empleat> getEmpleatFromSalari(String salari) throws XQException {
        
        ArrayList<Empleat> empleats = new ArrayList();
        XQConnection conn = this.getConnection();
        
        XQPreparedExpression xqpe = conn.prepareExpression(
                "declare variable $salari as xs:string external;"
                + "for $b in doc('/db/empresa/empresa.xml')//empleats/emp\n"
                + "where $b/salari >= $salari\n"
                + "return  <emp>{\n"
                + "    $b/@codi, $b/dept, $b/cap,\n"
                + "    $b/cognom, $b/ofici, $b/dataAlta,\n"
                + "    $b/salari, $b/comissio\n"
                + "    }</emp>");
        
        XQItemType xsstring = conn.createAtomicType(XQItemType.XQBASETYPE_STRING);
        xqpe.bindObject(new QName("salari"), salari , xsstring);

        XQResultSequence rs = xqpe.executeQuery();

        while (rs.next()) {
            empleats.add(generateEmpleat(rs));
        }
        
        this.closeConnection();
        
        return empleats;
    }
    
    
    public void deleteEmpleat(String codi) throws XQException, ClassNotFoundException, InstantiationException, XMLDBException, IllegalAccessException {
        
        String sQuery = "update delete \n" +
                        "      doc(\""+this.getDbFile()+"\")//empleats/emp[@codi=\""+codi+"\"]";

        this.executeUpdateInsertQuery(sQuery);
        
    }
    
    
    public void addEmpleat(Empleat emp) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {

        String sQuery = "update insert "
                + "<emp codi=\""+emp.getCodi()+"\" dept=\""+emp.getDep()+"\" cap=\""+emp.getDep()+"\">"
                + "<cognom>"+emp.getCognom()+"</cognom>"
                + "<ofici>"+emp.getOfici()+"</ofici>"
                + "<dataAlta>"+emp.getDataAlta()+"</dataAlta>"
                + "<salari>"+String.valueOf(emp.getSalari())+"</salari>"
                + ( (emp.getComissio() != 0)? 
                    "<comissio>"+ String.valueOf(emp.getComissio() +"</comissio>"):
                    "")
                + "</emp> into doc(\""+this.getDbFile()+"\")//empleats";

        this.executeUpdateInsertQuery(sQuery);
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
        
        //Podem obtenir el XML com un document tipus Node
//        XMLResource res = (XMLResource) col.getResource("empresa.xml");
//        if (res == null) {
//            System.err.println("could not retrieve document !");
//            return;
//        }
//        
//        Node document = res.getContentAsDOM();
        
        
        EXistXQueryService service = (EXistXQueryService) col.getService("XQueryService", "1.0");
        
        // set pretty-printing on
        service.setProperty(OutputKeys.INDENT, "yes");
        service.setProperty(OutputKeys.ENCODING, "UTF-8");
        ResourceSet rs = service.query(sQuery);
        
        col.close();
    }
    
    private Empleat generateEmpleat(XQResultSequence rs) throws XQException {
        Departament dep;
        Element e = (Element) rs.getNode();
        
        String codi = e.getAttribute("codi");
        String dept = e.getAttribute("dept");
        String cap = e.getAttribute("cap");
        String cognom = e.getElementsByTagName("cognom").item(0).getTextContent();
        String ofici = e.getElementsByTagName("ofici").item(0).getTextContent();
        String dataAlta = ((e.getElementsByTagName("dataAlta").getLength() > 0)? 
                        e.getElementsByTagName("dataAlta").item(0).getTextContent():
                        null);
        int salari = Integer.parseInt(e.getElementsByTagName("salari").item(0).getTextContent());
        //Aquest valor és opcional
        int comissio = ((e.getElementsByTagName("comissio").getLength() > 0)? 
                        Integer.parseInt(e.getElementsByTagName("comissio").item(0).getTextContent()):
                        0);
        
        return new Empleat(codi,
                dept,
                cap,
                cognom,
                ofici,
                dataAlta,
                salari,
                comissio
        );
    }


}

