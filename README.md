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

### 信号灯输入
格式参见[指导书](http://misaka-oss.oss-cn-beijing.aliyuncs.com/cs/oo/OO%E7%AC%AC09%E6%AC%A1%E4%BD%9C%E4%B8%9A%E6%8C%87%E5%AF%BC%E4%B9%A6.pdf)，文件名`traffic.txt`。

**信号灯采用单独文件输入，而不是load_file输入**

此外：

* 如果信号灯输入文件格式有异常，将在控制台上进行反馈
* 如果有异常，将跳过这一块**直接继续执行接下来的程序，且视为所有路口均无信号灯（保证操作原子性）**。 

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

### 其他一些骚操作
#### 路径动态选择策略
按照之前的一些同学讨论，大致分为两派：

* 打算开始时规划路径然后遇到被切断的路径再更新
* 打算一步一更新

这两种方法各有优劣：

* 前者很节约计算资源，但是机动性较差，无法做到实时避开高流量区域，用户体验较差（<del>可以想象为一个不会实时避开拥堵区域的百度地图导航，显然不可能有人想用这种东西的</del>）
* 后者机动性很高，可以做到实时避开拥堵区，但是计算资源消耗严重。尤其是在这样的一个系统中，很多时间单位都有一个最大公因数`500ms`，这意味着高并发状态下，每`500ms`就得应该一波最短路计算高峰。而本人程序在压测下，最短路计算引擎在一秒内大概只能计算`300`次左右的最短路。这将导致计算资源非常紧张（cpu占用飙升且居高不下）。

于是，基于以上，**本人采取了一种较为折中的策略**：

* 每次计算最短路后，行进`x`步后重新计算路线（`x`为\[2, 6\]的随机整数）
* 在每次行走的过程中，要是遇到断路，立刻退出循环并重新计算路线
* 直到走到终点为止

这样的做法有以下一些好处：

* 具有一定的机动性（显然）
* 同时节省计算资源（显然）
* 除此之外，由于`x`为一个随机数，所以意味着当同一批大量订单下达之后（假设100辆车全部进入待命状态），接下来各个车重新计算的时间点将被分散到（`2` `3` `4` `5` `6`五个时间点上，之后也将大致呈现均匀分布。故同一时间的计算压力将减少`80%`，系统运行将更加平稳）

#### 官方<del>智障</del>GUI
* 官方GUI放置在`shit_like_code`包下
* 本人这次对官方GUI包进行了一点小的改动，当一辆车在等待红灯的时候，将会出现<i style="color: #ff00ff">紫色圈</i>。

## 注意事项
* 本程序中除非特殊说明，否则**输入的枚举类参数均大小写不敏感，且`-`和`_`等价**。
* 除上条之外，本程序中各处格式，除非特殊说明，否则**一律大小写敏感**。
* 本程序中各处输入格式，除非特殊说明，否则一律**支持在元素之间带空格**。例如：`[CR,(0, -0),( 1 , 1) ] `合法，而`[CR, (1 2, 3), (3, 2)]`不合法。
* 事件机制本质是基于抽象类+接口的**面向接口编程**。
* 由于图片和指导书存储在云端，如果本文档图片加载不出来，**请连网**。
* `detail.txt`日志文件配合`grep`命令食用更佳。（具体：`cat detail.txt|grep "Taxi No.xxx"`类似这样的；<del>Q：Windows用户怎么办？ A：那就人眼看吧，祝你不要看漏哦23333</del>）
* 本程序`repOK`均使用全局接口`ApplicationClassInterface`默认实现，除少数类情况特殊进行了重写之外（例如`Node`类），行为均为**判定内部属性（包括私有属性）是否全部不为`null`**（Q：怎么实现的呢？为啥类中找不到`repOK`？ A：自行Google，`java interface default`，提高姿势水平） 
* <del>请无视官方gui包的包名。Q: 这明明是真心话啊喵？ A: 闭嘴，大实话怎么能瞎说。</del>
* Q：jsf啥的怎么办？ A：随你咯<del>，120+个类欢迎慢慢找</del>
* Q：类Overwrite在哪？ A：每个类上头都有javadoc，等价于Overwrite。 
* 看不懂代码？[javadoc在此](javadoc\index.html)，欢迎找bug，<del>给个笑容自己体会，找到bug算我输</del>。（<del>Q：那。。河蟹六系怎么办？ A：这种东西我才不在乎23333。比起这个，这么多次一个bug都没有过才叫寂寞呢</del>）

![](http://misaka-oss.oss-cn-beijing.aliyuncs.com/others/misaka-sister-1.jpg)