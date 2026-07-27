package com.billing.controller;
import com.billing.service.AiAnalyticsService;
import com.billing.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/analytics")
public class AiAnalyticsController {
 private final AiAnalyticsService service; public AiAnalyticsController(AiAnalyticsService service){this.service=service;}
 @GetMapping("/ai") public ResponseEntity<ApiResponse<Map<String,Object>>> dashboard(){return ResponseEntity.ok(ApiResponse.success(service.dashboard()));}
}
