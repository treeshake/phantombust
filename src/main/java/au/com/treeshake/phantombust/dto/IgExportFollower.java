package au.com.treeshake.phantombust.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Exported data from IG (App) Official
 */
@Data
public class IgExportFollower {
    private String title;

    @JsonProperty("media_list_data")
    private List<Object> mediaListData;

    @JsonProperty("string_list_data")
    private List<StringListData> stringListData;

    @Data
    public static class StringListData {
        private String href;
        private String value;
        private long timestamp;
    }
}