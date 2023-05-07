import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class VolcanoAnalyzer {
    private List<Volcano> volcanos;

    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try {
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString,
                    typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch (Exception e) {
            throw (e);
        }
    }

    public Integer numbVolcanoes() {
        return volcanos.size();
    }

    public List<Volcano> eruptedInEighties() {
        return volcanos.stream()
                .filter(n -> n.getYear() >= 1980 && n.getYear() <= 1989)
                .collect(Collectors.toList());
    }

    public String[] highVEI() {
        return volcanos.stream().filter(v -> v.getVEI() >= 6).map(Volcano::getName).toArray(String[]::new);
    }

    public Volcano mostDeadly() {
        return volcanos.stream().max(Comparator.comparing(Volcano::getDEATHS)).orElseThrow(null);
    }
    // add methods here to meet the requirements in README.md

}
