package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.CerereValidator;
import ro.ubbcluj.map.domain.validators.MesajValidator;
import ro.ubbcluj.map.domain.validators.PrietenieValidator;
import ro.ubbcluj.map.domain.validators.UtilizatorValidator;
import ro.ubbcluj.map.repository.db.CereriDbRepository;
import ro.ubbcluj.map.repository.db.MesajeDbRepository;
import ro.ubbcluj.map.repository.db.PrietenieDbRepository;
import ro.ubbcluj.map.repository.db.UtilizatorDbRepository;
import ro.ubbcluj.map.repository.memory.InMemoryRepository;
import ro.ubbcluj.map.service.Service;
import ro.ubbcluj.map.ui.UI;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        InMemoryRepository<Long, Utilizator> usersRepo = new UtilizatorDbRepository(new UtilizatorValidator(), "jdbc:postgresql://localhost:5432/lab5", "postgres", "Postgres123");
        InMemoryRepository<Long, Prietenie> friendshipsRepo = new PrietenieDbRepository(new PrietenieValidator(), "jdbc:postgresql://localhost:5432/lab5", "postgres", "Postgres123");
        InMemoryRepository<Long, Message> messagesRepo = new MesajeDbRepository(new MesajValidator(), "jdbc:postgresql://localhost:5432/lab5", "postgres", "Postgres123");
        InMemoryRepository<Long, FriendRequest> friendRequestsRepo = new CereriDbRepository(new CerereValidator(), "jdbc:postgresql://localhost:5432/lab5", "postgres", "Postgres123");
        Service serviceDb = new Service(usersRepo, friendshipsRepo, messagesRepo, friendRequestsRepo);
        UI uidb = new UI(serviceDb);
        uidb.menu();
    }
}
