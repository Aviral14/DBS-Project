package com.DBSProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
    private JScrollPane cKeyScroll;
    private List<Set<String>> fdX;
    private List<Set<String>> fdY;
    Set<String> pKey;
    Set<Set<String>> candidateKey;
    private Set<String> attributes;
    private RoundButton goButton;
    private List<Integer> nFList;
    private JScrollPane scrollPane;
    private JLabel decompositionLabel;

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
        JLabel warning1 = new JLabel("<html>&#9432; &ensp; <b>By default, names of attributes are A, B, C..., but you can change as per your wish</b></html>");
        JLabel warning2 = new JLabel("<html>&#9432; &ensp; <b>Please enter FDs in format: A,B->C,D :ignoring whitespaces, one per line</b></html>");
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
        add(new RectangleShape(100, 450, (frameWidth - 200), 425, Color.WHITE, 0.2f));

        addIOComponents();
    }

    private void addIOComponents() {
        JLabel attributeLabel = new JLabel("▸   Number of attributes: ");
        attributeLabel.setFont(new Font(getName(), Font.BOLD, 20));
        attributeLabel.setForeground(Color.white);
        attributeLabel.setBounds(150, 100, 400, 20);
        goButton = new RoundButton(">", 500, 100, 50, 20, 5, Color.white, blueColor, false);
        goButton.setFont(new Font(getName(), Font.BOLD, 10));
        fDSetLabel = new JLabel("▸   FD Set -");
        fDSetLabel.setFont(new Font(getName(), Font.BOLD, 20));
        fDSetLabel.setForeground(Color.white);
        attrCountField = new RoundTextField(420, 100, 60, 20, 10, Color.RED, Color.green, false);
        attrCountField.setCaretColor(Color.white);
        add(goButton);
        add(attributeLabel);
        add(attrCountField);


        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        textArea.setFont(new Font(getName(), Font.BOLD, 20));
        textArea.setForeground(Color.white);
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setCaretColor(Color.WHITE);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        decompositionLabel = new JLabel();
        decompositionLabel.setFont(new Font(getName(), Font.BOLD, 20));
        decompositionLabel.setForeground(Color.white);
        decompositionLabel.setOpaque(false);
        decompositionLabel.setBackground(new Color(0, 0, 0, 0));
        decompositionLabel.setVerticalAlignment(SwingConstants.TOP);
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(new LineBorder(Color.WHITE, 2, true));
        JScrollPane scrollPaneDecomposition = new JScrollPane(decompositionLabel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneDecomposition.getViewport().setOpaque(false);
        scrollPaneDecomposition.setOpaque(false);
        scrollPaneDecomposition.setBorder(new LineBorder(Color.WHITE, 2, true));
        RoundButton resultButton = new RoundButton("<html>Fetch<br/>Result</html>", 950, 280, 140, 60, 25, Color.WHITE, Color.decode("#3d52e3"), true);
        add(resultButton);
        resultButton.setVisible(false);
        mainFrame.getRootPane().setDefaultButton(goButton);
        SwingUtilities.invokeLater(() -> attrCountField.requestFocus());

        goButton.addActionListener(e -> {
            try {
                int count = Integer.parseInt(attrCountField.getText());
                if (count > 0 && count <= 18) {
                    fDSetLabel.setBounds(150, 175 + 30 * ((count - 1) / 6), 200, 20);
                    textArea.setText("");
                    scrollPane.setBounds(205, 210 + 30 * ((count - 1) / 6), 512, 215 - 30 * ((count - 1) / 6));
                    scrollPane.getVerticalScrollBar().setValue(0);
                    scrollPane.getHorizontalScrollBar().setValue(0);
                    scrollPaneDecomposition.setBounds(200, 605, 512, 260);
                    scrollPaneDecomposition.getVerticalScrollBar().setValue(0);
                    scrollPaneDecomposition.getHorizontalScrollBar().setValue(0);
                    resultLabel.setVisible(false);
                    pKeyLabel.setVisible(false);
                    cKeyLabel.setVisible(false);
                    cKeyScroll.setVisible(false);
                    highestNF.setVisible(false);
                    scrollPaneDecomposition.setVisible(false);
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
                    textArea.requestFocusInWindow();
                } else {
                    setBoundsAndVisiblity(textArea, scrollPaneDecomposition, resultButton);
                    showErrorPopUp("Attribute count must be in range [1, 18] !");
                }
            } catch (NumberFormatException nfe) {
                setBoundsAndVisiblity(textArea, scrollPaneDecomposition, resultButton);
                showErrorPopUp("Please enter a valid 'Number' in range [1, 18] !");
            }
        });

        resultLabel = new JLabel("<html>▸ &ensp; <u>Result →</u></html>");
        resultLabel.setFont(new Font(getName(), Font.BOLD, 20));
        resultLabel.setForeground(Color.GREEN);
        resultLabel.setBounds(150, 460, 200, 25);
        pKeyLabel = new JLabel("Primary key: ");
        pKeyLabel.setFont(new Font(getName(), Font.BOLD, 20));
        pKeyLabel.setForeground(Color.white);
        pKeyLabel.setBounds(200, 495, 700, 25);
        cKeyLabel = new JLabel("Candidate keys: ");
        cKeyLabel.setFont(new Font(getName(), Font.BOLD, 20));
        cKeyLabel.setForeground(Color.white);
        cKeyScroll = new JScrollPane(cKeyLabel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        cKeyScroll.setBounds(200, 530, 700, 35);
        cKeyScroll.getViewport().setOpaque(false);
        cKeyScroll.setOpaque(false);
        cKeyScroll.setBorder(BorderFactory.createEmptyBorder());
        cKeyScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        highestNF = new JLabel("Normal form: ");
        highestNF.setFont(new Font(getName(), Font.BOLD, 20));
        highestNF.setForeground(Color.white);
        highestNF.setBounds(200, 575, 700, 25);
        add(resultLabel);
        add(pKeyLabel);
        add(cKeyScroll);
        add(highestNF);
        add(scrollPaneDecomposition);
        resultLabel.setVisible(false);
        pKeyLabel.setVisible(false);
        cKeyLabel.setVisible(false);
        cKeyScroll.setVisible(false);
        highestNF.setVisible(false);
        scrollPaneDecomposition.setVisible(false);

        resultButton.addActionListener(e -> {
            try {
                checkFDValidity(textArea);
                String[] resultArray = findResult();
                pKeyLabel.setText("Primary key: " + resultArray[0]);
                cKeyLabel.setText("Candidate keys: " + resultArray[1]);
                highestNF.setText("Normal form: " + resultArray[2]);
                decompositionLabel.setText(getDecomposition());
                resultLabel.setVisible(true);
                pKeyLabel.setVisible(true);
                cKeyLabel.setVisible(true);
                cKeyScroll.setVisible(true);
                highestNF.setVisible(true);
                scrollPaneDecomposition.setVisible(true);
            } catch (Exception ex) {
                showErrorPopUp(ex.getMessage());
            }
        });
    }

    private void setBoundsAndVisiblity(JTextArea textArea, JScrollPane scrollPaneDecomposition, RoundButton resultButton) {
        if (attrNameField != null) {
            fDSetLabel.setBounds(0, 0, 0, 0);
            scrollPane.setBounds(0, 0, 0, 0);
            textArea.setBounds(0, 0, 0, 0);
            for (RoundTextField roundTextField : attrNameField) framePanel.remove(roundTextField);
            resultLabel.setVisible(false);
            pKeyLabel.setVisible(false);
            cKeyLabel.setVisible(false);
            cKeyScroll.setVisible(false);
            highestNF.setVisible(false);
            resultButton.setVisible(false);
            scrollPaneDecomposition.setVisible(false);
            framePanel.repaint(150, 125, frameWidth - 300, 315);
        }
    }

    private void checkFDValidity(JTextArea textArea) throws Exception {
        attributes = new HashSet<>();
        for (RoundTextField roundTextField : attrNameField) {
            String temp = roundTextField.getText();
            if (temp.equals(""))
                throw new Exception("Attribute name should not be empty !");
            attributes.add(temp);
        }
        fdX = new ArrayList<>();
        fdY = new ArrayList<>();

        for (String line : textArea.getText().split("\\n")) {
            String array = line.replaceAll(" ", "");
            if (!array.equals("")) {
                String[] properFD = array.split("->");
                if (properFD.length != 2)
                    throw new Exception("FDs are not in proper format !");
                List<String> t1 = Arrays.asList(properFD[0].split(","));
                List<String> t2 = Arrays.asList(properFD[1].split(","));
                if (!t1.containsAll(t2)) {
                    fdX.add(new HashSet<>(t1));
                    fdY.add(new HashSet<>(t2));
                }
            }
        }

        for (int i = 0; i < fdX.size(); i++) {
            for (String s : fdX.get(i)) {
                if (!attributes.contains(s))
                    throw new Exception("FDs are not in proper format!");
            }
            for (String s : fdY.get(i)) {
                if (!attributes.contains(s))
                    throw new Exception("FDs are not in proper format!");
            }
        }
    }

    private String[] findResult() {
        pKey = new HashSet<>();
        candidateKey = findCandidateKeys(attributes);
        removeRedundancy(candidateKey);
        minimalCover();

        for (Set<String> set : candidateKey) {
            if (pKey.size() == 0 || set.size() < pKey.size()) {
                pKey = new HashSet<>(set);
            }
        }

        return new String[]{pKey.toString(), candidateKey.toString(), findNF(candidateKey)};
    }

    private String findNF(Set<Set<String>> candidateKey) {
        nFList = new ArrayList<>();
        int min = 4;
        Set<String> keyAttributes = new HashSet<>();
        for (Set<String> s : candidateKey)
            keyAttributes.addAll(s);

        for (int i = 0; i < fdX.size(); i++) {
            int result = 4;
            Set<String> X = fdX.get(i);
            if (!candidateKey.contains(X)) {
                result = 1;

                // Checking whether X is a part of CKey :
                boolean xFound = false, redundantX = false;
                for (Set<String> set : candidateKey) {
                    if (X.containsAll(set)) {
                        redundantX = true;
                        break;
                    }
                    if (set.containsAll(X)) {
                        xFound = true;
                        break;
                    }
                }
                if (redundantX) {
                    result = 4;
                } else if (keyAttributes.containsAll(fdY.get(i))) {
                    result = 3;
                } else if (!xFound) {
                    result = 2;
                }
                if (result < min) min = result;
            }
            nFList.add(result);
            System.out.println(result);
        }
        if (min == 4)
            return "BCNF";
        else
            return min + "NF";
    }

    private String getDecomposition() {
        ArrayList<ArrayList<String>> decomposition = new ArrayList<>();
        ArrayList<Integer> primaryKeyNo = new ArrayList<>();
        decomposition.add(new ArrayList<>(attributes));
        int currentnf;
        boolean firstExists = true;
        if (highestNF.getText().charAt(13) == 'B') {
            return "Already in BCNF";
        } else {
            currentnf = highestNF.getText().charAt(13) - '0';
        }
        for (int i = 0; i < fdX.size(); i++) {
            Set<String> x = fdX.get(i);
            Set<String> y = fdY.get(i);
            if (nFList.get(i) == currentnf) {

                // Separate lhs and rhs of dependency into separate table
                separateDependencyFromRoot(decomposition, primaryKeyNo, x, y);

            } else {
                // If nf does not need increasing, check for transitive dependency: helps in finding lossless decomposition
                for (int j = 0; j < fdX.size(); j++) {
                    if (i != j) {
                        Set<String> yn = fdY.get(j);
                        if (yn.equals(x)) {
                            separateDependencyFromRoot(decomposition, primaryKeyNo, x, y);
                        }
                    }
                }
            }
        }
        if (decomposition.get(0).size() == 1) {
            decomposition.remove(0);
            firstExists = false;
        }
        return getDecompositionString(decomposition, primaryKeyNo, firstExists);
    }

    private void separateDependencyFromRoot(ArrayList<ArrayList<String>> decomposition, ArrayList<Integer> primaryKeyNo, Set<String> x, Set<String> y) {
        decomposition.add(new ArrayList<>());
        decomposition.get(decomposition.size() - 1).addAll(x);
        decomposition.get(decomposition.size() - 1).addAll(y);
        primaryKeyNo.add(x.size());
        decomposition.get(0).removeAll(y);
    }

    private String getDecompositionString(ArrayList<ArrayList<String>> decomposition, ArrayList<Integer> primaryKeyNo, boolean firstExists) {
        Set<String> primaryKey = pKey;
        if (firstExists && !decomposition.get(0).containsAll(pKey)) {
            for (Set<String> key : candidateKey) {
                if (decomposition.get(0).containsAll(key)) {
                    primaryKey = key;
                    pKeyLabel.setText("Primary key: " + primaryKey.toString());
                    break;
                }
            }
        }
        int n = (Collections.min(nFList));
        String x = (n == 3) ? "BCNF" : ((n + 1) + "NF");
        StringBuilder result = new StringBuilder("<html>Decomposition to " + x + ": <br>");
        int initialI = 0;
        if (firstExists) {
            for (String key : primaryKey) {
                if (!decomposition.get(0).contains(key)) {
                    decomposition.get(0).add(key);
                }
            }
            result.append("R1 (");
            for (String s : decomposition.get(0)) {
                if (primaryKey.contains(s)) {
                    result.append("<u>").append(s).append("</u>, ");
                } else {
                    result.append(s).append(", ");
                }
            }
            result.delete(result.lastIndexOf(","), result.lastIndexOf(" ") + 1);
            result.append(")<br>");
            initialI = 1;
        } else if (primaryKey.size() > 1) {
            decomposition.add(new ArrayList<>(primaryKey));
            primaryKeyNo.add(primaryKey.size());
        }

        // Remove redundant tables
        for (int i = 0, limit = decomposition.size(); i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                if (i != j && decomposition.get(i).containsAll(decomposition.get(j))) {
                    decomposition.remove(j);
                    if (firstExists) {
                        primaryKeyNo.remove(j - 1);
                    } else {
                        primaryKeyNo.remove(j);
                    }
                    if (i >= j) {
                        i--;
                    }
                    j--;
                    limit--;
                }
            }
        }
        for (int i = initialI; i < decomposition.size(); i++) {
            result.append("R").append(i + 1).append(" (");
            ArrayList<String> dec = decomposition.get(i);
            int j = 0;
            for (String e : dec) {
                if (j < primaryKeyNo.get(i - initialI)) {
                    result.append("<u>").append(e).append("</u>, ");

                } else {
                    result.append(e).append(", ");
                }
                j++;

            }
            result.delete(result.lastIndexOf(","), result.lastIndexOf(" ") + 1);
            result.append(")<br>");
        }
        result.append("</html>");
        return result.toString();
    }

    private void minimalCover() {
        for (int i = 0; i < fdX.size(); i++) {
            Set<String> x = fdX.get(i);
            Set<String> y = fdY.get(i);
            difference(y, x);

            for (int j = 0; j < fdX.size(); j++) {
                if (i != j) {
                    Set<String> xn = fdX.get(j);
                    Set<String> yn = fdY.get(j);
                    if (x.equals(xn)) {
                        y.addAll(yn);
                        fdX.remove(j);
                        fdY.remove(j);
                        j--;
                    } else if (x.containsAll(xn) && y.equals(yn)) {
                        fdX.remove(i);
                        fdY.remove(i);
                        j--;
                    } else if (x.containsAll(xn) && y.containsAll(yn)) {
                        y.removeAll(yn);
                    }
                }
            }
        }
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

    private void showErrorPopUp(String msg) {
        JOptionPane.showMessageDialog(null, msg);
        mainFrame.getRootPane().setDefaultButton(goButton);
        attrCountField.requestFocusInWindow();
    }

    private void difference(Set<String> s1, Set<String> s2) {
        for (String s : s2) {
            s1.remove(s);
        }
    }
}
