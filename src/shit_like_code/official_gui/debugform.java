package shit_like_code.official_gui;

import interfaces.application.ApplicationShitCode;

import javax.swing.*;

class debugform extends JFrame implements ApplicationShitCode {
    /**
     * @overview:
     */

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JTextArea text1 = new JTextArea();
    
    public debugform() {
        super();
        getContentPane().add(text1);
        setBounds(0, 100, 500, 100);
        setAlwaysOnTop(true);
        setVisible(true);
    }
}

