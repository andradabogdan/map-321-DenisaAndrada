package ro.ubbcluj.map.domain.graph;


import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

import java.util.*;

public class Graph extends AbstractGraph {


    public Graph(InMemoryRepository<Long, Utilizator> repository) {
        super(repository);
    }

    public void createGraph(InMemoryRepository<Long, Utilizator> repository) {
        keyList = new ArrayList<>(repository.getEntities().keySet());

        for (Long key : keyList) {
            addVertex(key);
        }
        for (Utilizator utilizator : repository.getEntities().values()) {
            List<Utilizator> friends = utilizator.getFriends();
            for (Utilizator utilizator1 : friends) {
                addEdge(utilizator.getId(), utilizator1.getId());
            }
        }
    }


}
