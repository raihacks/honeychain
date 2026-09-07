package com.honeychain.controller;

import com.honeychain.dto.AdminBatchDetail;
import com.honeychain.dto.AdminOverview;
import com.honeychain.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Read-only oversight pages for KVIC / cooperative administrators:
 * system-wide counts and a hash-chain integrity check across every
 * batch, plus a full audit drill-down per batch. No write actions live
 * here - that's the beekeeper dashboard's job (/dashboard).
 *
 * Access at: /admin
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String overview(Model model) {
        AdminOverview overview = adminService.buildOverview();
        model.addAttribute("overview", overview);
        return "admin";
    }

    @GetMapping("/batches/{id}")
    public String batchDetail(@PathVariable Long id, Model model) {
        AdminBatchDetail detail = adminService.buildBatchDetail(id);
        model.addAttribute("detail", detail);
        return "admin-batch-detail";
    }
}
