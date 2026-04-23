/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author smth
 */

class Connect{
    public static Connection getConnection(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbUrl= "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=TetrisDB;user=sa;password=sa2025;encrypt=true;trustServerCertificate=true;";
            return DriverManager.getConnection(dbUrl);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
public class DatabaseManager {
    //obj for save a score record to the database
    public static void saveScore(String playerName, int score, int level, int lines) {
        String sql = "INSERT INTO Scores (player, score, level, lines) VALUES (?, ?, ?, ?)";
        try (Connection con = Connect.getConnection()) {
            if (con == null) return;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, playerName);
                ps.setInt(2, score);
                ps.setInt(3, level);
                ps.setInt(4, lines);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();        
        }
    }
    //obj for get top 10 leaderboard from DB
    public static List<String[]> getLeaderboard() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT TOP 10 player, score, level, lines " +
                     "FROM Scores ORDER BY score DESC";
        try (Connection con = Connect.getConnection()) {
            if (con == null) return list;
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    list.add(new String[]{
                        rs.getString("player"),
                        String.valueOf(rs.getInt("score")),
                        String.valueOf(rs.getInt("level")),
                        String.valueOf(rs.getInt("lines"))
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
