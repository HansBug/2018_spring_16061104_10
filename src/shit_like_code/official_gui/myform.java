package shit_like_code.official_gui;

import interfaces.application.ApplicationShitCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class myform extends JFrame implements ApplicationShitCode {// 我的窗体程序
    /**
     * @overview:
     */

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int left = 100;
    private int top = 100;
    private int width = 630;
    private int height = 600;
    
    public myform() {
        super();
        /* 设置按钮属性 */
        // button1
        JButton button1 = new JButton();// 创建一个按钮
        button1.setBounds(10, 515, 100, 40);// 设置按钮的位置
        button1.setText("重置");// 设置按钮的标题
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guigv.xoffset = 0;
                guigv.yoffset = 0;
                guigv.oldxoffset = 0;
                guigv.oldyoffset = 0;
                guigv.percent = 1.0;
                guigv.drawboard.repaint();
            }
        });
        // button2
        JButton button2 = new JButton();// 创建一个按钮
        button2.setBounds(120, 515, 100, 40);// 设置按钮的位置
        button2.setText("放大");// 设置按钮的标题
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guigv.percent += 0.1;
                guigv.drawboard.repaint();
            }
        });
        // button3
        JButton button3 = new JButton();// 创建一个按钮
        button3.setBounds(230, 515, 100, 40);// 设置按钮的位置
        button3.setText("缩小");// 设置按钮的标题
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (guigv.percent > 0.1)
                    guigv.percent -= 0.1;
                guigv.drawboard.repaint();
            }
        });
        // button4
        JButton button4 = new JButton();
        button4.setBounds(340, 515, 100, 40);
        button4.setText("清除轨迹");
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 清除colormap
                for (int i = 0; i < 85; i++) {
                    for (int j = 0; j < 85; j++) {
                        guigv.colormap[i][j] = 0;
                    }
                }
                guigv.drawboard.repaint();
            }
        });
        /* 设置复选框属性 */
        JCheckBox check1 = new JCheckBox("显示位置");
        check1.setBounds(450, 515, 80, 40);
        check1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guigv.drawstr = check1.isSelected();
                guigv.drawboard.repaint();
            }
        });
        JCheckBox check2 = new JCheckBox("显示流量");
        check2.setBounds(530, 515, 80, 40);
        check2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guigv.drawflow = check2.isSelected();
                guigv.drawboard.repaint();
            }
        });
        /* 设置绘图区属性 */
        DrawBoard drawboard = new DrawBoard();// 创建新的绘图板
        drawboard.setBounds(10, 10, 500, 500);// 设置大小
        drawboard.setBorder(BorderFactory.createLineBorder(Color.black, 1));// 设置边框
        drawboard.setOpaque(true);
        drawboard.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {// 按下鼠标
                guigv.redraw = true;
                guigv.mousex = e.getX();
                guigv.mousey = e.getY();
            }
            
            public void mouseReleased(MouseEvent e) {// 松开鼠标
                guigv.oldxoffset = guigv.xoffset;
                guigv.oldyoffset = guigv.yoffset;
                guigv.redraw = false;
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        drawboard.addMouseMotionListener(new MouseMotionAdapter() {// 添加鼠标拖动
            public void mouseDragged(MouseEvent e) {
                if (guigv.redraw == true) {
                    guigv.xoffset = guigv.oldxoffset + e.getX() - guigv.mousex;
                    guigv.yoffset = guigv.oldyoffset + e.getY() - guigv.mousey;
                    guigv.drawboard.repaint();
                }
            }
        });
        drawboard.addMouseWheelListener(new MouseWheelListener() {// 添加鼠标滚轮
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() == 1) {// 滑轮向前
                    if (guigv.percent > 0.1)
                        guigv.percent -= 0.1;
                    guigv.drawboard.repaint();
                } else if (e.getWheelRotation() == -1) {// 滑轮向后
                    guigv.percent += 0.1;
                    guigv.drawboard.repaint();
                }
            }
        });
        guigv.drawboard = drawboard;// 获得一份drawboard的引用
        
        /* 设置窗体属性 */
        setTitle("实时查看");// 设置窗体标题
        setLayout(null);// 使用绝对布局
        setBounds(left, top, width, height);// 设置窗体位置大小
        
        /* 添加控件，显示窗体 */
        Container c = getContentPane();// 获得一个容器
        c.add(button1);// 添加button1
        c.add(button2);
        c.add(button3);
        c.add(button4);
        c.add(check1);
        c.add(check2);
        c.add(drawboard);
        setVisible(true);// 使窗体可见
        setAlwaysOnTop(true);// 设置窗体置顶
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置默认关闭方式
    }
}

