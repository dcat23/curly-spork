package life.macchiato.repair.services;

import life.macchiato.repair.dto.CreateRepairRequest;
import life.macchiato.repair.dto.RepairStatus;
import life.macchiato.repair.models.Repair;
import life.macchiato.repair.repositories.RepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RepairService {

    @Autowired
    RepairRepository repairRepo;

    public RepairStatus repairStatus(Long repairId) {
        Optional<Repair> byId = repairRepo.findById(repairId);
        return byId.map(Repair::getStatus).orElse(RepairStatus.UNKNOWN);
    }

    public void createRepair(CreateRepairRequest request) {
        Repair repair = Repair.builder()
                .customerId(request.customerId())
                .deviceName(request.deviceName())
                .laborCost(request.laborCost())
                .description(request.description())
                .status(RepairStatus.CREATED)
                .build();

        /* check for valid  */

    }

}
