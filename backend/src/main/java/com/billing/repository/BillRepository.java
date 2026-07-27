package com.billing.repository;

import com.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBillNumber(String billNumber);

    @Query("SELECT b FROM Bill b WHERE CAST(b.createdAt AS date) = CAST(CURRENT_DATE AS date)")
    List<Bill> findTodayBills();

    @Query("SELECT b FROM Bill b WHERE b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Bill> findBillsBetween(@Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(b.grandTotal), 0) FROM Bill b WHERE CAST(b.createdAt AS date) = CAST(CURRENT_DATE AS date)")
    BigDecimal sumTodaySales();

    @Query("SELECT COALESCE(SUM(b.grandTotal), 0) FROM Bill b WHERE b.createdAt >= :startDate")
    BigDecimal sumSalesFrom(@Param("startDate") LocalDateTime startDate);

    @Query("SELECT COUNT(b) FROM Bill b WHERE CAST(b.createdAt AS date) = CAST(CURRENT_DATE AS date)")
    Long countTodayBills();

    @Query("SELECT b FROM Bill b WHERE b.billNumber LIKE %:search% OR " +
           "LOWER(b.cashier.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "ORDER BY b.createdAt DESC")
    List<Bill> searchBills(@Param("search") String search);

    @Query("SELECT bi.product.id, SUM(bi.quantity) as totalQty " +
           "FROM BillItem bi GROUP BY bi.product.id ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts();
}