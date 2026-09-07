package com.honeychain.controller;

import com.honeychain.dto.ConsumerVerificationView;
import com.honeychain.service.ConsumerVerificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VerificationPageController {

    private final ConsumerVerificationService consumerVerificationService;

    public VerificationPageController(ConsumerVerificationService consumerVerificationService) {
        this.consumerVerificationService = consumerVerificationService;
    }

    @GetMapping("/verify/{batchId}")
    public String verify(@PathVariable Long batchId, Model model) {
        ConsumerVerificationView view = consumerVerificationService.buildView(batchId);
        model.addAttribute("view", view);
        return "verify";
    }
}
