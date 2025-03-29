package com.example.backend.service;

import com.example.backend.ENUM.ORDER_STATUS;
import com.example.backend.model.ProductStatistic;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface StatisticalService {
    double getTotalRevenue(Date startDate, Date endDate);
    int getTotalOrders(Date startDate, Date endDate);
    List<ProductStatistic> getBestSellingProducts(Date startDate, Date endDate, int limit);

    Map<String, Double> getRevenueByCategory(Date startDate, Date endDate);

    Map<ORDER_STATUS, Integer> getOrderStatusStatistics(Date startDate, Date endDate);

    int getTotalProductsSold(Date startDate, Date endDate);

}
