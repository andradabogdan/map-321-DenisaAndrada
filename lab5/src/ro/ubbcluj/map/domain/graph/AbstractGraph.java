package ro.ubbcluj.map.domain.graph;

import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;
import ro.ubbcluj.map.domain.Utilizator;

import java.util.*;

public abstract class AbstractGraph {
    protected final Map<Long, List<Long>> adjVertices = new HashMap<>();
    protected List<Long> keyList;

    public AbstractGraph(InMemoryRepository<Long, Utilizator> repository) {
        createGraph(repository);
    }

    public void addVertex(Long id) {
        adjVertices.putIfAbsent(id, new ArrayList<>());
    }

    public List<Long> getAdjVertices(Long id) {
        return adjVertices.get(id);
    }

    public void addEdge(Long id1, Long id2) {
        adjVertices.get(id1).add(id2);
        adjVertices.get(id2).add(id1);
    }

    public abstract void createGraph(InMemoryRepository<Long, Utilizator> repository);

    public Set<Long> breadthFirstTraversal(Long root) {
        Set<Long> visited = new LinkedHashSet<>();
        Queue<Long> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            Long vertex = queue.poll();
            for (Long v : getAdjVertices(vertex)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
        return visited;
    }

    public int getNrOfConnectedComponents() {
        Set<Set<Long>> stringSet = poolOfConnectedComponents();
        return stringSet.size();
    }

    public Set<Long> getLargestConnectedComponent() {
        int max = 0;
        Set<Long> setMax = new HashSet<>();
        Set<Set<Long>> stringSet = poolOfConnectedComponents();

        for (Set<Long> set : stringSet) {
            if (set.size() > max) {
                max = set.size();
                setMax = set;
            }
        }
        return setMax;
    }

    private Set<Set<Long>> poolOfConnectedComponents() {
        Set<Set<Long>> stringSet = new HashSet<>();
        List<String> stringBuilder = new ArrayList<>();
        for (Long key : keyList) {
            stringBuilder.add(key.toString());
        }
        for (String s : stringBuilder) {
            stringSet.add(this.breadthFirstTraversal(Long.parseLong(s)));
        }
        return stringSet;
    }
}
