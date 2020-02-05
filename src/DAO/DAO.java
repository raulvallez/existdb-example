/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import net.xqj.exist.ExistXQDataSource;

/**
 *
 * @author rvallez
 */
public class DAO {
    
    private static XQConnection conn;
    private static String dbfile = "/db/empresa/empresa.xml";
    
    
    DAO() throws XQException {
    }
    
    DAO(String dbfile) throws XQException {
        DAO.dbfile = dbfile;
    }
    
    protected XQConnection startConnection() throws XQException {
        XQDataSource xqs = new ExistXQDataSource();
        xqs.setProperty("serverName", "localhost");
        xqs.setProperty("port", "8080");
        xqs.setProperty("user", "admin");
        xqs.setProperty("password", "");

        return xqs.getConnection();
    }
    
    
    
    public XQConnection getConnection() throws XQException {
        if(DAO.conn == null || DAO.conn.isClosed()) {
            DAO.conn = startConnection();
        }
        
        return DAO.conn;
    }
    
    public void closeConnection() throws XQException {
        DAO.conn.close();
    }
    
    
    public String getDbFile() {
        return DAO.dbfile;
    }
}
