package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.Prietenie;
import ro.ubbcluj.map.domain.Status;
import ro.ubbcluj.map.domain.Utilizator;
import ro.ubbcluj.map.domain.validators.ValidationException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import ro.ubbcluj.map.service.Service;

public class UI {
    private final Service service;

    public UI(Service service) {
        this.service = service;
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Meniul de optiuni: ");
        System.out.println("================================================================");
        System.out.println("1. Adaugare utilizator");
        System.out.println("2. Stergere utilizator");
        System.out.println("3. Afisare utilizatori");
        System.out.println("----------------------------------------------------------------");
        System.out.println("4. Adaugare prieten");
        System.out.println("5. Stergere prieten");
        System.out.println("6. Determinarea numarului de comunitati");
        System.out.println("7. Determinarea celei mai sociabile comunitati");
        System.out.println("8. Afisarea prieteniilor unui utilizator dat");
        System.out.println("9. Afisare prietenii");
        System.out.println("10. Afisarea prieteniilor unui utilizator dat dintr-o luna data");
        System.out.println("----------------------------------------------------------------");
        System.out.println("11. Adaugare mesaj");
        System.out.println("12. Stergere mesaj");
        System.out.println("13. Afisare mesaje");
        System.out.println("14. Afisati conversatiile a 2 utilizatori ordonate cronologic");
        System.out.println("----------------------------------------------------------------");
        System.out.println("15. Trimite cerere de prietenie");
        System.out.println("16. Retragere cerere de prietenie");
        System.out.println("17. Modificarea statusului unei cereri de prietenie");
        System.out.println("18. Afisare cereri de prietenie");
        System.out.println("----------------------------------------------------------------");
        System.out.println("19. Iesire");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Selectati optiunea dorita: ");
    }

