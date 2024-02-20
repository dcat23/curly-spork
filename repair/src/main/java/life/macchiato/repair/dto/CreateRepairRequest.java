package life.macchiato.repair.dto;

public record CreateRepairRequest(
        long customerId,
        String deviceName,
        String description,
        double laborCost
) {}
