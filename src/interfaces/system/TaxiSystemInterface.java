package interfaces.system;

import models.map.Edge;
import models.request.TaxiRequest;

/**
 * 出租车系统接口
 */
public interface TaxiSystemInterface {
    /**
     * 经过边
     *
     * @param edge 经过边
     */
    void walkBy(Edge edge);
    
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
}