    private void addUtilizatorUI() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Introduceti prenumele si numele utilizatorului de adaugat: ");
            String firstName = scanner.nextLine();
            String lastName = scanner.nextLine();
            this.service.saveUtilizator(firstName, lastName);
            System.out.println("Utilizatorul a fost adaugat!");
        } catch (ValidationException var4) {
            System.out.println(var4.getMessage());
        }

    }

    private void addFriendUI() {
        this.printAllUI();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Introduceti id-ul primului utilizator: ");
            Long id1 = scanner.nextLong();
            System.out.println("Introduceti id-ul celui de al doilea utilizator: ");
            Long id2 = scanner.nextLong();
            System.out.println("Introduceti data la care se realizeaza prietenia (yyyy-MM-dd): ");
            this.service.addFriend(id1, id2, addDate());
            System.out.println("Prietenia s-a creat cu succes!");
        } catch (ValidationException | IllegalArgumentException | NullPointerException var4) {
            System.out.println(var4.getMessage());
        }

    }

    public static LocalDate addDate() {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(str, dtf);
    }

    private void deleteFriendUI() {
        this.printAllUI();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Introduceti id-ul primului utilizator: ");
            Long id1 = scanner.nextLong();
            System.out.println("Introduceti id-ul celui de al doilea utilizator: ");
            Long id2 = scanner.nextLong();
            this.service.deleteFriend(id1, id2);
        } catch (ValidationException | IllegalArgumentException | NullPointerException var4) {
            System.out.println(var4.getMessage());
        }

    }

    private void deleteUtilizatorUI() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduceti id-ul utilizatorului de sters: ");
            Long id = scanner.nextLong();
            this.service.deleteUtilizator(id);
            System.out.println("Utilizatorul a fost sters!");
        } catch (ValidationException | IllegalArgumentException var3) {
            System.out.println(var3.getMessage());
        }

    }

    private void printAllUI() {
        Iterable<Utilizator> users = this.service.findAll();
        Iterator var2 = users.iterator();

        while(var2.hasNext()) {
            Utilizator user = (Utilizator)var2.next();
            System.out.println(user.toString());
        }

    }

    private void printAllFriendshipsUI() {
        Iterable<Prietenie> friendships = this.service.findAllFriendships();
        Iterator var2 = friendships.iterator();

        while(var2.hasNext()) {
            Prietenie p = (Prietenie)var2.next();
            System.out.println(p.toString());
        }

    }

    private void getNrOfConnectedComponentsUI() {
        int nr = this.service.getNrOfConnectedComponents();
        System.out.println("Numarul de comunitati este: " + nr);
    }

    private void getLargestConnectedComponentUI() {
        System.out.println("Cea mai sociabila comunitate este:");
        List<Long> largestConnectedComponent = this.service.getLargestConnectedComponent();
        Iterator var2 = largestConnectedComponent.iterator();

        while(var2.hasNext()) {
            Long id = (Long)var2.next();
            System.out.println(id + ": " + this.service.getById(id).getLastName() + " " + this.service.getById(id).getFirstName());
        }

    }

    public void menu() {
        Scanner sc = new Scanner(System.in);
        boolean ok = true;

        while(ok) {
            this.printMenu();
            int op = sc.nextInt();
            if (op == 1) {
                this.addUtilizatorUI();
            } else if (op == 2) {
                this.deleteUtilizatorUI();
            } else if (op == 3) {
                this.printAllUI();
            } else if (op == 4) {
                this.addFriendUI();
            } else if (op == 5) {
                this.deleteFriendUI();
            } else if (op == 6) {
                this.getNrOfConnectedComponentsUI();
            } else if (op == 7) {
                this.getLargestConnectedComponentUI();
            } else if (op == 8) {
                this.showFriendsUI();
            } else if (op == 9) {
                this.printAllFriendshipsUI();
            } else if (op == 10) {
                this.showFriendsWithDateUI();
            } else if (op == 11) {
                this.addMessageUI();
            } else if (op == 12) {
                this.deleteMessageUI();
            } else if (op == 13) {
                this.printAllMessagesUI();
            } else if (op == 14) {
                this.showConversationsUI();
            } else if (op == 15) {
                this.sendFriendRequestUI();
            } else if (op == 16) {
                this.deleteFriendRequestUI();
            } else if (op == 17) {
                this.updateStatusUI();
            } else if (op == 18) {
                this.printAllFriendRequestsUI();
            } else if (op == 19) {
                ok = false;
            } else {
                System.out.println("Optiunea nu exista! Reincercati!");
            }
        }

    }

    private void showFriendsUI() {
        try {
            System.out.println("Dati id-ul utilizatorului: ");
            Scanner scanner = new Scanner(System.in);
            Long id = scanner.nextLong();
            List<Prietenie> list = this.service.showFriends(id);
            Utilizator utilizator = new Utilizator();
            System.out.println("Lista de prieteni: ");
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                Prietenie friendship = (Prietenie)var5.next();
                if (!Objects.equals(friendship.getId1(), id)) {
                    utilizator = this.service.getById(friendship.getId1());
                }

                if (!Objects.equals(friendship.getId2(), id)) {
                    utilizator = this.service.getById(friendship.getId2());
                }

                PrintStream var10000 = System.out;
                String var10001 = utilizator.getFirstName();
                var10000.println("FirstName: " + var10001 + " LastName: " + utilizator.getLastName() + " Data: " + friendship.getData());
            }
        } catch (ValidationException | NullPointerException | IllegalArgumentException var7) {
            System.out.println(var7.getMessage());
        }

    }

    private void showFriendsWithDateUI() {
        try {
            System.out.println("Dati id-ul utilizatorului: ");
            Scanner scanner = new Scanner(System.in);
            Long id = scanner.nextLong();
            System.out.println("Dati luna anului(1-12): ");
            scanner = new Scanner(System.in);
            int month = scanner.nextInt();
            List<Prietenie> friendships = this.service.showFriendsWithDate(id, month);
            Utilizator utilizator = new Utilizator();
            System.out.println("Lista de prieteni: ");
            Iterator var6 = friendships.iterator();

            while(var6.hasNext()) {
                Prietenie friendship = (Prietenie)var6.next();
                if (!Objects.equals(friendship.getId1(), id)) {
                    utilizator = this.service.getById(friendship.getId1());
                }

                if (!Objects.equals(friendship.getId2(), id)) {
                    utilizator = this.service.getById(friendship.getId2());
                }

                PrintStream var10000 = System.out;
                String var10001 = utilizator.getFirstName();
                var10000.println("FirstName: " + var10001 + " LastName: " + utilizator.getLastName() + " Data: " + friendship.getData());
            }
        } catch (ValidationException | NullPointerException | IllegalArgumentException var8) {
            System.out.println(var8.getMessage());
        }

    }
    private void printAllMessagesUI() {
        Iterable<Message> messages = this.service.findAllMessages();
        for (Message message : messages) {
            System.out.println(message.toString());
        }
    }

    private void deleteMessageUI() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Introduceti id-ul mesajului de sters: ");
            Long id = scanner.nextLong();
            this.service.deleteMessage(id);
            System.out.println("Mesajul a fost sters!");
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addMessageUI() {
        this.printAllUI();
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Introduceti mesajul: ");
            String message = scanner.nextLine();
            System.out.println("Introduceti data (yyyy-mm-dd): ");
            String data = scanner.nextLine();
            System.out.println("Introduceti id-ul utilizatorului care trimite mesajul (from): ");
            Long userID = scanner.nextLong();
            System.out.println("Introduceti id-ul utilizatorilor la care trimiteti mesajul (to): ");
            Long toID = scanner.nextLong();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateTime = LocalDate.parse(data, formatter);

            this.service.saveMessage(userID, toID, message, dateTime, 0L);
            System.out.println("Mesajul a fost trimis!");
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
    private void showConversationsUI() {
        try {
            System.out.println("Dati id-ul primului utilizator: ");
            Scanner scanner = new Scanner(System.in);
            Long id1 = scanner.nextLong();
            System.out.println("Dati id-ul celui de al doilea utilizator: ");
            scanner = new Scanner(System.in);
            Long id2 = scanner.nextLong();
            System.out.println("Conversatia intre utilizatorul cu id-ul " + id1 + " si utilizatorul cu id-ul " + id2);
            System.out.println("----------------------------------------------------------------------------------");
            List<Message> conversation = this.service.showConversation(id1, id2);
            for (Message message : conversation) {
                System.out.println(message.toString());
            }
        } catch (IllegalArgumentException | ValidationException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printAllFriendRequestsUI() {
        Iterable<FriendRequest> friendRequests = this.service.findAllRequests();
        for(FriendRequest friendRequest : friendRequests){
            System.out.println(friendRequest.toString());
        }
    }
    private void updateStatusUI() {
        this.printAllFriendRequestsUI();
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Introduceti noul status al cererii(approved,rejected,pending):");
            String stringStatus = scanner.nextLine();
            System.out.println("Introduceti id-ul cererii de modificat: ");
            Long id = scanner.nextLong();
            Status status;
            if(Objects.equals(stringStatus, "approved")){
                status = Status.APPROVED;
            }
            else if(Objects.equals(stringStatus, "rejected")){
                status = Status.REJECTED;
            }
            else if(Objects.equals(stringStatus, "pending")){
                status = Status.PENDING;
            }
            else{
                throw new IllegalArgumentException ("Statusul trebuie sa fie unul dintre [approved, rejected, pending]!");
            }
            this.service.updateStatus(id, status);
        } catch (NullPointerException | ValidationException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteFriendRequestUI() {
        this.printAllFriendRequestsUI();
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Introduceti id-ul cererii de sters: ");
            Long id = scanner.nextLong();
            this.service.deleteRequests(id);
        } catch (NullPointerException | ValidationException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendFriendRequestUI() {
        this.printAllUI();
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Introduceti id-ul primului utilizator: ");
            Long id1 = scanner.nextLong();
            System.out.println("Introduceti id-ul celui de al doilea utilizator: ");
            Long id2 = scanner.nextLong();
            this.service.saveRequests(id1,id2,"pending");
            System.out.println("Cererea a fost trimisa cu succes!");
        } catch (NullPointerException | ValidationException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
