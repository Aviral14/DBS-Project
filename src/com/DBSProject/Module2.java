package com.DBSProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.*;

import static com.DBSProject.CommonConstants.*;

public class Module2 extends BackgroundPanel {
    private RoundTextField keyField;
    private RoundButton insertButton;
    // private final bfr;
    private final Directory dir;

    public Module2() {
        // Directory init
        int bfr = 3, globalDepth = 1;
        dir = new Directory(globalDepth, bfr);

        // GUI init
        framePanel = this;

        JLabel heading = new JLabel("<html><u>Extendible Hashing Simulation</u></html>");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        heading.setBounds(100, 20, 1100, 80);
        heading.setForeground(Color.WHITE);
        JLabel subHeading = new JLabel("<html>Directory has been created with Global Depth=1</html>");
        subHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        subHeading.setBounds(100, 50, 1100, 80);
        subHeading.setForeground(Color.WHITE);
        RoundButton back = new RoundButton("←", 10, 10, 60, 25, 10, Color.white, blueColor, true);
        back.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        back.addActionListener(e -> {
            mainFrame.remove(framePanel);
            mainFrame.add(new MainPanel());
            mainFrame.repaint();
        });
        add(back);
        add(heading);
        add(subHeading);
        add(new RectangleShape(100, 120, (frameWidth - 200), 140, Color.WHITE, 0.2f));
        add(new RectangleShape(100, 280, (frameWidth - 200), 650, Color.WHITE, 0.2f));

        addIOComponents();
    }

    private void addIOComponents() {
        JLabel bfrLabel = new JLabel("▸   Blocking Factor: ");
        bfrLabel.setFont(new Font(getName(), Font.BOLD, 20));
        bfrLabel.setForeground(Color.white);
        bfrLabel.setBounds(150, 150, 400, 30);
        RoundTextField bfrField = new RoundTextField(380, 160, 60, 20, 10, Color.RED, Color.green, false);
        bfrField.setCaretColor(Color.white);
        RoundButton setBfrButton = new RoundButton("Set BFR", 600, 150, 120, 40, 5, Color.white, blueColor, false);
        setBfrButton.setFont(new Font(getName(), Font.BOLD, 12));
        JLabel keyLabel = new JLabel("▸   Key Value: ");
        keyLabel.setFont(new Font(getName(), Font.BOLD, 20));
        keyLabel.setForeground(Color.white);
        keyLabel.setBounds(150, 200, 400, 30);
        keyField = new RoundTextField(380, 210, 60, 20, 10, Color.RED, Color.green, false);
        keyField.setCaretColor(Color.white);
        insertButton = new RoundButton("Insert Key", 600, 200, 120, 40, 5, Color.white, blueColor, false);
        insertButton.setFont(new Font(getName(), Font.BOLD, 12));
        RoundButton searchButton = new RoundButton("Search key", 750, 200, 120, 40, 5, Color.white, blueColor, false);
        searchButton.setFont(new Font(getName(), Font.BOLD, 12));
        RoundButton resetButton = new RoundButton("Reset", 750, 150, 120, 40, 5, Color.white, blueColor, false);
        resetButton.setFont(new Font(getName(), Font.BOLD, 12));

        add(insertButton);
        add(searchButton);
        add(resetButton);
        add(setBfrButton);
        add(bfrLabel);
        add(bfrField);
        add(keyLabel);
        add(keyField);

        JLabel resultLabel = new JLabel("<html>▸ &ensp; <u>Result →</u></html>");
        resultLabel.setFont(new Font(getName(), Font.BOLD, 20));
        resultLabel.setForeground(Color.GREEN);
        resultLabel.setBounds(150, 340, 200, 25);
        JLabel gDepthLabel = new JLabel("GDepth");
        gDepthLabel.setFont(new Font(getName(), Font.BOLD, 20));
        gDepthLabel.setForeground(Color.white);
        gDepthLabel.setBounds(200, 375, 700, 25);
        JTextArea display = new JTextArea();
        display.setEditable(false);
        display.setBounds(200, 425, 700, 250);
        display.setFont(new Font(getName(), Font.BOLD, 20));
        display.setForeground(Color.white);
        display.setOpaque(false);
        display.setBackground(new Color(0, 0, 0, 0));
        display.setCaretColor(Color.WHITE);
        display.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(new LineBorder(Color.WHITE, 2, true));

        add(resultLabel);
        add(display);
        add(gDepthLabel);
        add(scrollPane);
        resultLabel.setVisible(false);
        gDepthLabel.setVisible(false);
        display.setVisible(false);

        insertButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(keyField.getText());
                try {
                    String msg = dir.insert(value, false);
                    showMsgPopUp(msg);
                } catch (InputMismatchException err) {
                    showMsgPopUp("Please enter a valid integer!");
                }
            } catch (NumberFormatException er) {
                showMsgPopUp("Please enter a valid integer!");
            }
            gDepthLabel.setText("Global Depth - " + dir.globalDepth);
            String ans = getResult();
            scrollPane.setViewportView(display);
            scrollPane.getPreferredSize();
            scrollPane.setBounds(200, 425, 700, 250);
            display.setText(ans);
            resultLabel.setVisible(true);
            gDepthLabel.setVisible(true);
            display.setVisible(true);
            scrollPane.setVisible(true);
        });

        searchButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(keyField.getText());
                try {
                    String msg = dir.search(value);
                    showMsgPopUp(msg);
                } catch (InputMismatchException err) {
                    showMsgPopUp("Please enter a valid integer!");
                }
            } catch (NumberFormatException er) {
                showMsgPopUp("Please enter a valid integer!");
            }
        });

        resetButton.addActionListener(e -> {
            mainFrame.remove(framePanel);
            mainFrame.add(new Module2());
            mainFrame.repaint();
        });
    }

    private void showMsgPopUp(String msg) {
        JOptionPane.showMessageDialog(null, msg);
        mainFrame.getRootPane().setDefaultButton(insertButton);
        keyField.requestFocusInWindow();
    }

    // duplicates will be used to incase the bucket is not split and we then want to
    // print it as different or not
    private String getResult() {
        String str;
        StringBuilder res;
        // this set is used to mark if a particular value has been printed before
        // this will have no significance if we are printing duplicates
        HashSet<String> displayed = new HashSet<>();
        res = new StringBuilder();
        for (int i = 0; i < dir.buckets.size(); ++i) {
            str = dir.bucketID(i);
            if (!displayed.contains(str)) {
                displayed.add(str);
                res.append(str).append(" => ");
                for (Integer integer : dir.buckets.get(i).values) {
                    res.append(integer).append(" ");
                }
                res.append("\n");
            }
        }
        return res.toString();
    }
}

