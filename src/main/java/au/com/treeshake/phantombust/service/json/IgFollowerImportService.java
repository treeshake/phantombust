package au.com.treeshake.phantombust.service.json;

import au.com.treeshake.phantombust.dto.IgExportFollower;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class IgFollowerImportService {

    private final ObjectMapper objectMapper;

    public IgFollowerImportService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<IgExportFollower> importFollowers(File jsonFile) throws IOException {
        // Parse the JSON file into a list of IgExportFollower DTOs
        return objectMapper.readValue(jsonFile, new TypeReference<>() {
        });
    }
}