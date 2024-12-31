package dev.danvega.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.TypeReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class RunJsonLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RunJsonLoader.class);
    private final JdbcClientRunRepository runRepository;
    private final ObjectMapper objectMapper;

    public RunJsonLoader(JdbcClientRunRepository jdbcClientRunRepository, ObjectMapper objectMapper) {
        this.runRepository = jdbcClientRunRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if(runRepository.count()==0){
            try(InputStream inputStream= TypeReference.class.getResourceAsStream("/data/runs.json")){
                Runs allRuns = objectMapper.readValue(inputStream, Runs.class);
                log.info("Reading {} runs from JSON data and saving in-memory collection", allRuns.runs().size());
                runRepository.saveAll(allRuns.runs());
            }
            catch (IOException e){
                throw new RuntimeException("Failed to load runs from JSON data", e);
            }
        }else {
            log.info("Not loading Runs from JSON data because the collection contains data");
        }
    }





}
