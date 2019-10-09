package main;

import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection lib = DriverManager.getConnection("jdbc:derby:lib");
            Statement table = lib.createStatement();
            MainFrame mainFrame = new MainFrame(table);
            mainFrame.setElement();
            mainFrame.setVisible(true);
        } catch (SQLException | ClassNotFoundException | UnsupportedLookAndFeelException | ParseException e) {
            e.printStackTrace();
        }
    }
}
