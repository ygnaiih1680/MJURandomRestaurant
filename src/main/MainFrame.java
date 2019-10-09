package main;

import global.Constants;
import list.RestaurantList;
import result.ResponsiveDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;

public class MainFrame extends JFrame {
    private JCheckBox checkBox;
    private GridBagLayout gridBagLayout;
    private Statement statement;
    private static RestaurantList restaurantList;
    public static Font titleFont = new Font("a아메리카노B", Font.BOLD, 35),
            extraFont = new Font(titleFont.getName(), Font.PLAIN, 20);
    private static int count;

    public MainFrame(Statement statement) throws HeadlessException {
        this.statement = statement;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(Constants.MainFrame.title.getText());
        this.setBounds(Constants.MainFrame.x.getValue(), Constants.MainFrame.y.getValue(),
                Constants.MainFrame.w.getValue(), Constants.MainFrame.h.getValue());
        this.setResizable(false);
        try {
            this.setIconImage(ImageIO.read(new File(Constants.MainFrame.icon.getText())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setElement() {
        restaurantList = new RestaurantList(this, this.statement);

        this.gridBagLayout = new GridBagLayout();
        Container content = this.getContentPane();
        content.setBackground(Color.WHITE);
        content.setLayout(this.gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel title = new JLabel("<html><center>MJU Random<br>Restaurant</center></html>");
        title.setFont(titleFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        content.add(title, constraints);

        constraints = new GridBagConstraints();
        try {
            JButton image = new JButton(new ImageIcon(ImageIO.read(new File("data\\mju.gif")).getScaledInstance(100, 100, Image.SCALE_AREA_AVERAGING)));
            image.setContentAreaFilled(false);
            image.setBorderPainted(false);
            image.setFocusable(false);
            image.setRolloverEnabled(false);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.EAST;
            content.add(image, constraints);
        } catch (IOException e) {
            e.printStackTrace();
        }

        constraints = new GridBagConstraints();
        CategorySelector categorySelector = new CategorySelector();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        content.add(categorySelector, constraints);

        JPanel temp = new JPanel();
        temp.setLayout(gridBagLayout);
        temp.setBackground(Color.WHITE);

        constraints = new GridBagConstraints();
        JLabel subTitle = new JLabel(Constants.MainFrame.subtitle.getText());
        subTitle.setFont(extraFont);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.7;
        constraints.anchor = GridBagConstraints.CENTER;
        temp.add(subTitle, constraints);

        constraints = new GridBagConstraints();
        this.checkBox = new JCheckBox("all");
        this.checkBox.setFont(new Font(extraFont.getName(), extraFont.getStyle(), 25));
        this.checkBox.setBackground(Color.WHITE);
        this.checkBox.addActionListener(e -> categorySelector.selectAll());
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.3;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(0, 0, 0, 15);
        temp.add(this.checkBox, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        content.add(temp, constraints);

        temp = new JPanel();
        temp.setLayout(gridBagLayout);
        temp.setBackground(Color.WHITE);

        constraints = new GridBagConstraints();
        JLabel copyright = new JLabel(Constants.MainFrame.copyright.getText());
        copyright.setFont(new Font(extraFont.getName(), Font.PLAIN, 15));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.insets = new Insets(20, 20, 0, 20);
        constraints.anchor = GridBagConstraints.WEST;
        temp.add(copyright, constraints);

        constraints = new GridBagConstraints();
        JButton feedback = new JButton(Constants.MainFrame.feedback.getText());
        feedback.setFont(new Font(extraFont.getName(), Font.PLAIN, 15));
        feedback.setContentAreaFilled(false);
        feedback.setBorderPainted(false);
        feedback.addActionListener(e -> FeedBack.showDialog());
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = new Insets(20, 20, 0, 20);
        temp.add(feedback, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        content.add(temp, constraints);
    }

    private class CategorySelector extends JPanel implements ActionListener {
        private Vector<JToggleButton> categories;
        private Vector<String> category;

        public CategorySelector() {
            category = new Vector<>();
            Insets st = new Insets(20, 20, 15, 20), nd = new Insets(15, 20, 15, 20);
            this.categories = new Vector<>();
            this.setBackground(Color.WHITE);
            this.setLayout(new GridBagLayout());
            GridBagConstraints constraints;
            for (Constants.CategoryButton category : Constants.CategoryButton.values()) {
                JToggleButton button = new JToggleButton(category.getTitle());
                button.setActionCommand(category.name());
                button.setFont(extraFont);
                button.addActionListener(this);
                int ordinal = category.ordinal();
                constraints = new GridBagConstraints();
                constraints.gridx = ordinal % 2;
                constraints.gridy = ordinal / 2;
                constraints.weightx = 0.5;
                constraints.fill = GridBagConstraints.BOTH;
                constraints.ipady = ordinal / 2 == 2 ? 0 : 20;
                constraints.insets = ordinal / 2 == 0 ? st : nd;
                this.add(button, constraints);
                this.categories.add(button);
            }

            constraints = new GridBagConstraints();
            JButton allList = new JButton(Constants.MainFrame.list.getText());
            allList.setFont(extraFont);
            allList.setBackground(Color.WHITE);
            allList.addActionListener(e -> restaurantList.setVisible(!restaurantList.isVisible()));
            constraints.gridx = 1;
            constraints.gridy = 3;
            constraints.weightx = 0.5;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = nd;
            this.add(allList, constraints);

            constraints = new GridBagConstraints();
            JButton random = new JButton("RANDOM    go->");
            random.setFont(extraFont);
            random.addActionListener(e -> {
                if (this.category.isEmpty()) {
                    checkBox.setSelected(true);
                    selectAll();
                }
                Random randomNum = new Random(System.nanoTime());
                int number = Constants.getNumber(this.category.get(randomNum.nextInt(this.category.size())));
                ResponsiveDialog.showDialog(number, statement, this.getParent());
            });
            constraints.gridx = 0;
            constraints.gridy = 4;
            constraints.gridwidth = 2;
            constraints.weightx = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.insets = new Insets(30, 20, 15, 20);
            this.add(random, constraints);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            JToggleButton button = this.categories.get(Constants.categories.get(command));
            if (button.isSelected()) {
                this.category.add(command);
                boolean selected = true;
                for (JToggleButton button1 : this.categories) {
                    selected &= button1.isSelected();
                }
                checkBox.setSelected(selected);
            } else {
                this.category.remove(command);
                checkBox.setSelected(false);
            }
        }

        void selectAll() {
            boolean check = checkBox.isSelected();
            for (JToggleButton toggleButton : this.categories) {
                toggleButton.setSelected(check);
            }
            if (check)
                for (Constants.CategoryButton categoryButton : Constants.CategoryButton.values())
                    this.category.add(categoryButton.name());
            else this.category.clear();
        }

    }
}