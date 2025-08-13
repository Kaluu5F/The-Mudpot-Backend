package com.the.mudpot.service;

import com.the.mudpot.model.MenuItem;
import com.the.mudpot.repository.MenuItemRepository;
import com.the.mudpot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private MenuItemRepository menuItemRepository;

    public Map<String,String> getStatus() {
        Map<String,String> status = new HashMap<>();
        int userCount = (int) userRepository.count();
        int menuItemCount = (int) menuItemRepository.count();


        return Map.of(
                "currentOrders", String.valueOf(menuItemCount),
                "completeOrders", String.valueOf(10),
                "totalUsers", String.valueOf(userCount),
                "menuItems", String.valueOf(10)
        );

    }


    public Map<String, Integer> getStatistics() {
        Map<String, Integer> statistics = new HashMap<>();
        List<MenuItem> result = menuItemRepository.findAll();

        for (MenuItem menuItem : result) {
            String category = (menuItem.getCategory() != null)
                    ? menuItem.getCategory().name()
                    : "UNKNOWN";

            statistics.merge(category, 1, Integer::sum);
        }
        return statistics;
    }

}
