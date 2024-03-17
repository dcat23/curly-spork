package life.macchiato.courses.dto;

import life.macchiato.courses.model.Torrent;

public record CourseResponse(Long id, String name, String creator, String fileSize, Torrent.Status status) {
}
