package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.*;
import ro.ubbcluj.map.domain.graph.AbstractGraph;
import ro.ubbcluj.map.domain.graph.GraphDB;
import ro.ubbcluj.map.domain.validators.CerereValidator;
import ro.ubbcluj.map.domain.validators.MesajValidator;
import ro.ubbcluj.map.domain.validators.PrietenieValidator;
import ro.ubbcluj.map.domain.validators.UtilizatorValidator;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;

public class Service {
    private final InMemoryRepository<Long, Utilizator> usersRepo;
    private final InMemoryRepository<Long, Prietenie> friendshipsRepo;
    private final InMemoryRepository<Long, Message> messagesRepo;
    private final InMemoryRepository<Long, FriendRequest> friendRequestsRepo;

    public Service(InMemoryRepository<Long, Utilizator> repository, InMemoryRepository<Long, Prietenie> repository2, InMemoryRepository<Long, Message> repository3, InMemoryRepository<Long, FriendRequest> repository4) {
        this.usersRepo = repository;
        this.friendshipsRepo = repository2;
        this.messagesRepo = repository3;
        this.friendRequestsRepo = repository4;
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

    public void saveMessage(Long from_user, Long to_user, String message, LocalDate data, Long reply) {
        Message currentMessage = new Message(from_user, to_user, message, data, reply);
        MesajValidator mesajValidator = new MesajValidator();
        mesajValidator.validate(currentMessage);
        List<Message> messages = this.showConversation(from_user,to_user);
        Message lastMessage = new Message();
        int i=0;
        for(Message mesaj: messages){
            if(i==messages.size()-1){
                lastMessage = mesaj;
            }
            i++;
        }
        for(Message mesaj : messages){
            if(Objects.equals(mesaj.getTo(), from_user) && Objects.equals(mesaj.getFrom(), to_user)
                    && currentMessage.getData().compareTo(mesaj.getData())<0){
                throw new IllegalArgumentException("Mesajul adaugat trebuie sa fie cronologic dupa mesajul la care se raspunde!");
            }
        }
        if(Objects.equals(lastMessage.getTo(), currentMessage.getTo())
                && Objects.equals(lastMessage.getFrom(), currentMessage.getFrom())){
            currentMessage.setReply(0L);
        }
        else{
            currentMessage.setReply(lastMessage.getId());
        }

        this.messagesRepo.save(currentMessage);
    }

    public void deleteMessage(Long id) {
        Message message = this.messagesRepo.findOne(id);
        MesajValidator mesajValidator = new MesajValidator();
        mesajValidator.validate(message);
        this.messagesRepo.delete(id);
    }

    public Iterable<Message> findAllMessages() {
        return this.messagesRepo.findAll();
    }

    public Iterable<Prietenie> findAllFriendships() {
        return this.friendshipsRepo.findAll();
    }

    public Iterable<Utilizator> findAll() {
        return this.usersRepo.findAll();
    }

    public void saveRequests(Long id1, Long id2, String status){

        Iterable<Prietenie> prietenii = this.friendshipsRepo.findAll();
        for(Prietenie prietenie : prietenii){
            if((Objects.equals(prietenie.getId1(), id1) && Objects.equals(prietenie.getId2(), id2))
                    ||(Objects.equals(prietenie.getId1(), id2) && Objects.equals(prietenie.getId2(), id1))){
                throw new IllegalArgumentException("Cererea nu se poate trimite! Prietenia exista deja!");
            }
            if (Objects.equals(id1, id2))
                throw new IllegalArgumentException("Un utilizator nu se poate sa se imprieteneasca singur!");


            FriendRequest friendRequest = new FriendRequest(id1, id2, status);
            CerereValidator validator = new CerereValidator();
            validator.validate(friendRequest);
            this.friendRequestsRepo.save(friendRequest);
        }
    }

    public void deleteRequests(Long id) {
        FriendRequest friendRequest = this.friendRequestsRepo.findOne(id);
        CerereValidator cerereValidator = new CerereValidator();
        cerereValidator.validate(friendRequest);
        this.friendRequestsRepo.delete(id);
    }

    public void updateStatus(Long id, Status status) {
        FriendRequest friendRequest = this.friendRequestsRepo.findOne(id);
        if(status==Status.REJECTED){
            deleteRequests(id);
        }
        else if(status==Status.APPROVED){
            deleteRequests(id);
            this.addFriend(friendRequest.getId1(),friendRequest.getId2(),LocalDate.now());
        }
    }

    public Iterable<FriendRequest> findAllRequests() {
        return this.friendRequestsRepo.findAll();
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

    public List<Prietenie> showFriendsWithDate(Long userID, int month) {
        if (month >= 1 && month <= 12) {
            Predicate<Prietenie> filterCriteria1 = n -> Objects.equals(n.getId1(), userID)
                    || Objects.equals(n.getId2(), userID);
            Predicate<Prietenie> filterCriteria2 = n -> n.getData().getMonthValue() == month;
            Predicate<Prietenie> filterCriteria = filterCriteria1.and(filterCriteria2);
            List<Prietenie> friendships = toolMethod();

            return friendships.stream()
                    .filter(filterCriteria).toList();
        } else {
            throw new IllegalArgumentException("Luna trebuie sa fie un numar cuprins intre 1 si 12!");
        }
    }

    public List<Message> showConversation(Long id1, Long id2) {
        Iterable<Message> messages = this.messagesRepo.findAll();
        List<Message> conversation = new ArrayList<>();
        for(Message message : messages){
            if((Objects.equals(message.getFrom(), id1) && Objects.equals(message.getTo(), id2))
                    ||(Objects.equals(message.getFrom(), id2) && Objects.equals(message.getTo(), id1))){
                conversation.add(message);
            }
        }
        Comparator<Message> mapComparator = (Message m1, Message m2) -> m1.getData().compareTo(m2.getData());
        conversation.sort(mapComparator);
        return conversation;
    }
}
