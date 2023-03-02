/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mycompany.mavenlogger.JobLogger;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author suse
 */
public class NewJUnitTest {
    
    public NewJUnitTest() {
    	 Map <String, String> hm = new HashMap<String, String>();
         hm.put("userName", "postgres");
         hm.put("password", "postgres");
         hm.put("dbms", "postgresql");
         hm.put("portNumber", "5432");
         hm.put("serverName", "localhost/databaseFull");
      
         JobLogger jb = new JobLogger(true, true, true, true, true, true, hm);
         //JobLogger.LogMessage("Init parametros", true, true, true);
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void TestLogFile() throws Exception {
    	JobLogger.LogMessage("Init parametros: conexión Database", true, true, true);
    	
		File f = new File("/home/suse/eclipse-workspace/mavenLogger/logfile 2 de marzo de 2023.txt");
		if(f.exists() && !f.isDirectory()) { 
			assertEquals(f.exists() , true);
		}
    	
    }
    
    @Test
    public void TestLogToDatabase() {
    	boolean b = JobLogger.logToDataBase("Mensaje personalizado a base de datos", "WARNING");
    	assertEquals(b , true);
    }
    
    @Test
    public void TestLogQueryDatabaseSeguros() throws SQLException {
    	int n = JobLogger.Seguros_Siniestros();
    	assertEquals(n > 0 , true);
    }
}
