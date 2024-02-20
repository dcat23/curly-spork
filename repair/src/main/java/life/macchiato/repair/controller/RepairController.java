package life.macchiato.repair.controller;

import life.macchiato.repair.dto.CreateRepairRequest;
import life.macchiato.repair.dto.RepairStatus;
import life.macchiato.repair.services.RepairService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/repair")
@RequiredArgsConstructor
public class RepairController {

    private final RepairService repairService;

    @GetMapping("/status/{repairId}")
    public ResponseEntity<RepairStatus> repairStatus(@PathVariable("repairId") Long repairId) {
        log.info("New repair status requested: {}", repairId);

        return ResponseEntity.status(200).body(repairService.repairStatus(repairId));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRepair(@RequestBody CreateRepairRequest request) {
        log.info("Create repair request {}", request);
        repairService.createRepair(request);
        return ResponseEntity.status(200).body("Created repair: " + request);
    }
}
