package shit_like_code.official_gui;


import java.awt.*;

class brush {// 画笔
    public static int[][] colormap = new int[85][85];
    
    public static void draw(Graphics2D g) {
        boolean drawcolor = true;
        int factor = (int) (35 * guigv.percent);
        int width = (int) (20 * guigv.percent);
        int xoffset = -5;
        int yoffset = 3;
        // 检索一遍出租车位置信息，将有出租车的位置标上1
        int[][] taximap = new int[85][85];
        // 获得colormap的引用
        guigv.colormap = colormap;
        // 设置出租车位置
        for (int i = 0; i < 80; i++)
            for (int j = 0; j < 80; j++) {
                taximap[i][j] = -1;
            }
        for (int i = 0; i < guigv.taxilist.size(); i++) {
            guitaxi gt = guigv.taxilist.get(i);
            if (gt.status > -1) {
                // System.out.println("####"+gt.x+" "+gt.y);
                taximap[gt.x][gt.y] = gt.status;
                if (gt.status == 1) {
                    colormap[gt.x][gt.y] = 1;// 路线染色
                }
            }
        }
        // synchronized (guigv.m.taxilist) {
        // for (taxiInfo i : guigv.m.taxilist) {
        // taximap[i.nowPoint.x][i.nowPoint.y] = 1;
        // if (i.state == STATE.WILL || i.state == STATE.RUNNING) {
        // taximap[i.nowPoint.x][i.nowPoint.y] = 2;
        // colormap[i.nowPoint.x][i.nowPoint.y] = 1;// 路线染色
        // }
        // }
        // }
        // ...
        
        for (int i = 0; i < 80; i++) {
            for (int j = 0; j < 80; j++) {
                if (j < 10) {
                    xoffset = -5;
                    yoffset = 3;
                } else {
                    xoffset = -7;
                    yoffset = 3;
                }
                g.setStroke(new BasicStroke(2));
                g.setFont(new Font("Arial", Font.PLAIN, (int) (10 * guigv.percent)));
                if (guigv.m.map[i][j] == 2 || guigv.m.map[i][j] == 3) {
                    if (drawcolor && colormap[i][j] == 1 && colormap[i + 1][j] == 1)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.BLACK);
                    int memj=(int) ((j * factor + guigv.xoffset) * guigv.percent);
                    g.drawLine(memj,
                            (int) ((i * factor + guigv.yoffset) * guigv.percent),
                            memj,
                            (int) (((i + 1) * factor + guigv.yoffset) * guigv.percent));
                    //绘制道路流量
                    if(guigv.drawflow){
                        g.setColor(Color.BLUE);
                        g.drawString(""+guigv.GetFlow(i, j, i+1, j), memj,
                                (int) (((i + 0.5) * factor + guigv.yoffset) * guigv.percent));
                    }
                }
                if (guigv.m.map[i][j] == 1 || guigv.m.map[i][j] == 3) {
                    if (drawcolor && colormap[i][j] == 1 && colormap[i][j + 1] == 1)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.BLACK);
                    int memi=(int) ((i * factor + guigv.yoffset) * guigv.percent);
                    g.drawLine((int) ((j * factor + guigv.xoffset) * guigv.percent),
                            memi,
                            (int) (((j + 1) * factor + guigv.xoffset) * guigv.percent),
                            memi);
                    //绘制道路流量
                    if(guigv.drawflow){
                        g.setColor(Color.BLUE);
                        g.drawString(""+guigv.GetFlow(i, j, i, j+1), (int) (((j + 0.5) * factor + guigv.xoffset) * guigv.percent),
                                memi);
                    }
                }
                int targetWidth;
                if (taximap[i][j] == 3) {
                    g.setColor(Color.GREEN);
                    targetWidth = 2;
                } else if (taximap[i][j] == 2) {
                    g.setColor(Color.RED);
                    targetWidth = 2;
                } else if (taximap[i][j] == 1) {
                    g.setColor(Color.BLUE);
                    targetWidth = 2;
                } else if (taximap[i][j] == 0) {
                    g.setColor(Color.YELLOW);
                    targetWidth = 2;
                } else {
                    g.setColor(Color.BLACK);
                    targetWidth = 1;
                }
                int cleft = (int) ((j * factor - width / 2 + guigv.xoffset) * guigv.percent);
                int ctop = (int) ((i * factor - width / 2 + guigv.yoffset) * guigv.percent);
                int cwidth = (int) (width * guigv.percent) * targetWidth;
                if (targetWidth > 1) {
                    cleft = cleft - (int) (cwidth / 4);
                    ctop = ctop - (int) (cwidth / 4);
                }
                // g.fillOval((int)((j*factor-width/2+guigv.xoffset)*guigv.percent),(int)((i*factor-width/2+guigv.yoffset)*guigv.percent),(int)(width*guigv.percent)*targetWidth,(int)(width*guigv.percent)*targetWidth);
                g.fillOval(cleft, ctop, cwidth, cwidth);// 绘制点
                // 标记srclist中的点
                for (Point p : guigv.srclist) {
                    g.setColor(Color.RED);
                    int x = p.x;
                    int y = p.y;
                    g.drawLine((int) (((y - 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x - 2) * factor + guigv.yoffset) * guigv.percent),
                            (int) (((y + 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x - 2) * factor + guigv.yoffset) * guigv.percent));
                    g.drawLine((int) (((y + 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x - 2) * factor + guigv.yoffset) * guigv.percent),
                            (int) (((y + 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x + 2) * factor + guigv.yoffset) * guigv.percent));
                    g.drawLine((int) (((y + 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x + 2) * factor + guigv.yoffset) * guigv.percent),
                            (int) (((y - 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x + 2) * factor + guigv.yoffset) * guigv.percent));
                    g.drawLine((int) (((y - 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x + 2) * factor + guigv.yoffset) * guigv.percent),
                            (int) (((y - 2) * factor + guigv.xoffset) * guigv.percent),
                            (int) (((x - 2) * factor + guigv.yoffset) * guigv.percent));
                }
                if (guigv.drawstr == true) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.PLAIN, (int) (8 * guigv.percent)));
                    g.drawString("" + i + "," + j, (int) ((j * factor + xoffset + guigv.xoffset) * guigv.percent),
                            (int) ((i * factor + yoffset + guigv.yoffset) * guigv.percent));
                }
            }
        }
    }
}
