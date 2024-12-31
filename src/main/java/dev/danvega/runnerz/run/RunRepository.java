package dev.danvega.runnerz.run;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface RunRepository extends ListCrudRepository<Run,Integer> {

//    @Query("SELECT * FROM run WHERE location = :location")
    List<Run> findAllByLocation(String location);
}
