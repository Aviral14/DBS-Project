package com.DBSProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.*;

import static com.DBSProject.CommonConstants.*;

public class Module2 extends BackgroundPanel {
    private JLabel gDepthLabel;
    private JTextArea resultArea;
    private RoundTextField keyField;
    private RoundButton insertButton;
    private RoundButton searchButton;
    private RoundButton displayButton;
    private JScrollPane scrollPane;
    private Directory dir;

    public Module2() {
        framePanel = this;

        JLabel heading = new JLabel("<html><u>Extendible Hashing Simulation</u></html>");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 25));
        heading.setBounds(100, 20, 1100, 80);
        heading.setForeground(Color.WHITE);
        RoundButton back = new RoundButton("←", 10, 10, 50, 25, 10, Color.white, blueColor, true);
        back.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        back.addActionListener(e -> {
            mainFrame.remove(framePanel);
            mainFrame.add(new MainPanel());
            mainFrame.repaint();
        });
        add(back);
        add(heading);
        add(new RectangleShape(100, 90, (frameWidth - 200), 350, Color.WHITE, 0.2f));
        add(new RectangleShape(100, 450, (frameWidth - 200), 425, Color.WHITE, 0.2f));

        addIOComponents();

        int bfr = 3, globalDepth = 1;
        dir = new Directory(globalDepth, bfr);
        System.out.println("Directory has been created with global depth=1 and bfr=3");
    }

    private void addIOComponents() {
        JLabel keyLabel = new JLabel("▸   Key Value: ");
        keyLabel.setFont(new Font(getName(), Font.BOLD, 20));
        keyLabel.setForeground(Color.white);
        keyLabel.setBounds(150, 100, 400, 20);
        insertButton = new RoundButton("Insert Key", 500, 100, 100, 40, 5, Color.white, blueColor, false);
        insertButton.setFont(new Font(getName(), Font.BOLD, 10));
        searchButton = new RoundButton("Search key", 600, 100, 100, 40, 5, Color.white, blueColor, false);
        searchButton.setFont(new Font(getName(), Font.BOLD, 10));
        keyField = new RoundTextField(420, 100, 60, 20, 10, Color.RED, Color.green, false);
        keyField.setCaretColor(Color.white);
        add(insertButton);
        add(searchButton);
        add(keyLabel);
        add(keyField);

        RoundButton resultButton = new RoundButton("<html>Fetch<br/>Result</html>", 950, 280, 140, 60, 25, Color.WHITE,
                Color.decode("#3d52e3"), true);
        add(resultButton);
        JLabel resultLabel = new JLabel("<html>▸ &ensp; <u>Result →</u></html>");
        resultLabel.setFont(new Font(getName(), Font.BOLD, 20));
        resultLabel.setForeground(Color.GREEN);
        resultLabel.setBounds(150, 460, 200, 25);
        JLabel gDepthLabel = new JLabel("GDepth");
        gDepthLabel.setFont(new Font(getName(), Font.BOLD, 20));
        gDepthLabel.setForeground(Color.white);
        gDepthLabel.setBounds(200, 495, 700, 25);
        resultArea = new JTextArea();
        ;
        JTextArea display = new JTextArea(16, 58);
        display.setEditable(false); // set textArea non-editable
        display.setBounds(200, 545, 700, 150);
        display.setFont(new Font(getName(), Font.BOLD, 20));
        display.setForeground(Color.white);
        display.setOpaque(false);
        display.setBackground(new Color(0, 0, 0, 0));
        display.setCaretColor(Color.WHITE);
        display.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(new LineBorder(Color.WHITE, 2, true));
        add(resultLabel);
        add(display);
        add(scrollPane);
        add(gDepthLabel);
        add(resultArea);
        resultLabel.setVisible(false);
        gDepthLabel.setVisible(false);
        resultArea.setVisible(false);
        display.setVisible(false);
        resultButton.addActionListener(e -> {
            gDepthLabel.setText("Global Depth - " + dir.globalDepth);
            String ans = getResult(false);
            display.setText(ans);
            resultLabel.setVisible(true);
            gDepthLabel.setVisible(true);
            display.setVisible(true);
        });

        insertButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(keyField.getText());
                try {
                    String msg = dir.insert(value, false);
                    showErrorPopUp(msg);
                } catch (InputMismatchException err) {
                    showErrorPopUp("Please enter a valid integer!");
                }
            } catch (NumberFormatException er) {
                showErrorPopUp("Please enter a valid integer!");
            }
        });

        searchButton.addActionListener(e -> {
            try {
                int value = Integer.parseInt(keyField.getText());
                try {
                    String msg = dir.search(value);
                    showErrorPopUp(msg);
                } catch (InputMismatchException err) {
                    showErrorPopUp("Please enter a valid integer!");
                }
            } catch (NumberFormatException er) {
                showErrorPopUp("Please enter a valid integer!");
            }
        });
    }

    private void showErrorPopUp(String msg) {
        JOptionPane.showMessageDialog(null, msg);
        mainFrame.getRootPane().setDefaultButton(insertButton);
        keyField.requestFocusInWindow();
    }

    // duplicates will be used to incase the bucket is not split and we then want to
    // print it as different or not
    private String getResult(boolean duplicates) {
        String str, res;
        // this set is used to mark if a particular value has been printed before
        // this will have no significance if we are printing duplicates
        HashSet<String> displayed = new HashSet<String>();
        res = "";
        for (int i = 0; i < dir.buckets.size(); ++i) {
            int extra = dir.buckets.get(i).getDepth();
            str = dir.bucketID(i);
            if (duplicates || !displayed.contains(str)) {
                displayed.add(str);
                res += str + " => ";
                Iterator<Integer> it = dir.buckets.get(i).values.iterator();
                while (it.hasNext()) {
                    res += it.next() + " ";
                }
                res += "\n";
            }
        }
        return res;
    }
}

