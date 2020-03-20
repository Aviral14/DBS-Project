package com.DBSProject;

public class Main {

    public static void main(String[] args) {
	    // Create and display GUI from event dispatching thread (enhances thread safety)
        javax.swing.SwingUtilities.invokeLater(MainFrame::new);
    }
}
