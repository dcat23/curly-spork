package life.macchiato.media.config;

import life.macchiato.media.dto.DownloadStatus;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class DownloadStatusEnumConverter implements Converter<String, DownloadStatus> {

    @Override
    public DownloadStatus convert(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        return DownloadStatus.valueOf(source.toUpperCase());
    }

}
