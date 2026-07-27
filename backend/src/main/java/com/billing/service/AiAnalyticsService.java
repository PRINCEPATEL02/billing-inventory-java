package com.billing.service;

import com.billing.entity.Bill;
import com.billing.entity.BillItem;
import com.billing.entity.Product;
import com.billing.repository.BillRepository;
import com.billing.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.*;
import java.util.*;

/** Deterministic, explainable analytics. An external LLM can be layered on later without changing this API. */
@Service
public class AiAnalyticsService {
    private final BillRepository bills; private final ProductRepository products;
    public AiAnalyticsService(BillRepository bills, ProductRepository products) { this.bills = bills; this.products = products; }

    @Transactional(readOnly = true)
    public Map<String, Object> dashboard() {
        LocalDate today = LocalDate.now(); LocalDate start = today.minusDays(30);
        List<Bill> recent = bills.findBillsBetween(start.atStartOfDay(), today.plusDays(1).atStartOfDay());
        Map<Long, Map<String,Object>> metrics = new HashMap<>();
        Map<String, BigDecimal> categoryRevenue = new HashMap<>(), categoryProfit = new HashMap<>();
        BigDecimal todaySales = BigDecimal.ZERO, todayProfit = BigDecimal.ZERO;
        Map<String, BigDecimal> daily = new TreeMap<>();
        for (Bill bill : recent) {
            String day = bill.getCreatedAt().toLocalDate().toString(); daily.merge(day, bill.getGrandTotal(), BigDecimal::add);
            boolean isToday = bill.getCreatedAt().toLocalDate().equals(today);
            for (BillItem item : bill.getItems()) {
                Product p = item.getProduct(); BigDecimal qty = BigDecimal.valueOf(item.getQuantity());
                BigDecimal revenue = item.getUnitPrice().multiply(qty), profit = item.getUnitPrice().subtract(p.getPurchasePrice()).multiply(qty);
                Map<String,Object> m = metrics.computeIfAbsent(p.getId(), k -> { Map<String,Object> x = new HashMap<>(); x.put("productId", p.getId()); x.put("productName", p.getName()); x.put("category", p.getCategory()); x.put("quantity", BigDecimal.ZERO); x.put("revenue", BigDecimal.ZERO); x.put("profit", BigDecimal.ZERO); return x; });
                m.put("quantity", ((BigDecimal)m.get("quantity")).add(qty)); m.put("revenue", ((BigDecimal)m.get("revenue")).add(revenue)); m.put("profit", ((BigDecimal)m.get("profit")).add(profit));
                categoryRevenue.merge(p.getCategory(), revenue, BigDecimal::add); categoryProfit.merge(p.getCategory(), profit, BigDecimal::add);
                if (isToday) { todaySales = todaySales.add(revenue); todayProfit = todayProfit.add(profit); }
            }
        }
        List<Map<String,Object>> ranked = new ArrayList<>(metrics.values()); ranked.sort((a,b)->((BigDecimal)b.get("quantity")).compareTo((BigDecimal)a.get("quantity")));
        List<Map<String,Object>> least = new ArrayList<>(ranked); least.sort(Comparator.comparing(a -> (BigDecimal)a.get("quantity")));
        List<Map<String,Object>> inventory = new ArrayList<>();
        for (Product p : products.findByActiveTrue()) { BigDecimal sold = metrics.containsKey(p.getId()) ? (BigDecimal)metrics.get(p.getId()).get("quantity") : BigDecimal.ZERO; double dailyRate = sold.doubleValue() / 30d; long days = dailyRate == 0 ? 999 : (long)Math.floor(p.getQuantity() / dailyRate); Map<String,Object> row = new LinkedHashMap<>(); row.put("productId",p.getId()); row.put("productName",p.getName()); row.put("stock",p.getQuantity()); row.put("dailyRate",round(dailyRate)); row.put("daysRemaining",days); row.put("recommendation", days <= 7 ? "Purchase " + Math.max(p.getMinimumQuantity() * 2, 1) + " more units." : dailyRate > 0 && p.getQuantity() > sold.intValue() * 3 ? "Delay the next purchase." : "Stock level looks healthy."); inventory.add(row); }
        BigDecimal monthRevenue = recent.stream().map(Bill::getGrandTotal).reduce(BigDecimal.ZERO, BigDecimal::add); BigDecimal forecast = monthRevenue.multiply(BigDecimal.valueOf(1.05)).setScale(2, RoundingMode.HALF_UP);
        Map<String,Object> out = new LinkedHashMap<>(); out.put("todaySales",todaySales); out.put("todayProfit",todayProfit); out.put("monthlyForecast",forecast); out.put("annualForecast",forecast.multiply(BigDecimal.valueOf(12)).setScale(2,RoundingMode.HALF_UP)); out.put("revenueTrend",daily); out.put("topSellingProducts",ranked.stream().limit(5).toList()); out.put("leastSellingProducts",least.stream().limit(5).toList()); out.put("fastMovingItems",ranked.stream().limit(5).toList()); out.put("slowMovingItems",least.stream().limit(5).toList()); out.put("deadStock",inventory.stream().filter(x -> ((Number)x.get("dailyRate")).doubleValue() == 0 && ((Number)x.get("stock")).intValue() > 0).toList()); out.put("inventorySuggestions",inventory.stream().filter(x -> !"Stock level looks healthy.".equals(x.get("recommendation"))).toList()); out.put("categoryAnalysis",categoryProfit); out.put("businessSuggestions",suggestions(ranked, inventory)); out.put("lowStockAlerts",products.countLowStock()); return out;
    }
    private List<String> suggestions(List<Map<String,Object>> top, List<Map<String,Object>> inventory) { List<String> s = new ArrayList<>(); if (!top.isEmpty()) s.add("Increase inventory for " + top.get(0).get("productName") + "."); if (top.size() > 1) s.add("Consider bundling " + top.get(0).get("productName") + " with " + top.get(1).get("productName") + "."); if (inventory.stream().anyMatch(x -> ((Number)x.get("dailyRate")).doubleValue() == 0 && ((Number)x.get("stock")).intValue() > 0)) s.add("Offer discounts on slow-moving or dead stock."); s.add("Review the forecast weekly before placing supplier orders."); return s; }
    private double round(double value) { return Math.round(value * 100) / 100d; }
}
