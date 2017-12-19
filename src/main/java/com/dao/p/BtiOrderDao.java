package com.dao.p;

import com.entity.p.BtiAgent;
import com.entity.p.BtiOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BtiOrderDao extends JpaRepository<BtiOrder, Long> {
    BtiOrder findById(Long id);
    @Query(value="select o from BtiOrder o where o.status =0 and (o.checkerLoginUserId = :checkerLoginUserId  or o.checkerLoginUserId is null) ")
    Page<BtiOrder> findUnCheckOrder(@Param("checkerLoginUserId") String checker, Pageable pageable  );

    @Query(value="select o from BtiOrder o where o.status=1 and  o.checkerLoginUserId = :checkerLoginUserId ")
    List<BtiOrder> findCheckingOrder( @Param("checkerLoginUserId") String checker);

    @Query(value="select count(o) from BtiOrder o where o.status<>2 and  o.checkerLoginUserId = :checkerLoginUserId ")
    int getCheckingOrder( @Param("checkerLoginUserId") String checker);

    @Query(value="select o from BtiOrder o where o.status=2 and  o.checkerLoginUserId = :checkerLoginUserId ")
    List<BtiOrder> getCheckedOrder( @Param("checkerLoginUserId") String checker);

    @Query(value="select o from BtiOrder o where   o.orderLoginUserId = :checkerLoginUserId ")
    List<BtiOrder> getMyOrder( @Param("checkerLoginUserId") String checker);

    @Query(value="select count(o) from BtiOrder o where o.status=0")
    int getUncheckedOrder( ) ;
}
