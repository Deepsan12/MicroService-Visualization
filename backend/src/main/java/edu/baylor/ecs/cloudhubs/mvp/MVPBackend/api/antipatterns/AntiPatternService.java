package edu.baylor.ecs.cloudhubs.mvp.MVPBackend.api.antipatterns;

import com.google.common.graph.Graph;
import edu.baylor.ecs.cloudhubs.mvp.MVPBackend.persistence.node.Node;
import edu.baylor.ecs.cloudhubs.mvp.MVPBackend.persistence.graph.MicroserviceGraph;
import edu.baylor.ecs.cloudhubs.mvp.MVPBackend.persistence.patterns.CyclicDependency;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("UnstableApiUsage")
public class AntiPatternService {
    /**
     * Takes a graph of Nodes and links and maps the nodes by labelling them if they are in a cyclic dependency
     * @param graph microservice graph
     * @return same graph but labelled with nodes that are in the dependency
     */
    public MicroserviceGraph labelCyclicDependencies(MicroserviceGraph graph) {
        Objects.requireNonNull(graph);

        // Find the strongly connected components
        Graph<Set<Node>> sccs = graph.findSCCs();
        // Reduce SCCs to only those containing multiple nodes
        List<Set<Node>> cyclicDeps = sccs.nodes().stream().filter(scc -> scc.size() > 1).toList();

        // Iterate over the strongly connected components and add cyclic dependency
        // tags to applicable nodes
        for (int i = 0; i < cyclicDeps.size(); i++) {
            Set<Node> scc = cyclicDeps.get(i);
            int id = i;
            scc.forEach(node -> graph.getNodes().stream().filter(node2 ->
                    node2.filterByName(node.getNodeName())).findFirst().ifPresent(
                            n -> n.addPattern(new CyclicDependency(scc, (long) id))
                    )
            );
        }

        // Return the new graph
        return graph;
    }
}