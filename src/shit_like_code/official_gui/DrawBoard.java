package shit_like_code.official_gui;

import javax.swing.*;
import java.awt.*;

class DrawBoard extends JPanel {// 绘图板类
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