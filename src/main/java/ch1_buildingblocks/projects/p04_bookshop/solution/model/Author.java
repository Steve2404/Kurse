package ch1_buildingblocks.projects.p04_bookshop.solution.model;

// public : sinon la classe ne serait visible QUE dans le paquet model (et app ne pourrait pas s'en servir).
public class Author {
    String firstName;
    String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String fullName() {
        return firstName + " " + lastName;
    }

    public String sortName() {
        return lastName + ", " + firstName;
    }
}
