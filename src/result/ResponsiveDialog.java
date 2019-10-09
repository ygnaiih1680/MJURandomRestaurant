package result;

import global.Constants;
import main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResponsiveDialog {
    private static final String place = "https://store.naver.com/restaurants/detail?id=";
    private static final JLabel name = new JLabel("식당 이름"),
            category = new JLabel("카테고리");
    private static JLabel rname, categoryString;
    private static JButton link;
    private static JDialog dialog = new JDialog();

    static {
        dialog.setBounds(Constants.ResponsiveDialog.x.getValue(), Constants.ResponsiveDialog.y.getValue(),
                Constants.ResponsiveDialog.w.getValue(), Constants.ResponsiveDialog.h.getValue());
        dialog.setTitle(Constants.ResponsiveDialog.title.getText());
        JPanel content = new JPanel();
        Insets insets = new Insets(10, 15, 10, 15);
        content.setLayout(new GridBagLayout());
        content.setBackground(Color.WHITE);
        GridBagConstraints constraints = new GridBagConstraints();
        JButton image = new JButton("카테고리 이미지");
        image.setFont(MainFrame.titleFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = insets;
        content.add(image, constraints);

        constraints = new GridBagConstraints();
        name.setFont(MainFrame.titleFont);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = insets;
        content.add(name, constraints);

        rname = new JLabel();
        rname.setFont(MainFrame.extraFont);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = insets;
        content.add(rname, constraints);

        constraints = new GridBagConstraints();
        category.setFont(MainFrame.titleFont);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = insets;
        content.add(category, constraints);

        constraints = new GridBagConstraints();
        categoryString = new JLabel();
        categoryString.setFont(MainFrame.extraFont);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = insets;
        content.add(categoryString, constraints);

        constraints = new GridBagConstraints();
        link = new JButton();
        link.setFont(MainFrame.extraFont);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = insets;
        constraints.ipady = 20;
        content.add(link, constraints);

        dialog.setContentPane(content);
    }

    public static void showDialog(int number, Statement statement, Container owner) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT R_Name, Category, LinkNum FROM FoodMap WHERE " + number + "=FoodMap.RID");
            if (resultSet.next()) {
                rname.setText(resultSet.getString("R_Name"));
                categoryString.setText(resultSet.getString("Category"));
                int linkn = resultSet.getInt("LinkNum");
                boolean linkable = linkn != -1;
                link.setText(linkable ? "네이버 플레이스" : "No Link!");
                link.setEnabled(linkable);
                link.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(place + linkn));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                });
                dialog.setLocationRelativeTo(owner);
                dialog.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
