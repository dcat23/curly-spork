package life.macchiato.repair.models;

import jakarta.persistence.*;
import life.macchiato.repair.dto.RepairStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Repair {

    @Id
    @SequenceGenerator(
            name = "repair_id_sequence",
            sequenceName = "repair_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "repair_id_sequence"
    )
    private Long id;
    @Enumerated
    private RepairStatus status;
    private Long youtubeId;
    private String deviceName;
    private String description;
    private Double laborCost;
    private LocalDate createdAt;

}
