/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.dal;

import com.mysql.cj.util.StringUtils;
import dk.dtu.compute.se.pisd.roborally.fileaccess.IOUtil;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Spillets connection med databasen.
 * Her indtastes bla. de nødvendige informationer ved forbindelse til databasen.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */


class Connector {
	private static final String HOST     = "localhost";
	private static final int    PORT     = 3306;
	private static final String DATABASE = "roborally";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "qjf67xsm";

	private static final String DELIMITER = ";;";

	private Connection connection;

	/**
	 * Konstruktøren.
	 * Opretter de nødvendige inrformationer til oprettelse af forbindelsen.
	 */
	Connector() {
        try {
			String url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?user=root";
			connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
			

			createDatabaseSchema();
		} catch (SQLException e) {
			// TODO we should try to diagnose and fix some problems here and
			//      exit in a more graceful way
			e.getSQLState();
			e.printStackTrace();
			System.out.println("1");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Connection error!");
			alert.setHeaderText(null);
			alert.setContentText("There was an error connecting to the database.\nPlease check your settings in connector.java and try again.");

			alert.showAndWait();
			// Platform.exit();
		}
    }

	/**
	 * Opretter forbindelsen til databasen vha. de tidligere angivede informationer.
	 */
	private void createDatabaseSchema() {
    	String createTablesStatement;
    	try {
			ClassLoader classLoader = Connector.class.getClassLoader();
			URI uri = classLoader.getResource("schemas/createschema.sql").toURI();
			byte[] bytes = Files.readAllBytes(Paths.get(uri));
			createTablesStatement = new String(bytes);
		} catch (URISyntaxException | IOException e){
    		e.printStackTrace();
			System.out.println("2");
    		return;
		}

    	try {
    		connection.setAutoCommit(false);
    		Statement statement = connection.createStatement();
    		for (String sql : createTablesStatement.split(DELIMITER)) {
    			if (!StringUtils.isEmptyOrWhitespaceOnly(sql)) {
    				statement.executeUpdate(sql);
    			}
    		}

    		statement.close();
    		connection.commit();
    	} catch (SQLException e) {
    		e.printStackTrace();
    		// TODO error handling
			System.out.printf("3");
    		try {
				connection.rollback();
			} catch (SQLException e1) {
				System.out.println("4");
			}
    	} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				System.out.println("5");
			}
		}
    }

	/**
	 * returnerer spillets connection.
	 * @return connetion
	 */
	Connection getConnection() {
    	return connection; 
    }
    
}
