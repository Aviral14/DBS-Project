package com.DBSProject;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static com.DBSProject.CommonConstants.*;

public class Module1 extends BackgroundPanel {
    private JLabel fDSetLabel;
    private RoundTextField attrCountField;
    private RoundTextField[] attrNameField;
    private JLabel resultLabel;
    private JLabel pKeyLabel;
    private JLabel cKeyLabel;
    private JLabel highestNF;
    private List<Set<String>> fdX;
    private List<Set<String>> fdY;
    private RoundButton decompositionButton;
    private Set<String> attributes;

    public Module1() {
        framePanel = this;

        JLabel heading = new JLabel("<html><u>Find Highest Normal Form</u> :</html>");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        heading.setBounds(120, 10, 1100, 80);
        heading.setForeground(Color.WHITE);
        RoundButton back = new RoundButton("←", 10, 10, 60, 25, 10, Color.white, blueColor, false);
        back.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        back.addActionListener(e -> {
            mainFrame.remove(framePanel);
            mainFrame.add(new MainPanel());
            mainFrame.repaint();
        });
        JLabel warning1 = new JLabel("<html>&#9432; &ensp; <b>By default, name of attributes are A, B, C..., but you can change as per your wish</b></html>");
        JLabel warning2 = new JLabel("<html>&#9432; &ensp; <b>Please enter FDs in format: &ensp; A,B->C,D &ensp; :ignore white_spaces, each per line !</b></html>");
        warning1.setForeground(Color.green);
        warning2.setForeground(Color.green);
        warning1.setBounds(120, frameHeight - 75, 1000, 25);
        warning2.setBounds(120, frameHeight - 45, 1000, 25);
        warning1.setFont(new Font(getName(), Font.BOLD, 20));
        warning2.setFont(new Font(getName(), Font.BOLD, 20));
        add(warning1);
        add(warning2);
        add(back);
        add(heading);
        add(new RectangleShape(100, 90, (frameWidth - 200), 350, Color.WHITE, 0.2f));
        add(new RectangleShape(100, 450, (frameWidth - 200), 160, Color.WHITE, 0.2f));

        addIOComponents();
    }

