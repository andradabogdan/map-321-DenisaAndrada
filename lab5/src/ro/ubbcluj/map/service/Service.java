package ro.ubbcluj.map.service;
import ro.ubbcluj.map.domain.*;
import ro.ubbcluj.map.domain.graph.AbstractGraph;
import ro.ubbcluj.map.domain.graph.GraphDB;

import ro.ubbcluj.map.domain.validators.PrietenieValidator;
import ro.ubbcluj.map.domain.validators.UtilizatorValidator;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

public class Service {
    private final InMemoryRepository<Long, Utilizator> usersRepo;
    private final InMemoryRepository<Long, Prietenie> friendshipsRepo;


    public Service(InMemoryRepository<Long, Utilizator> repository, InMemoryRepository<Long, Prietenie> repository2, InMemoryRepository<Long, Message> repository3, InMemoryRepository<Long, FriendRequest> repository4) {
        this.usersRepo = repository;
        this.friendshipsRepo = repository2;

    }

    public void saveUtilizator(String firstName, String lastName) {
        Utilizator utilizator = new Utilizator(firstName, lastName);
        this.usersRepo.save(utilizator);
    }

    public void deleteUtilizator(Long id) {
        Iterable<Utilizator> users = this.usersRepo.findAll();
        Utilizator utilizator = this.usersRepo.findOne(id);
        UtilizatorValidator utilizatorValidator = new UtilizatorValidator();
        utilizatorValidator.validate(utilizator);
        for (Utilizator user : users) {
            this.deleteFriend(id, user.getId());
        }
        this.usersRepo.delete(id);
    }



    public Iterable<Prietenie> findAllFriendships() {
        return this.friendshipsRepo.findAll();
    }

    public Iterable<Utilizator> findAll() {
        return this.usersRepo.findAll();
    }


    public void addFriend(Long id1, Long id2, LocalDate data) {
        boolean ok = true;
        if (Objects.equals(id1, id2)) {
            throw new IllegalArgumentException("Un utilizator nu se poate sa se imprieteneasca singur!");
        } else {
            Utilizator utilizator1 = this.usersRepo.findOne(id1);
            Utilizator utilizator2 = this.usersRepo.findOne(id2);
            UtilizatorValidator validator = new UtilizatorValidator();
            validator.validate(utilizator1);
            validator.validate(utilizator2);
            ok = isOk(id2, ok, utilizator1);
            ok = isOk(id1, ok, utilizator2);
            if (ok) {
                utilizator1.makeFriend(utilizator2);
                utilizator2.makeFriend(utilizator1);
                Prietenie prietenie = new Prietenie(id1, id2, data);
                PrietenieValidator prietenieValidator = new PrietenieValidator();
                prietenieValidator.validate(prietenie);
                this.friendshipsRepo.save(prietenie);
            } else {
                throw new IllegalArgumentException("Prietenia exista deja!");
            }
        }
    }

    private boolean isOk(Long id, boolean ok, Utilizator utilizator) {
        for (Utilizator user : utilizator.getFriends()) {
            if (Objects.equals(user.getId(), id)) {
                ok = false;
                break;
            }
        }
        return ok;
    }

    public void deleteFriend(Long id1, Long id2) {
        Iterable<Prietenie> list = this.friendshipsRepo.findAll();
        if (Objects.equals(id1, id2)) {
            throw new IllegalArgumentException("Id-urile trebuie sa fie diferite!");
        }
        for (Prietenie prietenie : list) {
            if ((Objects.equals(prietenie.getId1(), id1) && Objects.equals(prietenie.getId2(), id2))
                    || (Objects.equals(prietenie.getId2(), id1) && Objects.equals(prietenie.getId1(), id2))) {
                this.friendshipsRepo.delete(prietenie.getId());
            }
            else{
                throw new IllegalArgumentException("Prietenia nu exista!");
            }
        }
    }

    public int getNrOfConnectedComponents() {
        AbstractGraph graph;
        graph = new GraphDB(this.usersRepo);
        return graph.getNrOfConnectedComponents();
    }

    public List<Long> getLargestConnectedComponent() {
        AbstractGraph graph;
        graph = new GraphDB(this.usersRepo);
        return graph.getLargestConnectedComponent().stream().toList();
    }

    public Utilizator getById(Long x) {
        return this.usersRepo.findOne(x);
    }

    public List<Prietenie> toolMethod() {
        List<Prietenie> list = new ArrayList<>();
        Iterable<Prietenie> friendships = this.friendshipsRepo.findAll();
        for (Prietenie friendship : friendships) {
            list.add(friendship);
        }
        return list;
    }

    public List<Prietenie> showFriends(Long userID) {
        Predicate<Prietenie> filterCriteria = n -> Objects.equals(n.getId1(), userID) || Objects.equals(n.getId2(), userID);
        List<Prietenie> friendships = toolMethod();

        return friendships.stream()
                .filter(filterCriteria).toList();
    }


}