class Directory {
    int globalDepth, bfr;
    ArrayList<BUCKET> buckets = new ArrayList<BUCKET>(0); // buckets will be storing individual buckets of the directory

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
        HashSet<Integer> temp = new HashSet<Integer>();

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
        Iterator<Integer> it = temp.iterator();
        while (it.hasNext()) {
            insert(it.next(), true);
        }
    }

    // In search function firstly the bucket where key needs to be inserted is found
    // and then we check that bucket
    String search(int key) {
        int bucketNumber = hashFunction(key);
        System.out.println("Searching key " + key + " in bucket " + bucketID(bucketNumber));
        return buckets.get(bucketNumber).search(key);
    }

    int hashFunction(int n) {
        return (n & ((1 << globalDepth) - 1));
    }

    // bucketID is a function which will be converting number to binary string
    String bucketID(int num) {
        int d;
        String str;
        d = buckets.get(num).getDepth();
        str = "";
        while (num > 0 && d > 0) {
            str = (num % 2 == 0 ? "0" : "1") + str;
            num /= 2;
            d--;
        }
        while (d > 0) {
            str = "0" + str;
            d--;
        }
        return str;
    }

    String insert(int key, boolean reinserted) {
        String msg = null;
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
    HashSet<Integer> values = new HashSet<Integer>();

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
        HashSet<Integer> temp = new HashSet<Integer>();
        Iterator<Integer> it = values.iterator();
        while (it.hasNext()) {
            temp.add(it.next());
        }
        return temp;
    }

    // To check if a particular bucket is full or not
    boolean isFull() {
        if (values.size() == size) {
            return true;
        } else {
            return false;
        }
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
    String search(int key) {
        String msg = null;
        if (values.contains(key)) {
            msg = "Key exists in a bucket";
        } else {
            msg = "This key does not exists";
        }
        return msg;
    }
}
