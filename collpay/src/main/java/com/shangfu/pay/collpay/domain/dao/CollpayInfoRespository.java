package com.shangfu.pay.collpay.domain.dao;


import com.shangfu.pay.collpay.domain.entity.CollpayInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;

/**
 * 对传递的参数(交易信息)操作的持久层
 */

@Repository
public interface CollpayInfoRespository extends JpaRepository<CollpayInfo,String>, JpaSpecificationExecutor<CollpayInfo>, Serializable {

    @Query("select c from CollpayInfo c where c.trade_state =?1")
    List<CollpayInfo> findByTradeState(String tradeState);

    @Query("select c from CollpayInfo c where c.out_trade_no =?1")
    CollpayInfo findByOutTradeNo(String OutTradeNo);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CollpayInfo c set c.trade_state =?1, c.err_code =?2, c.err_msg =?3 where c.out_trade_no =?4")
    Integer updateByoutTradeNo(String tradeState, String errCode, String errMsg, String OutTradeNo);

    @Transactional
    @Modifying
    @Query("update CollpayInfo c set c.notice_status = ?2 where c.out_trade_no = ?1")
    Integer updateByNoticeStatus(String out_trade_no, String notice_status);

}
