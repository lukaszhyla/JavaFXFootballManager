package pl.sdacademy.entity;

import java.time.LocalDate;

public class Team extends Entity {
    private String name;
    private LocalDate foundationDate;
    private String info;

    public Team(String name, LocalDate foundationDate, String info) {
        this.name = name;
        this.foundationDate = foundationDate;
        this.info = info;
    }

    public Team(Integer id, String name, LocalDate foundationDate, String info) {
        super(id);
        this.name = name;
        this.foundationDate = foundationDate;
        this.info = info;
    }

    public Team(Team team) {
        super(team.getId());
        this.name = team.name;
        this.foundationDate = team.foundationDate;
        this.info = team.info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFoundationDate(LocalDate foundationDate) {
        this.foundationDate = foundationDate;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public LocalDate getFoundationDate() {
        return foundationDate;
    }

    public String getInfo() {
        return info;
    }
}
