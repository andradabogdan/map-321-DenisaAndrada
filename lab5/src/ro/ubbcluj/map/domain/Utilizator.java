package ro.ubbcluj.map.domain;

import java.util.*;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private final List<Utilizator> friends = new ArrayList<>();

    public Utilizator(){

    }

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public List<Utilizator> getFriends() {
        return this.friends;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String toString() {
        Set<String> stringOfFriends = new HashSet<>();
        for (Utilizator utilizator : friends) {
            String ut = "{" + utilizator.getId() + "; " + utilizator.firstName + "; " + utilizator.lastName + "} ";
            stringOfFriends.add(ut);
        }

        return "Utilizatorul{id: " + this.id + ", firstname: " + this.firstName + ", lastname: " + this.lastName + ", friends: " + stringOfFriends + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    public void makeFriend(Utilizator utilizator) {
        this.friends.add(utilizator);
    }

    public List<Long> getFriendIds() {
        List<Long> listIds = new ArrayList<>();
        for (Utilizator utilizator : friends) {
            listIds.add(utilizator.getId());
        }
        return listIds;
    }
}

