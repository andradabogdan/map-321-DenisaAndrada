package ro.ubbcluj.map.domain.graph;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

import java.util.*;

public class GraphDB extends AbstractGraph {

    public GraphDB(InMemoryRepository<Long, Utilizator> repository) {
        super(repository);
    }

    public void createGraph(InMemoryRepository<Long, Utilizator> repo) {
        Iterable<Utilizator> users = repo.findAll();
        keyList = new ArrayList<>();
        for (Utilizator utilizator : users) {
            keyList.add(utilizator.getId());
        }
        for (Utilizator utilizator : users) {
            addVertex(utilizator.getId());
        }
        for (Utilizator utilizator : users) {
            List<Utilizator> friends = utilizator.getFriends();
            for (Utilizator utilizator1 : friends) {
                addEdge(utilizator.getId(), utilizator1.getId());
            }
        }
    }

}
