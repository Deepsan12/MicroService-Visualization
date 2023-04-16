package edu.baylor.ecs.cloudhubs.mvp.MVPBackend.api.antipatterns;

import edu.baylor.ecs.cloudhubs.mvp.MVPBackend.persistence.graph.MicroserviceGraph;
import edu.baylor.ecs.cloudhubs.mvp.MVPBackend.persistence.graph.GraphModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/anti-pattern")
@CrossOrigin(origins={"http://localhost:3000"}, maxAge = 3600)
public class AntiPatternController {
    protected final AntiPatternService antiPatternService;

    /**
     * Labels the provided graph with cyclic dependencies
     * @return the labelled graph
     */
    @GetMapping("/cyclic")
    public ResponseEntity<MicroserviceGraph> getCyclicDependencies(@RequestBody GraphModel graphModel) {
        MicroserviceGraph resultGraph = antiPatternService.labelCyclicDependencies(graphModel.toGraph());
        return ResponseEntity.ok(resultGraph);
    }

    /**
     * Labels the provided graph with bottlenecks
     * @return the labelled graph
     */
    @GetMapping("/bottleneck")
    public ResponseEntity<MicroserviceGraph> getBottleneck(@RequestBody GraphModel graphModel, @RequestParam Integer threshold) {
        MicroserviceGraph resultGraph = antiPatternService.labelBottlenecks(graphModel.toGraph(), threshold);
        return ResponseEntity.ok(resultGraph);
    }
}
