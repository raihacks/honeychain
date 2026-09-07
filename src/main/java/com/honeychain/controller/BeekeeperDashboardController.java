package com.honeychain.controller;

import com.honeychain.dto.*;
import com.honeychain.entity.BatchStatus;
import com.honeychain.repository.BatchRepository;
import com.honeychain.repository.BeekeeperRepository;
import com.honeychain.repository.HiveRepository;
import com.honeychain.service.BatchService;
import com.honeychain.service.BeekeeperService;
import com.honeychain.service.HiveService;
import com.honeychain.service.SensorReadingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class BeekeeperDashboardController {

    private final BeekeeperRepository beekeeperRepository;
    private final HiveRepository hiveRepository;
    private final BatchRepository batchRepository;
    private final BeekeeperService beekeeperService;
    private final HiveService hiveService;
    private final BatchService batchService;
    private final SensorReadingService sensorReadingService;

    public BeekeeperDashboardController(BeekeeperRepository beekeeperRepository,
                                         HiveRepository hiveRepository,
                                         BatchRepository batchRepository,
                                         BeekeeperService beekeeperService,
                                         HiveService hiveService,
                                         BatchService batchService,
                                         SensorReadingService sensorReadingService) {
        this.beekeeperRepository = beekeeperRepository;
        this.hiveRepository = hiveRepository;
        this.batchRepository = batchRepository;
        this.beekeeperService = beekeeperService;
        this.hiveService = hiveService;
        this.batchService = batchService;
        this.sensorReadingService = sensorReadingService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("beekeepers", beekeeperRepository.findAll());
        model.addAttribute("hives", hiveRepository.findAll());
        model.addAttribute("batches", batchRepository.findAll());
        model.addAttribute("batchStatuses", BatchStatus.values());

        model.addAttribute("beekeeperForm", new RegisterBeekeeperRequest());
        model.addAttribute("hiveForm", new RegisterHiveRequest());
        model.addAttribute("batchForm", new CreateBatchRequest());
        model.addAttribute("sensorForm", new AddSensorReadingRequest());

        return "dashboard";
    }

    @PostMapping("/beekeepers")
    public String registerBeekeeper(@ModelAttribute RegisterBeekeeperRequest beekeeperForm,
                                     RedirectAttributes redirectAttributes) {
        try {
            beekeeperService.register(beekeeperForm);
            redirectAttributes.addFlashAttribute("message",
                    "Registered beekeeper: " + beekeeperForm.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/hives")
    public String registerHive(@ModelAttribute RegisterHiveRequest hiveForm,
                                RedirectAttributes redirectAttributes) {
        try {
            hiveService.register(hiveForm);
            redirectAttributes.addFlashAttribute("message",
                    "Registered hive: " + hiveForm.getHiveCode());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/batches")
    public String createBatch(@ModelAttribute CreateBatchRequest batchForm,
                               RedirectAttributes redirectAttributes) {
        try {
            batchService.createBatch(batchForm);
            redirectAttributes.addFlashAttribute("message",
                    "Created batch: " + batchForm.getBatchCode());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/batches/{id}/advance")
    public String advanceBatch(@PathVariable Long id,
                                @RequestParam BatchStatus newStatus,
                                RedirectAttributes redirectAttributes) {
        try {
            batchService.transitionStatus(id, newStatus);
            redirectAttributes.addFlashAttribute("message",
                    "Batch #" + id + " moved to " + newStatus);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/sensor-readings")
    public String addSensorReading(@ModelAttribute AddSensorReadingRequest sensorForm,
                                    RedirectAttributes redirectAttributes) {
        try {
            sensorReadingService.add(sensorForm);
            redirectAttributes.addFlashAttribute("message", "Sensor reading logged.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
