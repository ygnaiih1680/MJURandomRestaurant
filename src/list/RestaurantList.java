package list;

import global.Constants;
import main.MainFrame;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class RestaurantList extends JDialog {

    public RestaurantList(Frame owner, Statement statement) {
        this.setTitle(Constants.ListDialog.title.getText());
        this.setBounds(Constants.ListDialog.x.getValue(), Constants.ListDialog.y.getValue(),
                Constants.ListDialog.w.getValue(), Constants.ListDialog.h.getValue());
        this.setLocationRelativeTo(owner);
        try {
            ResultSet resultSet = statement.executeQuery("SELECT R_Name, Category FROM FoodMap");
            JTable table = new JTable();
            Vector<String> header = new Vector<>();
            header.add("식당 이름");
            header.add("카테고리");
            DefaultTableModel model = new DefaultTableModel(header, 0) {
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            while (resultSet.next()) {
                Vector<String> temp = new Vector<>();
                temp.add(resultSet.getString("R_Name"));
                temp.add(resultSet.getString("Category"));
                model.addRow(temp);
            }
            table.setModel(model);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            JTableHeader tableHeader = table.getTableHeader();
            tableHeader.getColumnModel().getColumn(0).setMinWidth(300);
            tableHeader.setDefaultRenderer(centerRenderer);
            tableHeader.setReorderingAllowed(false);
            tableHeader.setResizingAllowed(false);
            tableHeader.setFont(MainFrame.extraFont);
            TableColumnModel tableColumnModel = table.getColumnModel();
            for (int i = 0; i < tableColumnModel.getColumnCount(); i++)
                tableColumnModel.getColumn(i).setCellRenderer(centerRenderer);
            table.setFont(MainFrame.extraFont);
            table.setRowHeight(30);

            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.getVerticalScrollBar().setUnitIncrement(30);
            this.getContentPane().add(scrollPane);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
