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

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
class Repository implements IRepository {
	
	private static final String GAME_GAMEID = "gameID";

	private static final String BOARD_NAME = "boardName";

	private static final String GAME_NAME = "name";
	
	private static final String GAME_CURRENTPLAYER = "currentPlayer";

	private static final String GAME_PHASE = "phase";

	private static final String GAME_STEP = "step";
	
	private static final String PLAYER_PLAYERID = "playerID";
	
	private static final String PLAYER_NAME = "name";

	private static final String PLAYER_COLOUR = "colour";
	
	private static final String PLAYER_GAMEID = "gameID";
	
	private static final String PLAYER_POSITION_X = "positionX";

	private static final String PLAYER_POSITION_Y = "positionY";

	private static final String PLAYER_HEADING = "heading";
	private static final String PLAYER_SCORE = "score";
	private static final String PLAYER_HEALTH = "health";
	private static final String CHECKPOINTS_REACHED = "checkpointsReached";
	private Object String;


	private void makeProKort(){
		for (int i = 1; i <= 5; i++) {
			final String PROKORT = "prokort" + i;
		}
	}
	
	
	private Connector connector;
	
	Repository(Connector connector){
		this.connector = connector;
	}

	@Override
	public boolean createGameInDB(Board game) {

		//Egne navne på saves
		TextInputDialog savegamename_dialog = new TextInputDialog();
		savegamename_dialog.setContentText("Enter name for your save");
		Optional<String> savegamename = savegamename_dialog.showAndWait();

		if (game.getGameId() == null || savegamename.isPresent()) {
			Connection connection = connector.getConnection();
			try {
				connection.setAutoCommit(false);

				PreparedStatement ps = getInsertGameStatementRGK();
				ps.setString(1, savegamename.get()); // instead of name
				//ps.setNull(2, Types.TINYINT); // game.getPlayerNumber(game.getCurrentPlayer())); is inserted after players!
				ps.setNull(2, game.getPlayerNumber(game.getCurrentPlayer()));
				ps.setInt(3, game.getPhase().ordinal());
				ps.setInt(4, game.getStep());
				ps.setString(5, game.getBoardName());


				// If you have a foreign key constraint for current players,
				// the check would need to be temporarily disabled, since
				// MySQL does not have a per transaction validation, but
				// validates on a per row basis.
				// Statement statement = connection.createStatement();
				// statement.execute("SET foreign_key_checks = 0");
				
				int affectedRows = ps.executeUpdate();
				ResultSet generatedKeys = ps.getGeneratedKeys();
				if (affectedRows == 1 && generatedKeys.next()) {
					game.setGameId(generatedKeys.getInt(1));
				}
				generatedKeys.close();

				// Enable foreign key constraint check again:
				// statement.execute("SET foreign_key_checks = 1");
				// statement.close();

				createPlayersInDB(game);
				/* TOODO this method needs to be implemented first
				createCardFieldsInDB(game);
				 */

				// since current player is a foreign key, it can oly be
				// inserted after the players are created, since MySQL does
				// not have a per transaction validation, but validates on
				// a per row basis.
				ps = getSelectGameStatementU();
				ps.setInt(1, game.getGameId());

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
					rs.updateRow();
				} else {
					Alert rsalert = new Alert(Alert.AlertType.ERROR);
					rsalert.setTitle("Game creation error!");
					rsalert.setHeaderText(null);
					rsalert.setContentText("There was an error creating the game in the database.");
					rsalert.showAndWait();
				}
				rs.close();

				connection.commit();
				connection.setAutoCommit(true);
				return true;
			} catch (SQLException e) {
				Alert psalert = new Alert(Alert.AlertType.ERROR);
				psalert.setTitle("Game creation error!");
				psalert.setHeaderText(null);
				psalert.setContentText("An error occurred when creating the game in the database.\n\nError message:\n" + e);
				psalert.showAndWait();

				e.printStackTrace();

				try {
					connection.rollback();
					connection.setAutoCommit(true);
				} catch (SQLException e1) {
					Alert rbalert = new Alert(Alert.AlertType.ERROR);
					rbalert.setTitle("Game creation error!");
					rbalert.setHeaderText(null);
					rbalert.setContentText("An error occurred when creating the game in the database.\n\nError message:\n" + e);
					rbalert.showAndWait();

					e1.printStackTrace();
				}
			}
		} else {
			System.err.println("Game cannot be created in DB, since it has a game id already!");
		}
		return false;
	}
		
	@Override
	public boolean updateGameInDB(Board game) {
		assert game.getGameId() != null;
		
		Connection connection = connector.getConnection();
		try {
			connection.setAutoCommit(false);

			PreparedStatement ps = getSelectGameStatementU();
			ps.setInt(1, game.getGameId());
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				rs.updateInt(GAME_CURRENTPLAYER, game.getPlayerNumber(game.getCurrentPlayer()));
				rs.updateInt(GAME_PHASE, game.getPhase().ordinal());
				rs.updateInt(GAME_STEP, game.getStep());
				rs.updateRow();
			} else {
				Alert rsalert = new Alert(Alert.AlertType.ERROR);
				rsalert.setTitle("Update error!");
				rsalert.setHeaderText(null);
				rsalert.setContentText("There was an error updating the game in the database.");
				rsalert.showAndWait();
			}
			rs.close();

			updatePlayersInDB(game);
			/* TOODO this method needs to be implemented first
			updateCardFieldsInDB(game);
			*/

            connection.commit();
            connection.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			Alert psalert = new Alert(Alert.AlertType.ERROR);
			psalert.setTitle("Update error!");
			psalert.setHeaderText(null);
			psalert.setContentText("An error occurred when updating the game in the database.\n\nError message:\n" + e);
			psalert.showAndWait();
			e.printStackTrace();
			
			try {
				connection.rollback();
				connection.setAutoCommit(true);
			} catch (SQLException e1) {
				Alert rbalert = new Alert(Alert.AlertType.ERROR);
				rbalert.setTitle("Update error!");
				rbalert.setHeaderText(null);
				rbalert.setContentText("An error occurred when updating the game in the database.\n\nError message:\n" + e);
				rbalert.showAndWait();
				e1.printStackTrace();
			}
		}

		return false;
	}
	
	@Override
	public Board loadGameFromDB(int id) {
		Board game;
		try {
			PreparedStatement ps = getSelectGameStatementU();
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			int playerNo = -1;
			if (rs.next()) {
				game = LoadBoard.loadBoard(rs.getString(BOARD_NAME));
				if (game == null) {
					return null;
				}
				playerNo = rs.getInt(GAME_CURRENTPLAYER);
				game.setPhase(Phase.values()[rs.getInt(GAME_PHASE)]);
				game.setStep(rs.getInt(GAME_STEP));
			} else {
				Alert loadalert = new Alert(Alert.AlertType.ERROR);
				loadalert.setTitle("Loading error!");
				loadalert.setHeaderText(null);
				loadalert.setContentText("An error occurred when loading the game from the database.");
				loadalert.showAndWait();
				return null;
			}
			rs.close();

			game.setGameId(id);			
			loadPlayersFromDB(game);

			if (playerNo >= 0 && playerNo < game.getPlayersNumber()) {
				game.setCurrentPlayer(game.getPlayer(playerNo));
			} else {
				Alert loadplayeralert = new Alert(Alert.AlertType.ERROR);
				loadplayeralert.setTitle("Loading error!");
				loadplayeralert.setHeaderText(null);
				loadplayeralert.setContentText("An error occurred when loading the saved current player from the database.");
				loadplayeralert.showAndWait();
				return null;
			}

			/* TODO this method needs to be implemented first
			loadCardFieldsFromDB(game);
			*/

			return game;
		} catch (SQLException e) {
			Alert loadalert = new Alert(Alert.AlertType.ERROR);
			loadalert.setTitle("Loading error!");
			loadalert.setHeaderText(null);
			loadalert.setContentText("An error occurred when loading the game from the database.\n\nError message:\n" + e);
			loadalert.showAndWait();

			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<GameInDB> getGames() {
		// TODO when there many games in the DB, fetching all available games
		//      from the DB is a bit extreme; eventually there should a
		//      methods that can filter the returned games in order to
		//      reduce the number of the returned games.
		List<GameInDB> result = new ArrayList<>();
		try {
			PreparedStatement ps = getSelectGameIdsStatement();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(GAME_GAMEID);
				String name = rs.getString(GAME_NAME);
				result.add(new GameInDB(id,name));
			}
			rs.close();
		} catch (SQLException e) {
			Alert rsalert = new Alert(Alert.AlertType.ERROR);
			rsalert.setTitle("Loading error!");
			rsalert.setHeaderText(null);
			rsalert.setContentText("An error occurred when loading the list of games from the database.\n\nError message:\n" + e);
			rsalert.showAndWait();
			e.printStackTrace();
		}
		return result;		
	}

	private void createPlayersInDB(Board game) throws SQLException {
		// TODO code should be more defensive
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());
		
		ResultSet rs = ps.executeQuery();
		for (int i = 0; i < game.getPlayersNumber(); i++) {
			Player player = game.getPlayer(i);
			rs.moveToInsertRow();
			rs.updateInt(PLAYER_GAMEID, game.getGameId());
			rs.updateInt(PLAYER_PLAYERID, i);
			rs.updateString(PLAYER_NAME, player.getName());
			rs.updateString(PLAYER_COLOUR, player.getColor());
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			rs.updateInt(PLAYER_SCORE, player.getScore());
			rs.updateInt(PLAYER_HEALTH, player.getHealth());
			rs.updateInt(CHECKPOINTS_REACHED, 1); //player.checkpointsCompleted.size()


			// Command cards
			for (int j = 1; j <= 8 ; j++) {
				if (player.getCardField(j-1).getCard() != null)
					rs.updateString("comKort" + j, player.getCardField(j - 1).getCard().getName());
			}

			// Programming cards
			for (int j = 1; j <=5 ; j++) {
				if (player.getProgramField(j-1).getCard() != null)
				rs.updateString("prokort"+j, player.getProgramField(j-1).getCard().getName());
			}


			rs.insertRow();
		}

		rs.close();
	}
	
	private void loadPlayersFromDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersASCStatement();
		ps.setInt(1, game.getGameId());
		
		ResultSet rs = ps.executeQuery();
		int i = 0;
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			if (i++ == playerId) {
				// TODO this should be more defensive
				String name = rs.getString(PLAYER_NAME);
				String colour = rs.getString(PLAYER_COLOUR);
				Player player = new Player(game, colour ,name);
				game.addPlayer(player);
				
				int x = rs.getInt(PLAYER_POSITION_X);
				int y = rs.getInt(PLAYER_POSITION_Y);
				player.setSpace(game.getSpace(x,y));
				int heading = rs.getInt(PLAYER_HEADING);
				player.setHeading(Heading.values()[heading]);

				player.setScore(rs.getInt(PLAYER_SCORE));

				player.setHealth(rs.getInt(PLAYER_HEALTH));
				for (int j = 0; j < rs.getInt(CHECKPOINTS_REACHED); j++) {
					player.addCheckpointsCompleted(j+1);
				}


				//commandskort
				for (int j = 1; j <= 8; j++) { //8 = antallet af commandcard felter
					if (rs.getString("comKort"+j) != null) {
						Command[] commands = Command.values();
						int proCardIndex = 0;

						String proCard = rs.getString("comKort" + j);

						for (int k = 1; k <= 6; k++) { //6 = mulige kort
							if (commands[k].displayName.equals(proCard)){
								proCardIndex = k;
							}
						}

						for (int k = 0; k < 8; k++) {
							player.getCardField(j-1).setCard((new CommandCard(commands[proCardIndex])));
						}
					}
				}

				//Programmeringskort
				for (int j = 1; j <= 5; j++) { // 5 = antallet af programkortfelter
					if (rs.getString("proKort"+j) != null) {
						Command[] commands = Command.values();
						int comCardIndex = 0;

						String proCard = rs.getString("proKort" + j);

						for (int k = 1; k <= 6; k++) { //6 = mulige kort
							if (commands[k].displayName.equals(proCard)){
								comCardIndex = k;
							}
						}

						for (int k = 0; k < 5; k++) {
							player.getProgramField(j-1).setCard((new CommandCard(commands[comCardIndex])));
						}
					}
				}


			} else {
				Alert rsalert = new Alert(Alert.AlertType.ERROR);
				rsalert.setTitle("Loading error!");
				rsalert.setHeaderText(null);
				rsalert.setContentText("Game in DB does not have a player with id " + i +"!");
				rsalert.showAndWait();

				System.err.println("Game in DB does not have a player with id " + i +"!");
			}
		}
		rs.close();
	}

	
	private void updatePlayersInDB(Board game) throws SQLException {
		PreparedStatement ps = getSelectPlayersStatementU();
		ps.setInt(1, game.getGameId());
		
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int playerId = rs.getInt(PLAYER_PLAYERID);
			// TODO should be more defensive
			Player player = game.getPlayer(playerId);
			// rs.updateString(PLAYER_NAME, player.getName()); // not needed: player's names does not change
			rs.updateInt(PLAYER_POSITION_X, player.getSpace().x);
			rs.updateInt(PLAYER_POSITION_Y, player.getSpace().y);
			rs.updateInt(PLAYER_HEADING, player.getHeading().ordinal());
			// TODO error handling
			// TODO take care of case when number of players changes, etc
			rs.updateRow();
		}
		rs.close();
		
		// TODO error handling/consistency check: check whether all players were updated
	}

	private static final String SQL_INSERT_GAME =
			"INSERT INTO Game(name, currentPlayer, phase, step, boardName) VALUES (?, ?, ?, ?, ?)";

	private PreparedStatement insert_game_stmt = null;

	private PreparedStatement getInsertGameStatementRGK() {
		if (insert_game_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				insert_game_stmt = connection.prepareStatement(
						SQL_INSERT_GAME,
						Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return insert_game_stmt;
	}

	private static final String SQL_SELECT_GAME =
			"SELECT * FROM Game WHERE gameID = ?";
	
	private PreparedStatement select_game_stmt = null;
	
	private PreparedStatement getSelectGameStatementU() {
		if (select_game_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_game_stmt = connection.prepareStatement(
						SQL_SELECT_GAME,
						ResultSet.TYPE_FORWARD_ONLY,
					    ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_game_stmt;
	}
		
	private static final String SQL_SELECT_PLAYERS =
			"SELECT * FROM Player WHERE gameID = ?";

	private PreparedStatement select_players_stmt = null;

	private PreparedStatement getSelectPlayersStatementU() {
		if (select_players_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_players_stmt = connection.prepareStatement(
						SQL_SELECT_PLAYERS,
						ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_players_stmt;
	}

	private static final String SQL_SELECT_PLAYERS_ASC =
			"SELECT * FROM Player WHERE gameID = ? ORDER BY playerID ASC";
	
	private PreparedStatement select_players_asc_stmt = null;
	
	private PreparedStatement getSelectPlayersASCStatement() {
		if (select_players_asc_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				// This statement does not need to be updatable
				select_players_asc_stmt = connection.prepareStatement(
						SQL_SELECT_PLAYERS_ASC);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_players_asc_stmt;
	}
	
	private static final String SQL_SELECT_GAMES =
			"SELECT gameID, name FROM Game";
	
	private PreparedStatement select_games_stmt = null;
	
	private PreparedStatement getSelectGameIdsStatement() {
		if (select_games_stmt == null) {
			Connection connection = connector.getConnection();
			try {
				select_games_stmt = connection.prepareStatement(
						SQL_SELECT_GAMES);
			} catch (SQLException e) {
				// TODO error handling
				e.printStackTrace();
			}
		}
		return select_games_stmt;
	}



}
