package com.skq.springbootskqinit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@RestController
@RequestMapping("/")
public class HealthCheckController {

//    @GetMapping("/health")
//    public String healthCheck() {
//        return "模板项目成功运行";
//    }

    /**
     * 健康检查接口，返回标准JSON格式
     * @return 包含状态、消息、时间戳的JSON响应
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        // 构建返回的JSON数据
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success"); // 状态标识（成功/失败）
        response.put("message", "模板项目成功运行"); // 提示信息
        response.put("timestamp", System.currentTimeMillis()); // 时间戳，便于排查问题
        response.put("code", HttpStatus.OK.value()); // HTTP状态码

        // 返回包含JSON数据和200状态码的响应
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
