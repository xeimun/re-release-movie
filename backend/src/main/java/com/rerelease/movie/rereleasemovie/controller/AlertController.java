package com.rerelease.movie.rereleasemovie.controller;

import com.rerelease.movie.rereleasemovie.service.NotificationQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final NotificationQueueService notificationQueueService;

    @Autowired
    public AlertController(NotificationQueueService notificationQueueService) {
        this.notificationQueueService = notificationQueueService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerAlert(@RequestParam Long userMovieAlertId) {
        notificationQueueService.addAlertToQueue(userMovieAlertId);
        return ResponseEntity.ok("알림이 성공적으로 등록되었습니다.");
    }
}