    private void addIOComponents() {
        JLabel attributeLabel = new JLabel("▸   Number of attributes: ");
        attributeLabel.setFont(new Font(getName(), Font.BOLD, 20));
        attributeLabel.setForeground(Color.white);
        attributeLabel.setBounds(150, 100, 400, 20);
        RoundButton goButton = new RoundButton(">", 500, 100, 50, 20, 5, Color.white, blueColor, false);
        goButton.setFont(new Font(getName(), Font.BOLD, 10));
        fDSetLabel = new JLabel("▸   FD Set -");
        fDSetLabel.setFont(new Font(getName(), Font.BOLD, 20));
        fDSetLabel.setForeground(Color.white);
        attrCountField = new RoundTextField(420, 100, 60, 20, 10, Color.RED, Color.green, false);
        add(goButton);
        add(attributeLabel);
        add(attrCountField);


        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setFont(new Font(getName(), Font.BOLD, 20));
        textArea.setForeground(Color.white);
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        RoundButton resultButton = new RoundButton("<html>Fetch<br/>Result</html>", 950, 280, 140, 60, 25, Color.WHITE, Color.decode("#3d52e3"), true);
        add(resultButton);
        resultButton.setVisible(false);

        goButton.addActionListener(e -> {
            try {
                int count = Integer.parseInt(attrCountField.getText());
                if (count <= 18 && count != 0) {
                    fDSetLabel.setBounds(150, 175 + 30 * ((count - 1) / 6), 200, 20);
                    textArea.setBounds(205, 210 + 30 * ((count - 1) / 6), 512, 215 - 30 * ((count - 1) / 6));
                    textArea.setText("");
                    scrollPane.setBounds(205, 210 + 30 * ((count - 1) / 6), 512, 215 - 30 * ((count - 1) / 6));
                    scrollPane.getVerticalScrollBar().setValue(0);
                    scrollPane.getHorizontalScrollBar().setValue(0);
                    resultLabel.setVisible(false);
                    pKeyLabel.setVisible(false);
                    cKeyLabel.setVisible(false);
                    highestNF.setVisible(false);
                    decompositionButton.setVisible(false);
                    resultButton.setVisible(true);
                    if (attrNameField == null) {
                        framePanel.add(fDSetLabel);
                        framePanel.add(scrollPane);
                    }

                    if (attrNameField != null)
                        for (RoundTextField roundTextField : attrNameField) framePanel.remove(roundTextField);
                    attrNameField = new RoundTextField[count];
                    char text = 'A';
                    for (int i = 0; i < count; i++) {
                        attrNameField[i] = new RoundTextField(200 + 134 * (i % 6), 130 + 30 * (i / 6), 120, 25, 10, Color.WHITE, Color.BLACK, false);
                        attrNameField[i].setText(String.valueOf(text++));
                        framePanel.add(attrNameField[i]);
                    }

                    framePanel.repaint(150, 125, frameWidth - 300, 315);
                } else {
                    System.out.println("Number of attributes out of range");
                    // Do if attributes Count > 18
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid argument !");
                // Do something if entered something other than number
                // or Don't even allow to enter number
            }
        });
        
        resultLabel = new JLabel("<html>▸ &ensp; <u>Result →</u></html>");
        resultLabel.setFont(new Font(getName(), Font.BOLD, 20));
        resultLabel.setForeground(Color.GREEN);
        resultLabel.setBounds(150, 460, 200, 25);
        pKeyLabel = new JLabel("PKey");
        pKeyLabel.setFont(new Font(getName(), Font.BOLD, 20));
        pKeyLabel.setForeground(Color.white);
        pKeyLabel.setBounds(200, 495, 800, 25);
        cKeyLabel = new JLabel("CKey");
        cKeyLabel.setFont(new Font(getName(), Font.BOLD, 20));
        cKeyLabel.setForeground(Color.white);
        cKeyLabel.setBounds(200, 530, 800, 25);
        highestNF = new JLabel("NF");
        highestNF.setFont(new Font(getName(), Font.BOLD, 20));
        highestNF.setForeground(Color.white);
        highestNF.setBounds(200, 565, 800, 25);
        decompositionButton = new RoundButton("<html><center>See<br>Decomposition</center></html>\n", 950, 500, 140, 60, 25, Color.WHITE, Color.decode("#3d52e3"), true);
        decompositionButton.setMargin(new Insets(0, -10, 0, 0));
        decompositionButton.setFont(new Font(getName(), Font.BOLD, 15));
        decompositionButton.setHorizontalAlignment(SwingConstants.CENTER);
        add(decompositionButton);
        add(resultLabel);
        add(pKeyLabel);
        add(cKeyLabel);
        add(highestNF);
        resultLabel.setVisible(false);
        pKeyLabel.setVisible(false);
        cKeyLabel.setVisible(false);
        highestNF.setVisible(false);
        decompositionButton.setVisible(false);

        resultButton.addActionListener(e -> {
            if (checkFDValidity()) {
                String[] resultArray = findResult(textArea);
                pKeyLabel.setText("PKey - " + resultArray[0]);
                cKeyLabel.setText("CKey - " + resultArray[1]);
                highestNF.setText("NF - " + resultArray[2]);
                resultLabel.setVisible(true);
                pKeyLabel.setVisible(true);
                cKeyLabel.setVisible(true);
                highestNF.setVisible(true);
                decompositionButton.setVisible(true);
            } else {
                System.out.println("FDs are not valid !");
            }
        });
    }

    private boolean checkFDValidity() {
        return true;
    }

    private String[] findResult(JTextArea textArea) {
        attributes = new HashSet<>();
        for (RoundTextField roundTextField : attrNameField) {
            attributes.add(roundTextField.getText());
        }
        fdX = new ArrayList<>();
        fdY = new ArrayList<>();

        for (String line : textArea.getText().split("\\n")) {
            String array = line.replaceAll(" ", "");
            if (!array.equals("")) {
                String[] properFD = array.split("->");
                fdX.add(new HashSet<>(Arrays.asList(properFD[0].split(","))));
                fdY.add(new HashSet<>(Arrays.asList(properFD[1].split(","))));
            }
        }
        Set<String> pKey = new HashSet<>();
        Set<Set<String>> candidateKey = findCandidateKeys(attributes);
        removeRedundancy(candidateKey);

        for (Set<String> set : candidateKey) {
            if (pKey.size() == 0 || set.size() < pKey.size()) {
                pKey = new HashSet<>(set);
            }
        }

        return new String[]{pKey.toString(), candidateKey.toString(), findNF(candidateKey)};
    }

    private String findNF(Set<Set<String>> candidateKey) {
        int result = 4;
        for (int i = 0; i < fdX.size(); i++) {
            Set<String> entry = fdX.get(i);
            if (!candidateKey.contains(entry)) {
                result = 1;
                boolean xFound = false, yFound = false;

                // Checking whether X or Y part of FD is part of CKey :
                for (Set<String> set : candidateKey) {
                    if (!xFound && set.containsAll(entry)) {
                        xFound = true;
                    }
                    if (!yFound && set.containsAll(fdY.get(i))) {
                        yFound = true;
                    }
                }
                if (!xFound && yFound) {
                    result = 3;
                } else if (!xFound) {
                    result = 2;
                }
            }
        }
        if (result == 4) {
            return "BCNF";
        }
        return result + "NF";
    }

    private Set<Set<String>> findCandidateKeys(Set<String> remainingSet) {
        Set<Set<String>> candidateKey = new HashSet<>();
        if (closureOf(remainingSet).equals(attributes)) {
            candidateKey.add(remainingSet);
            if (remainingSet.size() > 1) {
                Set<String> temp = new HashSet<>(remainingSet);
                for (String s : remainingSet) {
                    temp.remove(s);
                    candidateKey.addAll(findCandidateKeys(new HashSet<>(temp)));
                    temp.add(s);
                }
            }
        }

        return candidateKey;
    }

    private void removeRedundancy(Set<Set<String>> candidateKey) {
        Iterator<Set<String>> iterator = candidateKey.iterator();
        while (iterator.hasNext()) {
            Set<String> set = iterator.next();
            Set<Set<String>> temp = new HashSet<>(candidateKey);
            temp.remove(set);

            for (Set<String> s : temp) {
                if (set.containsAll(s)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private Set<String> closureOf(Set<String> key) {
        Set<String> temp = new HashSet<>(key);
        while (true) {
            boolean added = false;
            for (int i = 0; i < fdX.size(); i++) {
                if (temp.containsAll(fdX.get(i))) {
                    if (temp.addAll(fdY.get(i))) {
                        added = true;
                    }
                }
            }
            if (!added) break;
        }
        return temp;
    }
}
