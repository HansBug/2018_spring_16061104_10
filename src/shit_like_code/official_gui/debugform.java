package shit_like_code.official_gui;

import javax.swing.*;

class debugform extends JFrame {
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

