# OO第九次作业README
<del>嘛。。以后要拿中文写文档了呢</del>

[点击就送指导书](http://misaka-oss.oss-cn-beijing.aliyuncs.com/cs/oo/OO%E7%AC%AC09%E6%AC%A1%E4%BD%9C%E4%B8%9A%E6%8C%87%E5%AF%BC%E4%B9%A6.pdf)

## 运行环境
* java 1.8.0_112  
* javac 1.8.0_112
* git 2.7.4
* 编写者操作系统 Windows 10 Professional
* 基本硬件配置 4core i7 CPU, 8G RAM

## 需求分析与设计
### 需求
* 模拟一个出租车系统
* 出租车按照指定的模式自行行进
* 出租车调度中心按照指定的模式进行分配调度
* 支持通过控制台（或者其他形式）状态访问等操作
* 配合GUI详细展示整个动态效果 

### 架构布置
#### 路网(FlowMap)
* 路网仅用于存储程序所使用的**路网的拓扑结构信息**。
* 路网支持**节点和边的一些相关查询**（边存在性，点相关的边等）。
* 路网对象提供**路网结构数据导出**接口（**用于给GUI组件快速提供路网结构数据**）。
* 【新增】支持流量信息对外查询
* 支持最短路计算（内置进了该部分）
* 支持道路增删

#### 流量处理单元（MapFlow）
* 支持**流量累积统计**
* 支持**上一个时间单元流量查询**
* 通过外部定时器支持定时切换时间片

#### 出租车(Taxi)
* 出租车为**出租车动作模拟的基本单位**，只负责单个出租车本身的行为模拟。
* 可以查询出租车各项指标和状态。
* 当位置和状态发生改变时，**可以通过事件机制实时触发自定义动作**。
* 可以进行请求**任务的分配**，且带有**线程安全保护措施**。

#### 出租车调度系统(TaxiSystem)
* 出租车调度系统负责从外部**接受用户请求**，并**按照规定的策略尝试分配给出租车**。
* 出租车调度系统根据出租车所提供的事件机制，**监测出租车的变化，并提供相关的查询服务**。
* 出租车调度系统需要**能跟GUI进行一些数据对接**以实现动态渲染视图。

### 类图
#### 路网结构相关
![](http://misaka-oss.oss-cn-beijing.aliyuncs.com/others/project_9_map.png)

#### 出租车系统
![](http://misaka-oss.oss-cn-beijing.aliyuncs.com/others/project_9_taxi.png)

#### 请求
![](http://misaka-oss.oss-cn-beijing.aliyuncs.com/others/project_9_request.png)

## 系统设定
### 路网
* 路网横纵坐标编号均从`0`到`79`。
* 相邻点之间的连接状况

### 出租车
#### 编号
* 出租车编号从`1`到`100`

#### 状态
* **FREE** 自由状态，可以接收服务请求。
* **GOING\_TO\_SERVICE** 等待服务状态，该车正在赶往乘客位置（到达位置后等待的1s内也处于这一状态）。
* **IN\_SERVICE** 服务状态，该车正在载着乘客赶往目的地。
* **STOPPED** 停止状态，到达目的地后或随机行进20s后的1s修整时处于这一状态。 

#### 方向
* **UP** 向上，行为为`x -= 1`
* **DOWN** 向下，行为为`x += 1`
* **LEFT** 向左，行为为`y -= 1`
* **RIGHT** 向右，行为为`y += 1`

## 使用
### 地图输入
格式参见[指导书](http://misaka-oss.oss-cn-beijing.aliyuncs.com/cs/oo/OO%E7%AC%AC09%E6%AC%A1%E4%BD%9C%E4%B8%9A%E6%8C%87%E5%AF%BC%E4%B9%A6.pdf)，文件名`map.txt`。

**地图仍采用单独文件输入，而不是load_file输入**

### load_file输入
* 在程序的一开始，基础数据加载完毕后，可以输入**一条**`load_file`指令
* 格式为`load_file <filename>`，例如`load_file 1.txt`表示加载`1.txt`（**绝对路径相对路径均支持**）
* `load_file`指令不输入或者格式错误（在控制台上有操作反馈），**除了不会加载文件内数据外，不影响程序继续执行**。

#### load_file文件内格式
* `No.<id> <status> <credit> <position>` 表示将第`id`号出租车，状态设置为`status`，信用度设置为`credit`，位置设置为`position`，其中：
	* `id`需要保证合法，即`0`~`99`
	* 为了避免"滴滴打鬼"情况出现，本命令中**`status`只允许设置为`FREE`和`STOPPED`**（`STOPPED`将休止后继续恢复`FREE`状态）
	* `credit`请在`Integer`范围内（<del>其实可以设置为负数</del>）
	* `position`请确保合法
* `No.<id> <status> <request>`，表示将第`id`号出租车，状态设置为`status`，且当前单子设置为`request`，其中：
	* `id`需要保证合法，即`0`~`99`
	* **`status`四种状态均可**（由于一并设置了当前订单，故不会出现"滴滴打鬼"现象，可以较好模拟中间情况）
	* `request`即为一般的出租车请求格式，代表当前处理的订单
* 一般的出租车请求格式（具体格式见demo或者下文）

#### load_file demo
以上请求的样例如下：

<pre>
No.1 FREE 0 (1, 1)
No.2 FREE 0 (1, 1)
No.3 IN_SERVICE 0 (1, 1)
No.99 FREE 0 (70, 79)
No.98 FREE 0 (1, 1)

No.99 GOING_TO_SERVICE [CR, (1, 2), (70, 72)]
No.98 IN_SERVICE [CR, (1, 2), (39, 79)]
[CR, (1, 2), (70, 72)]
[CR, (1, 3), (70, 71)]
[CR, (1, 4), (70, 70)]
[CR, (1, 5), (70, 69)]
[CR, (1, 6), (70, 88)]
</pre>

其中：
* `No.3 IN_SERVICE 0 (1, 1)`为非法指令，解析失败
* ``（空行）为非法指令，解析失败
* `[CR, (1, 6), (70, 88)]`为非法指令，解析失败

注意：

* 在进行读入的时候，**成功读入的语句会在控制台上有反馈输出**（即识别失败的语句将不会有输出，可以据此判定某行是否解析成功）
* 如果多条**指令之间存在矛盾的话，以后面的为准**（相关请求执行方式为顺序执行，故后面的同类请求会覆盖前面的请求）
* 在`load_file`文件中出现的出租车请求**将会在系统start之后一并投入系统参与计算**。

### 控制台输入
* 输入格式如`[CR, (x0, y0), (x1, y1)]`的指令（支持带空格），表示向出租车系统发出从`(x0, y0)`去`(x1, y1)`的请求。（即**一般的出租车请求格式**）
* 输入格式如`query_taxi <taxi_id>`的指令，表示查询编号为`taxi_id`的出租车。例如：`query_taxi 23`将查询`23`号出租车当前信息。
* 输入格式如`query_taxi_by_status <taxi_status>`的指令，表示查询状态为`taxi_status`的出租车，例如：`query_taxi_by_status GOING_TO_SERVICE`表示查询`GOING_TO_SERVICE`状态的出租车。**其中<taxi_status\>部分大小写不敏感，且\_可以替换为-**（即`going-to-service`与`GOING_TO_SERVICE`等价）。
* 输入格式如`set_road (x0, y0) <direction> to <status>`的指令，表示将节点`(x0, y0)`与其指定方向上的对应相邻节点设置为指定的状态。
	* 其中，`direction`为方向值（**具体取值见上文**）
	* 其中，`status`包含两种取值
		* `CONNECT` 表示连接这两个点 
		* `UNCONNECT` 表示断开这两个点
	* 其中，**可以自由开闭任何相邻节点之间的道路**
	* 其中 \* 4，**请测试者保证图的连通性**。整个系统运转过程中将不会再次检查图连通性，一旦连通性被破坏，程序虽然不会crash，但是可能出现不可预料的结果。


### 控制台输出

* 控制台将输出出租车系统内的一些关键事件
	* 接单状况
		* 是否成功分配出租车？
		* 如果成功分配，出租车编号为多少？
	* 出租车运行状况
		* 出租车到达乘客位置
		* 出租车到达乘客目的地 
* 控制台将针对控制台输入的查询请求作即时回复
	* 对于`query_taxi`，将输出**出租车实时的状态信息**（编号、信用积累、位置、状态）
	* 对于`query_taxi_by_status`，将输出**该状态出租车的查询结果**（查询状态、结果数、符合条件的出租车编号） 
	* 对于`set_road`，将**输出操作是否成功**（只要两个点均合法即可成功，无论原先边状态如何）
* 各类异常操作信息
	* 基础数据异常，例如：路网图存在诸如不连通、边越界之类的数据异常 
	* 指令格式异常，例如：格式未识别的指令
	* 指令数据异常，例如：参数越界或者查询结果不存在的指令 

### 日志输出
在日志（文件名`log.txt`）中，将有较为详细的系统调度信息，可以自行查看日志，此处不再赘述。

### GUI
* GUI不使用`4*4`区域框
* GUI将详细展示点的运动状态
* GUI将展现出边的开闭状态

## 注意事项
* 本程序中除非特殊说明，否则**输入的枚举类参数均大小写不敏感，且`-`和`_`等价**。
* 除上条之外，本程序中各处格式，除非特殊说明，否则**一律大小写敏感**。
* 本程序中各处输入格式，除非特殊说明，否则一律**支持在元素之间带空格**。例如：`[CR,(0, -0),( 1 , 1) ] `合法，而`[CR, (1 2, 3), (3, 2)]`不合法。
* 事件机制本质是基于抽象类+接口的**面向接口编程**。
* 由于图片和指导书存储在云端，如果本文档图片加载不出来，**请连网**。
* `detail.txt`日志文件配合`grep`命令食用更佳。
* <del>请无视官方gui包的包名。Q: 这明明是真心话啊喵？ A: 闭嘴，大实话怎么能瞎说。</del>
* 看不懂代码？[javadoc在此](javadoc\index.html)，欢迎找bug。

## 其(xin4)他(yang3)
<pre>
                                                                                          
                                                                                          
                        `[+_&lt;&quot;            .,;&quot;^.&#39;^```.        .]&gt;:&quot;&quot;                      
                       !aaZZqO      .^,,,,,,&quot;&quot;^^^&quot;&quot;&quot;,;;^&#39;.    10aZwO;                     
                     `1/ow_a/*i   ^:;:,&quot;&quot;&quot;^^^^^^::,,&quot;``^&quot;::, ~w)*aaow&#39;                    
                    :|)\(` ,wo\&#39;&quot;!;&quot;^^&quot;,,&quot;^^^`^^&quot;,:::;&quot;`^^^:&lt;o]a?`0ao-                    
                  .+([|_. .`&gt;:i;,^^^&quot;,,:!i:&quot;^`,^^^&quot;:;;&gt;;`^`,\?11  ]Z|o^                   
                 ^]1])&quot; &quot;i::`:,`^&quot;&quot;,,::!!&gt;!;,&quot;:&quot;`^^&quot;:~~_;`&#39;-1?): ^&quot;;i?-.                  
                :m)|*&gt;  ,_][)&gt;,&quot;&quot;&quot;,;;::::;:;!!:&quot;^^^^&quot;&gt;-~~^;[-?_`&gt;!^^^^:&lt;.                 
                ./w/*o.&quot;?+-o&gt;&quot;:,,:;::,!:,,,;;&lt;-^&quot;^^```:_i&gt;(]1),.&quot;;&quot;&quot;&quot;&quot;&quot;,!.                
                 ./oo0)-_-o~`,,,,::,,&quot;&gt;&quot;&quot;,,::i-i&quot;&quot;^^```!&lt;?O|/*!.&quot;;&quot;&quot;&quot;&quot;&quot;,:&quot;                
                  `a//0(&lt;(] .`,,,:,,&quot;:1&quot;&quot;,,;::+];^&quot;&quot;^```&lt;,!a\\w:.&gt;:&quot;,,,,:,&quot;               
                   ,w|/a?1`  `,,,,,,&quot;&gt;?&quot;&quot;,&quot;,;,-&gt;::^&quot;,```,:.;w\\/:-[,,,,,,:;`              
         :&quot;^^      ,0a(|a&lt;..&#39;^,&quot;``&quot;^;&quot;;,^&quot;&quot;,;,+:.&quot;,`,:``^;^&#39;:/\(o)--:;;;:::!              
  `  ^  ^~-;&quot;      ~;1\((&quot;^&quot;^^^&quot;&quot;^&quot;^:.^;^&quot;&quot;^,^!;. &#39;&quot;`,:^`&quot;&quot;`&#39;^(o1o?+_:::;;,:&quot;             
  :&quot;,_; .`,,      ,;:!)o-`,&quot;`^``^^^,` .;&quot;&quot;&quot;^&quot;^,!.  .&quot;^,;^^&quot;``&#39;`]\[[;+-!!:;:,:.            
  ,&lt;^,!   &#39;^     &#39;&lt;::;;[~`;&quot;```^^^^,.  `;^^^^^^:&#39;   .&quot;:!,`;&quot;^&quot;^`|a/&lt;:-[!:;:,,^            
   :.&#39;`&#39;   .&#39;    ,::;;:-~^i,``&#39;^^^`^   .&quot;^&#39;&quot;,&quot;`;^...  .`;,::&quot;:,`:||?!&gt;1-;;,,&quot;&quot;            
   .   .^ `,:.  `,:;;;:++&quot;i:^``,^`+\+~;&#39; ^`^,:`^&quot;..:~&lt;;;&quot;&quot;&gt;i:::^,&lt;&lt;?+&gt;-]!,,&quot;&quot;&quot;^           
        :^  :. .,,::;;,)]&quot;i:,&quot;^:!*w|qZZw, ^`^^^&#39;&#39;.;obdOqZ1_~;:~;,&gt;~-?_--+,&quot;&quot;&quot;^:           
       .,:     :&quot;;;;!~+&gt;i;!::;&quot;io~ _1_]o0` .&#39;``&#39; .;,o++[/Z|&lt;;;-&quot;,:~:~?--?!^&quot;^^&quot;`          
              ,,,;;!i&gt;,;|o~:,i:&gt;?  ]--]]|:   ...   &#39;-~-?](i&lt;;:~,&#39;i1 ;?&lt;+-&gt;^&quot;^^^&quot;          
             `:^;&gt;i&gt;:^+*oo([i&gt;&lt;&gt;&gt;  :i:;,&gt;,         .&gt;:;;;_,&gt;!!&gt;:`_` ^_!&gt;+~&quot;^^^^,^         
             !&quot;&quot;;&gt;&gt;i^_w///oww?-!;. &#39;;&quot;^^&quot;.          :,^&quot;^,&quot;i;&gt;:^&quot;.  .&lt;:;&gt;+:^^^^^;         
            ,,^&quot;!&gt;+,,oooo//*00|i&quot;.. .&#39;..             .``.`^&gt;&quot;&gt;;~&#39;    :;;;~;^^^^^,`        
           .!^^:&gt;&gt;!^~*ooooaw000|_&quot;&#39;`..              ..&#39;&#39;`^,:^!&gt;.     &quot;;,:;i^^^^^^,        
           ;&quot;^&quot;ii!;&quot;[a/oaawww0wo/?&#39;            .     .&#39;&#39;&quot;^&gt;,,;.      `;&quot;,:i&quot;^^^^^,.       
          `:^&quot;;iii+ioZaaa0ZmZZ0*())!`.       ...     .._&gt;:&quot;&#39;.        .:&quot;&quot;,:&quot;^^^^&quot;&quot;&quot;       
          ;^^&quot;iii&lt;+~[w^...`,&gt;\a*|?-1|1i;i:&quot;&quot;^`&#39;`^...  .~;.            ;&quot;&quot;&quot;:&quot;^^^^&quot;&quot;:.      
         &quot;,^^;iii;&gt;~&lt;~       ^;&lt;_([??]~&gt;!i&lt;/(i&quot;&gt;*,    .       :`      :,&quot;&quot;,,^^^^&quot;,,&quot;      
         :^^&quot;!ii:,!&gt;],        &quot;..:1o(&gt;;^ ..,&gt;^.,i&gt;.          ^&gt;;      ^,&quot;&quot;&quot;&quot;^^^&quot;^:&quot;:      
        ^&quot;^^,!;;,,;&lt;[&#39;             &quot;i;^...`!~;&lt;&gt;,&quot;&#39;.          ,       &#39;:&quot;&quot;&quot;&quot;^&quot;&quot;&quot;^,&quot;;&#39;     
        &quot;^^^;::,,:,&lt;[                     !&lt;;][-_&lt; ^&lt;&#39;                &#39;!^&quot;&quot;&quot;&quot;^^^^&quot;,&quot;&quot;     
       ^^^^&quot;;:,,,:,~i                     ,,,_]-;i~i(*.               .i^^^^&quot;^^&quot;^^:&quot;:     
       &quot;`^^:::,,,,,+,        :           .!^&quot;~--,^)wOwa^              .i^^^^&quot;&quot;^^&quot;^:&quot;:`    
      &quot;^^^^:;:,,,,,_^       :!,.         ;&quot;^^i:!:&#39;;owoa*,             .&gt;^^^^&quot;&quot;&quot;^^^&quot;:&quot;;    
     .:`^^&quot;;;:,,,,,_&#39;       &#39;^,         &quot;:`^^;,,;``;&lt;*//*].           .i^^^^&quot;&quot;,,&quot;^^:&quot;;.   
     ;,`^^:;:::::::+                   i:`^^^:&quot;^,``&quot;,1Zaow\&quot;          .;&quot;&quot;^&quot;^,;;:&quot;^,,:&quot;   
     ;`^^^;::::::::_                &#39;&gt;[:`^^^^;^`:^``&quot;&gt;a0a/\\-&quot;.       `!^&quot;^&quot;^,;;;:&quot;&quot;,&quot;i   
    `,`^^^i;;:::::;+               &quot;aa(:``````_:^^``&#39;,[w\1[][1]_?&quot;    ^!^&quot;^&quot;^;iii:,,:&quot;-.  
    ,^`^^^&gt;;;::::,;&gt;             &quot;]/[[|)i^&#39;&#39;&#39;&lt;0*:&#39;&#39;`!(0o|[]--_--1+,   &quot;,^&quot;^&quot;^;&gt;i&gt;;:;&gt;,_`  
    ;``^^^i;;;;;:,!;          `:&lt;\0\|(|//)~+\Zo0Z-&gt;\](a(/1-______&quot;^   ;,^&quot;&quot;^&quot;i&lt;&gt;&gt;!;;&gt;;&gt;`  
   ^:```^^!;:;::,,;;          .;!]-|a/oao**O0/awOm0O/]1&lt;&gt;&lt;-+_+_?:&quot;&#39;  &#39;&gt;&quot;^&quot;&quot;&quot;:&gt;&lt;&gt;&gt;i!;&lt;:;^  
   ::&#39;``^^!;;:,,,,:!.            ,+~|O0*0**wa|*0ZOOOZOo+i&quot;+1_]?;&quot;,   &quot;i&quot;&quot;,,,!i&gt;&gt;&gt;i!!-,:^  
   !;&#39;``^^;;,,,&quot;&quot;&quot;,,&quot;             &#39;~~]o-(*wO*o0w0Zo)??-]?!,()&lt;,,&quot;    !:&quot;&quot;,;;!!i&gt;i!::~&quot;&gt;&#39;  
   ,,````^,;,&quot;&quot;&quot;&quot;&quot;,^,              ^&gt;;~?_[*o--?[|0-i!:&quot;&quot;`  .?;^.    .!&quot;,&quot;,:;;;!!;:,:;,;   
   ,,&#39;```^&quot;,&quot;&quot;&quot;&quot;&quot;&quot;,`^.             &quot;&#39;&#39;^&quot;~^&quot;^```^`&quot;:,`   .   ..      ;:&quot;,,:;;;;:;;,,::&quot;.   
   &quot;;&#39;`&#39;`^^&quot;&quot;&quot;&quot;&quot;&quot;&quot;,^,.             ]&gt;;:;`          ,,&#39;`&lt;&gt;          &#39;&gt;&quot;,,;!:;::,,,&quot;;&quot;,`    
    ;&#39;&#39;&#39;&#39;^^^&quot;&quot;&quot;&quot;&quot;^;`:,            `w/\\:            _))/Z&#39;         :,,,:;;;:::&quot;&quot;^;&quot;&quot;,.    
    ,&quot;&#39;```^^&quot;&quot;&quot;&quot;&quot;^:,.&quot;&#39;           ~0*0o              wm*Z_        &quot;:,::;;;;&quot;&quot;,^^::^&quot;      
    .&gt;&quot;&#39;``^&quot;,^^&quot;&quot;^:;....         .o*aZ,              `m0w0&#39;      `!;;;;;:&quot;&quot;^&quot;,,,:&quot;:       
     ^:&quot;```^;,^&quot;^^&quot;; ...         &gt;waZ+                :mwO)      ,;;;:;;&quot;&quot;&quot;^;:,;;&quot;.       
      .`^`^^,+,`^^^&quot;.            /ow(                  ~Z*Oi    ;!:;;;,&quot;&quot;&quot;,;!,^&#39;          
       .^``^&quot;,&gt;;&quot;^``^           ~*aw&#39;                   [Z*0&quot; .!!,,,,,,,&quot;&quot;,:`.            
         &#39;&#39;.&#39;:;;:,&quot;^^&#39;         &#39;&lt;:_&quot;                    .+&gt;:&quot;`;;,,:,&quot;`..```               
             .&quot;:!;,,&quot;^&#39;.                                    ^&lt;:,&quot;^&#39;                       
                ....`&quot;&quot;&#39;                                    ^.                            
                                                                                          
                             
</pre>
