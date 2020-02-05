/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empleats;

import DAO.DepartamentDAO;
import DAO.EmpleatDAO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import javax.xml.xquery.XQException;
import objects.Departament;
import objects.Empleat;
import org.xmldb.api.base.XMLDBException;

/**
 *
 * @author rvallez
 */
public class EmpleatsRun {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws XQException, XMLStreamException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException, IOException {
        // TODO code application logic herepublic static void main(String[] args) throws XQException
        
        Empleat emp = null;
        Departament dep = null;
        String line;
        
        DepartamentDAO depDAO = new DepartamentDAO();
        EmpleatDAO empDAO = new EmpleatDAO();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean end = false;
        
        System.out.println("enter a string (? for help)");
        while (!end) {
            System.out.print("> ");
            line = br.readLine();
            if (line == null) {
                continue;
            }
            
            String[] parts = line.split(" ");            
            if (parts.length == 0) {
                continue;
            }
            
            switch (parts[0]) {
                case "?":
                    System.out.println("deplist|depfind [nom]|adddep|empfind [codi]"
                            + "|empsalari [value]|modofici [empid] [nouofici]|addemp|empdel");
                    break;
                case "deplist":
                    ArrayList<Departament> depList = depDAO.getDepartaments();
        
                    if(depList.isEmpty()) {
                       System.out.println("No existeix cap departament");
                    } 
                    
                    for (Departament departament : depList) { 		      
                       System.out.println(departament); 		
                    } 
                    break;
                case "depfind":
                    if(parts.length <= 1) {
                        System.out.println("Falta nom del departament: depfind [nom]");
                        break;
                    }
                    
                    String nomDep=parts[1];
                    dep = depDAO.getDepartamentByName(nomDep);
                    System.out.println(dep);
                    break;
                case "adddep":
                    dep = menuGetDepartament();
                    System.out.println(dep);
                    depDAO.insert(dep);
                    depDAO.getDepartamentByName(dep.getNom());
                    break;
                case "empfind":
                    if(parts.length <= 1) {
                        System.out.println("Falta nom del departament: empfind [codi]");
                        break;
                    }
                    String codi = parts[1];
                    emp = empDAO.getEmpleatFromCodi(codi);
        
                    if(emp == null) {
                       System.out.println("No existeix cap empleat amb el codi");
                    } else {
                        System.out.println(emp);
                    }
                    break;
                case "empsalari":
                    if(parts.length <= 1) {
                        System.out.println("Falta el salari: empsalari [salari]");
                        break;
                    }
                    String salari = parts[1];
                    ArrayList<Empleat> empList = empDAO.getEmpleatFromSalari(salari);
        
                    if(empList.isEmpty()) {
                       System.out.println("No existeix cap empleat amb el codi");
                    } 
                    
                    for (Empleat empsalari : empList) { 		      
                       System.out.println(empsalari); 		
                    } 
                    break;
                case "modofici":
                    String codiemp=parts[1];
                    String ofici=parts[2];
                    empDAO.updateOficiByCode(codiemp, ofici);
                    empDAO.getEmpleatFromCodi(codiemp);
                    break;
                case "empdel":
                    String codidel=parts[1];
                    empDAO.deleteEmpleat(codidel);
                    String cod=parts[1];
                    break;
                case "addemp":
                case "empadd":
                    emp = menuGetEmpleat();
                    empDAO.addEmpleat(emp);
                    Empleat empBD = empDAO.getEmpleatFromCodi(emp.getCodi());
                    System.out.println(empBD);
                    break;
                case "exit":
                    end = true;
                    break;
                default:
                    System.out.println("No he entés la comanda...torna a ");
                    break;
            }
        }//end while

    }   

    private static Empleat menuGetEmpleat() throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Empleat emp = new Empleat();
        
        System.out.println("codi:");
        System.out.print("> ");
        String line = br.readLine();
        emp.setCodi(line);
        
        System.out.println("codi departament:");
        System.out.print("> ");
        line = br.readLine();
        emp.setDep(line);
        
        System.out.println("codi cap:");
        System.out.print("> ");
        line = br.readLine();
        emp.setCap(line);
        
        System.out.println("cognom:");
        System.out.print("> ");
        line = br.readLine();
        emp.setCognom(line);
        
        System.out.println("ofici:");
        System.out.print("> ");
        line = br.readLine();
        emp.setOfici(line);
        
        System.out.println("data alta");
        System.out.print("> ");
        line = br.readLine();
        emp.setDataAlta(line);
        
        System.out.println("salari (número)");
        System.out.print("> ");
        line = br.readLine();
        emp.setSalari(Integer.parseInt(line));
        
        System.out.println("comissio (número)");
        System.out.print("> ");
        line = br.readLine();
        emp.setComissio(Integer.parseInt(line));
        
        return emp;
    }
    
    private static Departament menuGetDepartament() throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Departament dep = new Departament();
        
        System.out.println("codi departament:");
        System.out.print("> ");
        String codi = br.readLine();
        dep.setCodi(codi);
        
        System.out.println("nom:");
        System.out.print("> ");
        String nom = br.readLine();
        dep.setNom(nom);
        
        System.out.println("localitat:");
        System.out.print("> ");
        String localitat = br.readLine();
        dep.setLocalitat(localitat);
       
        return dep;
    }
}
