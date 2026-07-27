package com.billing.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private BigDecimal todaySales;
    private BigDecimal weeklySales;
    private BigDecimal monthlySales;
    private BigDecimal yearlySales;
    private BigDecimal totalRevenue;
    private Long totalBills;
    private Long totalProducts;
    private Long currentStock;
    private Long lowStockCount;
    private List<Map<String, Object>> dailySalesChart;
    private List<Map<String, Object>> monthlySalesChart;
    private List<Map<String, Object>> yearlySalesChart;
    private List<Map<String, Object>> topSellingProducts;
    private List<Map<String, Object>> leastSellingProducts;

    public DashboardDTO() {}

    public DashboardDTO(BigDecimal todaySales, BigDecimal weeklySales, BigDecimal monthlySales, BigDecimal yearlySales, BigDecimal totalRevenue, Long totalBills, Long totalProducts, Long currentStock, Long lowStockCount, List<Map<String, Object>> dailySalesChart, List<Map<String, Object>> monthlySalesChart, List<Map<String, Object>> yearlySalesChart, List<Map<String, Object>> topSellingProducts, List<Map<String, Object>> leastSellingProducts) {
        this.todaySales = todaySales;
        this.weeklySales = weeklySales;
        this.monthlySales = monthlySales;
        this.yearlySales = yearlySales;
        this.totalRevenue = totalRevenue;
        this.totalBills = totalBills;
        this.totalProducts = totalProducts;
        this.currentStock = currentStock;
        this.lowStockCount = lowStockCount;
        this.dailySalesChart = dailySalesChart;
        this.monthlySalesChart = monthlySalesChart;
        this.yearlySalesChart = yearlySalesChart;
        this.topSellingProducts = topSellingProducts;
        this.leastSellingProducts = leastSellingProducts;
    }

    public static DashboardDTOBuilder builder() {
        return new DashboardDTOBuilder();
    }

    public static class DashboardDTOBuilder {
        private BigDecimal todaySales;
        private BigDecimal weeklySales;
        private BigDecimal monthlySales;
        private BigDecimal yearlySales;
        private BigDecimal totalRevenue;
        private Long totalBills;
        private Long totalProducts;
        private Long currentStock;
        private Long lowStockCount;
        private List<Map<String, Object>> dailySalesChart;
        private List<Map<String, Object>> monthlySalesChart;
        private List<Map<String, Object>> yearlySalesChart;
        private List<Map<String, Object>> topSellingProducts;
        private List<Map<String, Object>> leastSellingProducts;

        DashboardDTOBuilder() {}

        public DashboardDTOBuilder todaySales(BigDecimal todaySales) { this.todaySales = todaySales; return this; }
        public DashboardDTOBuilder weeklySales(BigDecimal weeklySales) { this.weeklySales = weeklySales; return this; }
        public DashboardDTOBuilder monthlySales(BigDecimal monthlySales) { this.monthlySales = monthlySales; return this; }
        public DashboardDTOBuilder yearlySales(BigDecimal yearlySales) { this.yearlySales = yearlySales; return this; }
        public DashboardDTOBuilder totalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; return this; }
        public DashboardDTOBuilder totalBills(Long totalBills) { this.totalBills = totalBills; return this; }
        public DashboardDTOBuilder totalProducts(Long totalProducts) { this.totalProducts = totalProducts; return this; }
        public DashboardDTOBuilder currentStock(Long currentStock) { this.currentStock = currentStock; return this; }
        public DashboardDTOBuilder lowStockCount(Long lowStockCount) { this.lowStockCount = lowStockCount; return this; }
        public DashboardDTOBuilder dailySalesChart(List<Map<String, Object>> dailySalesChart) { this.dailySalesChart = dailySalesChart; return this; }
        public DashboardDTOBuilder monthlySalesChart(List<Map<String, Object>> monthlySalesChart) { this.monthlySalesChart = monthlySalesChart; return this; }
        public DashboardDTOBuilder yearlySalesChart(List<Map<String, Object>> yearlySalesChart) { this.yearlySalesChart = yearlySalesChart; return this; }
        public DashboardDTOBuilder topSellingProducts(List<Map<String, Object>> topSellingProducts) { this.topSellingProducts = topSellingProducts; return this; }
        public DashboardDTOBuilder leastSellingProducts(List<Map<String, Object>> leastSellingProducts) { this.leastSellingProducts = leastSellingProducts; return this; }

        public DashboardDTO build() {
            return new DashboardDTO(todaySales, weeklySales, monthlySales, yearlySales, totalRevenue, totalBills, totalProducts, currentStock, lowStockCount, dailySalesChart, monthlySalesChart, yearlySalesChart, topSellingProducts, leastSellingProducts);
        }
    }

    public BigDecimal getTodaySales() { return todaySales; }
    public void setTodaySales(BigDecimal todaySales) { this.todaySales = todaySales; }

    public BigDecimal getWeeklySales() { return weeklySales; }
    public void setWeeklySales(BigDecimal weeklySales) { this.weeklySales = weeklySales; }

    public BigDecimal getMonthlySales() { return monthlySales; }
    public void setMonthlySales(BigDecimal monthlySales) { this.monthlySales = monthlySales; }

    public BigDecimal getYearlySales() { return yearlySales; }
    public void setYearlySales(BigDecimal yearlySales) { this.yearlySales = yearlySales; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public Long getTotalBills() { return totalBills; }
    public void setTotalBills(Long totalBills) { this.totalBills = totalBills; }

    public Long getTotalProducts() { return totalProducts; }
    public void setTotalProducts(Long totalProducts) { this.totalProducts = totalProducts; }

    public Long getCurrentStock() { return currentStock; }
    public void setCurrentStock(Long currentStock) { this.currentStock = currentStock; }

    public Long getLowStockCount() { return lowStockCount; }
    public void setLowStockCount(Long lowStockCount) { this.lowStockCount = lowStockCount; }

    public List<Map<String, Object>> getDailySalesChart() { return dailySalesChart; }
    public void setDailySalesChart(List<Map<String, Object>> dailySalesChart) { this.dailySalesChart = dailySalesChart; }

    public List<Map<String, Object>> getMonthlySalesChart() { return monthlySalesChart; }
    public void setMonthlySalesChart(List<Map<String, Object>> monthlySalesChart) { this.monthlySalesChart = monthlySalesChart; }

    public List<Map<String, Object>> getYearlySalesChart() { return yearlySalesChart; }
    public void setYearlySalesChart(List<Map<String, Object>> yearlySalesChart) { this.yearlySalesChart = yearlySalesChart; }

    public List<Map<String, Object>> getTopSellingProducts() { return topSellingProducts; }
    public void setTopSellingProducts(List<Map<String, Object>> topSellingProducts) { this.topSellingProducts = topSellingProducts; }

    public List<Map<String, Object>> getLeastSellingProducts() { return leastSellingProducts; }
    public void setLeastSellingProducts(List<Map<String, Object>> leastSellingProducts) { this.leastSellingProducts = leastSellingProducts; }
}