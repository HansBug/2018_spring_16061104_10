package shit_like_code.official_gui;

class node {// 结点信息
    int NO;
    int depth;
    
    public node(int _NO, int _depth) {
        // Requires:int类型的结点号,int类型的深度信息
        // Modifies:创建一个新的node对象，修改了此对象的NO,depth属性
        // Effects:创建了一个新的node对象并初始化
        NO = _NO;
        depth = _depth;
    }
}