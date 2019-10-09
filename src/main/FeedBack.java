package main;

import global.Constants;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedBack extends JDialog {
    private static JComboBox<String> field;
    private static JTextArea text;
    private static JTextField email;
    private static JLabel label;
    private static JButton send, cancel;
    private static boolean tEdit = true, eEdit = true;
    private static Fields fields;
    private static final GeneralPath path = new GeneralPath(Path2D.WIND_NON_ZERO);
    private static final Color highlight = new Color(0,32,96);

    private static FeedBack feedBack;

    private enum Fields {
        error("시스템 오류가 발견되었어요!", "불편을 끼쳐드려 죄송합니다 ㅠㅠ\r\n어떤 상황에서 어떤 오류가 발생했는지 적어주세요"),
        disappear("음식점이 사라졌어요..", "사라진 음식점의 이름을 알려주세요"),
        appear("음식점이 새로 생겼어요!", "새로생긴 음식점의 이름과 위치를 알려주세요!"),
        function("이 기능 추가해주세요!", "어떤 기능을 원하세요?"),
        etc("다른 문의가 하고 싶어요!", "하고 싶은 말씀 맘껏 적어주세요!!");

        private String content, placeholder;

        Fields(String text, String placeholder) {
            this.content = text;
            this.placeholder = placeholder;
        }
    }

    static {
        path.moveTo(0, (Constants.FeedbackDialog.h.getValue()>>1)+150);
        path.lineTo((Constants.FeedbackDialog.w.getValue() >> 1) + 20, 40);
        path.lineTo(Constants.FeedbackDialog.w.getValue(), 27);
        path.lineTo(0, 27);
        path.closePath();

        feedBack = new FeedBack();
        feedBack.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        feedBack.setAlwaysOnTop(true);
        Insets basicInsets = new Insets(15, 15, 15, 15);
        Container container = feedBack.getContentPane();
        container.setLayout(new GridBagLayout());
        container.setBackground(highlight);
        feedBack.setBounds(Constants.FeedbackDialog.x.getValue(), Constants.FeedbackDialog.y.getValue(),
                Constants.FeedbackDialog.w.getValue(), Constants.FeedbackDialog.h.getValue());
        feedBack.setTitle(Constants.FeedbackDialog.title.getText());
        feedBack.setResizable(false);
        feedBack.addWindowListener(new WindowAdapter() {

            @Override
            public void windowActivated(WindowEvent e) {
                feedBack.repaint();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                feedBack.setVisible(false);
            }


            @Override
            public void windowClosed(WindowEvent e) {
                text.setText(Fields.error.placeholder);
                email.setText("");
                fields = Fields.error;
                tEdit = eEdit = false;
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        label = new JLabel(Constants.FeedbackDialog.title.getText());
        label.setFont(new Font(MainFrame.titleFont.getName(), MainFrame.titleFont.getStyle(), 25));
        label.setForeground(Color.WHITE);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets = basicInsets;
        container.add(label, constraints);

        constraints = new GridBagConstraints();
        field = new JComboBox<>();
        for (Fields fields : Fields.values()
        ) {
            field.addItem(fields.content);
        }
        field.addItemListener(e -> {
            int index = field.getSelectedIndex();
            text.setText(Fields.values()[index].placeholder);
            fields = Fields.values()[index];
            tEdit = true;
        });
        field.setFont(MainFrame.extraFont.deriveFont(17f));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = basicInsets;
        container.add(field, constraints);

        constraints = new GridBagConstraints();
        text = new JTextArea(Fields.error.placeholder);
        text.setFont(MainFrame.extraFont.deriveFont(13f));
        text.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tEdit = false;
            }
        });
        text.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (tEdit) text.setText("");
            }
        });
        JScrollPane pane = new JScrollPane(text);
        pane.getViewport().setBackground(new Color(255,255,255,70));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.weighty = 0.7;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = basicInsets;
        container.add(pane, constraints);

        constraints = new GridBagConstraints();
        email = new JTextField("FEEDBACK 답변을 위해 E-Mail을 입력해주세요");
        email.setFont(MainFrame.extraFont.deriveFont(13f));
        email.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (eEdit) {
                    email.setText("");
                    email.setBackground(Color.WHITE);
                }
            }
        });
        email.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER)
                    eEdit = false;
            }
        });
        email.addActionListener(e -> {
            if (checkEmail()) {
                email.setBackground(Color.PINK);
                email.setText("이메일을 확인해주세요!");
                eEdit = true;
            } else {
                Thread thread = new Thread(FeedBack::send);
                thread.start();
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 1;
        constraints.weighty = 0.05;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = basicInsets;
        container.add(email, constraints);

        constraints = new GridBagConstraints();
        send = new JButton("전송하기");
        send.addActionListener(e -> {
            if (checkEmail()) {
                email.setBackground(Color.PINK);
                email.setText("이메일을 확인해주세요!");
                eEdit = true;
            } else {
                Thread thread = new Thread(FeedBack::send);
                thread.start();
            }
        });
        send.setFont(field.getFont());
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = new Insets(15, 120, 10, 15);
        constraints.anchor = GridBagConstraints.EAST;
        container.add(send, constraints);

        constraints = new GridBagConstraints();
        cancel = new JButton("취소하기");
        cancel.setFont(field.getFont());
        cancel.addActionListener(e -> feedBack.dispose());
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.insets = basicInsets;
        constraints.anchor = GridBagConstraints.WEST;
        container.add(cancel, constraints);
    }

    private static boolean checkEmail() {
        if (text == null) return false;
        boolean b = Pattern.matches(
                "[\\w~\\-.]+@[\\w~\\-]+(\\.[\\w~\\-]+)+",
                email.getText().trim());
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email.getText());
        return !m.matches() || !b;
    }


    public static void showDialog() {
        feedBack.setVisible(true);
    }

    private static void send() {
        String user = "teamnull404@gmail.com"; // 네이버일 경우 네이버 계정, gmail경우 gmail 계정
        String password = "l@venull18";   // 패스워드

        // SMTP 서버 정보를 설정한다.
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 465);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));

            //수신자메일주소
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("teamnull404@gmail.com"));

            // Subject
            message.setSubject("[" + fields.name() + "] " + System.getProperty("user.name") + "유저가 피드백을 발송했습니다."); //메일 제목을 입력

            // Text
            message.setText(text.getText() + "\r\n답신이메일: " + email.getText());    //메일 내용을 입력

            // send the message
            Transport.send(message); //전송

            message = new MimeMessage(session);

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getText()));

            // Subject
            message.setSubject(System.getProperty("user.name") + "님이 보내주신 피드백이 성공적으로 접수되었습니다."); //메일 제목을 입력

            // Text
            message.setText("소중한 의견 감사드리며, 처리결과를 이 이메일로 알려드리겠습니다!");    //메일 내용을 입력

            // send the message
            Transport.send(message); ////전송


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "예기치 못한 오류로 피드백을 전송하지 못했습니다..");
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setColor(Color.WHITE);
        graphics2D.fill(path);
        field.repaint();
        text.repaint();
    }
}
