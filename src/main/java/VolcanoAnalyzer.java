import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
        return volcanos.stream().min(Comparator.comparing(Volcano::getDEATHS)).orElseThrow(null);
    }

    public Double causedTsunami() {
        double x = volcanos.stream().filter(v -> v.getTsu().equals("tsu")).count();
        return x / volcanos.size() * 100;
    }

    public String mostCommonType() {
        return volcanos.stream()
                .collect(Collectors.groupingBy(Volcano::getType, Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Double percentNorth() {
        double x = volcanos.stream().filter(v -> v.getLatitude() > 0).count();
        return x / volcanos.size() * 100;
    }

    public Long eruptionsByCountry(String Country) {
        return volcanos.stream().filter(v -> v.getCountry().equalsIgnoreCase(Country)).count();
    }

    public Double averageElevation() {
        return volcanos.stream().mapToDouble(Volcano::getElevation).average().orElseThrow(null);
    }

    public String[] volcanoTypes() {
        return volcanos.stream().map(Volcano::getType).distinct().toArray(String[]::new);
    }

    public String[] manyFilters() {
        return volcanos.stream()
                .filter(v -> v.getYear() > 1800).filter(v -> v.getTsu().equals("")).filter(v -> v.getLatitude() < 0)
                .filter(v -> v.getVEI() == 5)
                .map(Volcano::getName).toArray(String[]::new);
    }

    public String[] elevatedVolcanoes(double x) {
        return volcanos.stream().filter(v -> v.getElevation() > x).map(Volcano::getName).toArray(String[]::new);
    }

    public String[] topAgentsOfDeath() {

    }

}
