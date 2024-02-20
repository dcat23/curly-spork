package life.macchiato.repair.repositories;

import life.macchiato.repair.models.Repair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairRepository extends JpaRepository<Repair, Long> {
}
