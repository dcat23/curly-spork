package life.macchiato.repair.dto;

public record CreateRepairRequest(
        long youtubeId,
        String deviceName,
        String description,
        double laborCost
) {}
