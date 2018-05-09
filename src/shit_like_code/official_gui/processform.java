package shit_like_code.official_gui;


import javax.swing.*;
import java.awt.*;

class processform extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JProgressBar progressBar = new JProgressBar();
    JLabel label1 = new JLabel("BFS进度", SwingConstants.CENTER);
    
    public processform() {
        super();
        // 将进度条设置在窗体最北面
        getContentPane().add(progressBar, BorderLayout.NORTH);
        getContentPane().add(label1, BorderLayout.CENTER);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        // 设置窗体各种属性方法
        setBounds(100, 100, 100, 100);
        setAlwaysOnTop(true);// 设置窗体置顶
        setVisible(true);
    }
}

