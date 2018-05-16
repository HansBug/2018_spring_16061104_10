package shit_like_code.official_gui;

import interfaces.application.ApplicationShitCode;

import javax.swing.*;
import java.awt.*;

class DrawBoard extends JPanel implements ApplicationShitCode {// 绘图板类
    /**
     * @overview:
     */

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        brush.draw(g2D);
    }
}