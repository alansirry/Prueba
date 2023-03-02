package com.mycompany.mavenlogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JobLogger {
	private static boolean logToFile;
	private static boolean logToConsole;
	private static boolean logMessage;
	private static boolean logWarning;
	private static boolean logError;
	private static boolean logToDatabase;
	private static boolean initialized;
	private static Map dbParams;
	private static Logger logger;

	public JobLogger(boolean logToFileParam, boolean logToConsoleParam, boolean logToDatabaseParam,
			boolean logMessageParam, boolean logWarningParam, boolean logErrorParam, Map<String, String> dbParamsMap) {
		logger = Logger.getLogger("MyLog");  
		logError = logErrorParam;
		logMessage = logMessageParam;
		logWarning = logWarningParam;
		logToDatabase = logToDatabaseParam;
		logToFile = logToFileParam;
		logToConsole = logToConsoleParam;
		dbParams = dbParamsMap;
	}

	public static void LogMessage(String messageText, boolean message, boolean warning, boolean error) throws Exception {
		
		String arch = "logfile " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) +".txt";

		FileHandler fh = new FileHandler("/home/suse/eclipse-workspace/mavenLogger/"+arch, true);
		logger.addHandler(fh);
		SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
		
		
		messageText.trim();
		if (messageText == null || messageText.length() == 0) {
			logger.log(Level.WARNING, "Message Not Found");
			return;
		}
		if (!logToConsole && !logToFile && !logToDatabase) {
			logger.log(Level.WARNING, "Exception:: Logs Off");
			throw new Exception("Invalid configuration");
		}
		if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
			logger.log(Level.SEVERE, "Message must be specified");
			throw new Exception("Error or Warning or Message must be specified");
		}
		
		
		if(logToFile) {
			logger.info(messageText + " LogToFile");
			
			if ((message && logMessage) && (error && logError) && (warning && logWarning)) { 
				if (warning && logWarning) {
					logger.info("Warning:: "  + messageText);
				}
			} else {
				if (message && logMessage) {
					logger.info("Message:: "  + messageText);
				}
	
				if (error && logError && !initialized) {
					logger.info("Error:: "  + messageText);
				}
	
				if (warning && logWarning) {
					logger.info("Warning:: "  + messageText);
				}
			}
		}
		// los mensajes se pueden marcar como mensaje error o warning, en la 
		//configuracion de los boolean definir como se van a dejar
		//si los tres son marcados se definen como WARNING
		
		if(logToConsole) {
			logger.info(messageText + " LogToConsole");
			if ((message && logMessage) && (error && logError) && (warning && logWarning)) { 
				if (warning && logWarning) {
					logger.info("Warning:: "  + messageText);
				}
			} else {
				if (message && logMessage) {
					logger.info("Message:: "  + messageText);
				}
	
				if (error && logError && !initialized) {
					logger.info("Error:: "  + messageText);
				}
	
				if (warning && logWarning) {
					logger.info("Warning:: "  + messageText);
				}
			}
			
		}
	

	}
	
	//public static void logToDataBase(String messageText, String value) {
	public static boolean logToDataBase(String messageText, String value) {
		
		boolean execute = false;
		if(logToDatabase) {
			try {
				Statement stmt = Sesion();
				stmt.executeUpdate("INSERT INTO Log_Values (Date_Time, Log_Message, Log_Value) VALUES(CURRENT_TIMESTAMP, '"+ messageText +"', '" + value + "')");
				logger.info("Insert to Database");
				execute = true;
			}catch (SQLException e) {
				logger.log(Level.SEVERE, "Exception::", e);
			}
		}
		return execute;
	}
	
	public static Statement Sesion() {

		Connection connection = null;
	    //jdbc:postgresql://localhost:5432/database/
		Statement stmt = null;
		try {
			connection = DriverManager.getConnection("jdbc:" + dbParams.get("dbms") + "://" + dbParams.get("serverName")
			 , dbParams.get("userName").toString(),dbParams.get("password").toString());
			stmt = connection.createStatement();
			logger.log(Level.INFO, "Connection to Database");
			initialized = true;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.log(Level.SEVERE, "Exception::", e);
			initialized =  false;
		}
		return stmt;

	}
	//public static void Seguros_Siniestros() {
	public static int Seguros_Siniestros() {
		ResultSet rs = null;
		List<Vehiculo> l = new ArrayList<Vehiculo>();
		String mensaje = "";
		try {
			Statement stmt = Sesion();
			rs = stmt.executeQuery("SELECT id_auto, nombre_auto, modelo, color, precio, seguro_cobertura, descripcion FROM vehiculo LIMIT 10");
			while (rs.next()) {
			  Vehiculo v = new Vehiculo();
			  v.setId_auto(Integer.parseInt(rs.getString(1)));
			  v.setNombre_vehiculo(rs.getString(2));
			  v.setModelo(Integer.parseInt(rs.getString(3)));
			  v.setColor(rs.getString(4));
			  v.setPrecio(Integer.parseInt(rs.getString(5)));
			  v.setSeguro_cobertura(rs.getString(6));
			  v.setDescripcion_siniestro(rs.getString(7));
			  l.add(v);
			}
		}catch (SQLException e) {
			logger.log(Level.SEVERE, "Exception::", e);
		}
		
		logger.info("Cobertura autos");
		
		try {
			for (Vehiculo vehiculo : l) {
				String c = vehiculo.getSeguro_cobertura();
				if (c.isEmpty() || c == "") {
					mensaje = mensaje + "ERROR: Id: " + vehiculo.getId_auto() + " Auto: " + vehiculo.getNombre_vehiculo() +  " Cobertura: VACIO\n";
				}else if (c.toString().contains("GNP") || c.toString().contains("SEGUROA") || c.toString().contains("SEGUROB") ) {
					mensaje = mensaje + "INFO: Id: " + vehiculo.getId_auto() + " Auto: " + vehiculo.getNombre_vehiculo() +  " Cobertura: " + vehiculo.getSeguro_cobertura() + "\n";
				}else {
					mensaje = mensaje + "WARNING: Id: " + vehiculo.getId_auto() + " Auto: " + vehiculo.getNombre_vehiculo() +  " Cobertura: " + vehiculo.getSeguro_cobertura() + "\n";
				}
			}
			logToDataBase(mensaje, "cobertura");
		}catch (Exception e) {
			logger.log(Level.SEVERE, "Exception::", e);
		}
		return l.size();
		
	}
	
}

