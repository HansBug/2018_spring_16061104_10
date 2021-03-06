package interfaces.system;

import models.map.Edge;
import models.request.TaxiRequest;
import models.system.Taxi;

/**
 * 出租车系统接口
 */
public interface TaxiSystemInterface {
    /**
     * @overview:
     *          出租车系统接口
     */

    /**
     * 出租车经过边
     *
     * @param taxi 出租车
     * @param edge 经过边
     */
    void taxiWalkBy(Taxi taxi, Edge edge);
    
    /**
     * 出租车分配失败
     *
     * @param request 出租车请求
     */
    void allocTaxiFailed(TaxiRequest request);
    
    /**
     * 同质出租车请求
     *
     * @param request 出租车请求
     */
    void duplicatedTaxiRequest(TaxiRequest request);
    
    /**
     * 设置道路状态行为
     *
     * @param edge   道路
     * @param status 状态
     */
    void setRoadStatus(Edge edge, int status);
}