class Directory {
    int globalDepth, bfr;
    ArrayList<BUCKET> buckets = new ArrayList<>(0); // buckets will be storing individual buckets of the directory

    Directory(int globalDepth, int bfr) {
        this.globalDepth = globalDepth;
        this.bfr = bfr;

        // initially we are creating only 2 buckets corresponding to 0 and 1
        for (int i = 0; i < (1 << globalDepth); ++i) {
            buckets.add(new BUCKET(globalDepth, bfr));
        }
    }

    int pairIndex(int bucketNumber, int depth) {
        return (bucketNumber ^ (1 << (depth - 1)));
    }

    void expandDirectory() {
        // this will double the buckets
        for (int i = 0; i < (1 << globalDepth); ++i) {
            buckets.add(buckets.get(i));
        }
        ++globalDepth;
    }

    void splitOverflown(int bucketNumber) {
        int localDepth, pairIndex, index_diff, dir_size, i;
        HashSet<Integer> temp;

        localDepth = buckets.get(bucketNumber).increaseDepth();

        // Directory needs to be expanded if the local depth is exceeding global depth
        if (localDepth > globalDepth) {
            expandDirectory();
        }
        pairIndex = pairIndex(bucketNumber, localDepth);
        buckets.set(pairIndex, new BUCKET(localDepth, bfr));
        temp = buckets.get(bucketNumber).copy();
        buckets.get(bucketNumber).clear();
        index_diff = 1 << localDepth;
        dir_size = 1 << globalDepth;
        for (i = pairIndex - index_diff; i >= 0; i -= index_diff) {
            buckets.set(i, buckets.get(pairIndex));
        }
        for (i = pairIndex + index_diff; i < dir_size; i += index_diff) {
            buckets.set(i, buckets.get(pairIndex));
        }
        for (Integer integer : temp) {
            insert(integer, true);
        }
    }

    // In search function firstly the bucket where key needs to be inserted is found
    // and then we check that bucket
    String search(int key) {
        int bucketNumber = hashFunction(key);
        return buckets.get(bucketNumber).search(key, bucketID(bucketNumber));
    }

    int hashFunction(int n) {
        return (n & ((1 << globalDepth) - 1));
    }

    // bucketID is a function which will be converting number to binary string
    String bucketID(int num) {
        int d;
        StringBuilder str;
        d = buckets.get(num).getDepth();
        str = new StringBuilder();
        while (num > 0 && d > 0) {
            str.insert(0, (num % 2 == 0 ? "0" : "1"));
            num /= 2;
            d--;
        }
        while (d > 0) {
            str.insert(0, "0");
            d--;
        }
        return str.toString();
    }

    String insert(int key, boolean reinserted) {
        String msg;
        int bucketNumber = hashFunction(key);
        int status = buckets.get(bucketNumber).insert(key);
        if (status == 1) {
            if (!reinserted) {
                msg = "Inserted " + key + " in bucket " + bucketID(bucketNumber);
            } else {
                msg = "Moved " + key + " to bucket " + bucketID(bucketNumber);
            }
        } else if (status == 0) {
            splitOverflown(bucketNumber);
            msg = insert(key, reinserted);
        } else {
            msg = "Key " + key + " already exists in bucket " + bucketID(bucketNumber);
        }
        return msg;
    }
}

class BUCKET {
    int depth, size;
    HashSet<Integer> values = new HashSet<>();

    BUCKET(int depth, int size) {
        this.depth = depth;
        this.size = size;
    }

    // this function will be used to get a status whether the key is already there /
    // bucket has to be split /add key
    int insert(int key) {
        if (values.contains(key)) {
            return -1;
        }
        if (isFull()) {
            return 0;
        }
        values.add(key);
        return 1;
    }

    HashSet<Integer> copy() {
        return new HashSet<>(values);
    }

    // To check if a particular bucket is full or not
    boolean isFull() {
        return values.size() == size;
    }

    // get depth of that particular bucket
    int getDepth() {
        return depth;
    }

    // increase depth while splitting
    int increaseDepth() {
        ++depth;
        return depth;
    }

    // clear that bucket
    void clear() {
        values.clear();
    }

    // searching in that bucket
    String search(int key, String bucket) {
        String msg;
        if (values.contains(key)) {
            msg = "Key:" + key + " exists in bucket " + bucket;
        } else {
            msg = "This key does not exist";
        }
        return msg;
    }
}
