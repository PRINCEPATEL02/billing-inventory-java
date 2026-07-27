package com.billing.service;

import com.billing.dto.DashboardDTO;
import com.billing.repository.BillRepository;
import com.billing.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final BillRepository billRepository;
    private final ProductRepository productRepository;

    public DashboardService(BillRepository billRepository, ProductRepository productRepository) {
        this.billRepository = billRepository;
        this.productRepository = productRepository;
    }

    public DashboardDTO getDashboardData() {
        LocalDateTime todayStart = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime weekStart = todayStart.minusDays(7);
        LocalDateTime monthStart = todayStart.minusDays(30);
        LocalDateTime yearStart = todayStart.minusDays(365);

        return DashboardDTO.builder()
                .todaySales(billRepository.sumTodaySales())
                .weeklySales(billRepository.sumSalesFrom(weekStart))
                .monthlySales(billRepository.sumSalesFrom(monthStart))
                .yearlySales(billRepository.sumSalesFrom(yearStart))
                .totalRevenue(billRepository.sumSalesFrom(LocalDateTime.of(2000, 1, 1, 0, 0)))
                .totalBills((long) billRepository.findAll().size())
                .totalProducts((long) productRepository.findByActiveTrue().size())
                .currentStock(productRepository.sumCurrentStock())
                .lowStockCount(productRepository.countLowStock())
                .dailySalesChart(getDailySales())
                .monthlySalesChart(getMonthlySales())
                .yearlySalesChart(getYearlySales())
                .topSellingProducts(getTopSellingProducts())
                .leastSellingProducts(getLeastSellingProducts())
                .build();
    }

    private List<Map<String, Object>> getDailySales() {
        List<Map<String, Object>> result = new ArrayList<>();
        return result;
    }

    private List<Map<String, Object>> getMonthlySales() {
        List<Map<String, Object>> result = new ArrayList<>();
        return result;
    }

    private List<Map<String, Object>> getYearlySales() {
        List<Map<String, Object>> result = new ArrayList<>();
        return result;
    }

    private List<Map<String, Object>> getTopSellingProducts() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Object[]> topProducts = billRepository.findTopSellingProducts();
        for (Object[] obj : topProducts.subList(0, Math.min(5, topProducts.size()))) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", obj[0]);
            map.put("totalQuantity", obj[1]);
            result.add(map);
        }
        return result;
    }

    private List<Map<String, Object>> getLeastSellingProducts() {
        List<Map<String, Object>> result = new ArrayList<>();
        return result;
    }
}