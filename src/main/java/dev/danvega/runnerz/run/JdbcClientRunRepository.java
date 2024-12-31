package dev.danvega.runnerz.run;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcClientRunRepository {

    private Logger log = LoggerFactory.getLogger(JdbcClientRunRepository.class);
    private final JdbcClient jdbcClient;

    public JdbcClientRunRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    List<Run>findAll(){
        return jdbcClient.sql("select * from run")
                .query(Run.class).list();
    }

    public Optional<Run>findById(Integer id){
        return jdbcClient.sql("SELECT id, title, started_on, completed_on, miles, location FROM Run WHERE id = :id")
                .param("id",id)
                .query(Run.class)
                .optional();
    }

    public void create(Run run){
        var created = jdbcClient.sql("INSERT INTO Run (id,title, started_on, completed_on, miles, location) values (?,?,?,?,?,?)")
                .params(List.of(run.id(),run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString()))
                .update();

        Assert.state(created == 1, "Failed to create run" + run.title());
    }

    public void update(Run run,Integer id){
        var updated = jdbcClient.sql("update run set title = ?, started_on = ?, completed_on = ?, miles = ?, location = ? where id = ?")
                .params(List.of(run.title(),run.startedOn(),run.completedOn(),run.miles(),run.location().toString(),id))
                .update();

        Assert.state(updated == 1, "Failed to update run" + run.title());

    }

    public void delete(Integer id){
        var deleted = jdbcClient.sql("delete from run where id = :id")
                .param("id",id)
                .update();

        Assert.state(deleted == 1, "Failed to delete run with id" + id);
    }

    public int count() {
        return jdbcClient.sql("select * from run")
                .query().listOfRows().size();
    }

    public void saveAll(List<Run> runs) {
        runs.forEach(this::create);
    }

    public List<Run> findByLocation(Location location) {
        return jdbcClient.sql("select * from run where location = :location")
                .param("location", location.toString())
                .query(Run.class)
                .list();
    }


//    private List<Run> runs=new ArrayList<>();

//    List<Run>findAll(){
//        return runs;
//    }
//
//    Optional<Run> findById(Integer id){
//        return runs.stream()
//                .filter(run -> run.id().equals(id))
//                .findFirst();
//    }
//
//    void create(Run run){
//        runs.add(run);
//    }
//
//    void update(Run run, Integer id){
//        Optional<Run> existingRun = findById(id);
//        if(existingRun.isPresent()){
//            runs.set(runs.indexOf(existingRun.get()),run);
//        }
//    }
//
//    void delete(Integer id){
//        runs.removeIf(run -> run.id().equals(id));
//    }
//
//
//    @PostConstruct
//    private void init(){
//        runs.add(new Run(1, "Monday Morning Run",LocalDateTime.now(), LocalDateTime.now().plus(30, ChronoUnit.HOURS),3, Location.INDOOR));
//        runs.add(new Run(2, "Wednesday Evening Run",LocalDateTime.now(), LocalDateTime.now().plus(60,ChronoUnit.HOURS), 6,Location.INDOOR));
//    }
}
