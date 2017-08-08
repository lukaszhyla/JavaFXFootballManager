package pl.sdacademy.entity;

import java.time.LocalDate;


public class Player extends Entity {
    private int teamId;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private int speed;
    private int shooting;
    private int stamina;

    public Player(int teamId, String name, String surname, LocalDate dateOfBirth, int speed, int shooting, int stamina) {
        this.teamId = teamId;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.speed = speed;
        this.shooting = shooting;
        this.stamina = stamina;
    }

    public Player(
            Integer id, int teamId, String name,
            String surname, LocalDate dateOfBirth,
            int speed, int shooting, int stamina) {

        super(id);
        this.teamId = teamId;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.speed = speed;
        this.shooting = shooting;
        this.stamina = stamina;
    }

    public Player(Player player) {
        super(player.getId());
        this.teamId = player.teamId;
        this.name = player.name;
        this.surname = player.surname;
        this.dateOfBirth = player.dateOfBirth;
        this.speed = player.speed;
        this.shooting = player.shooting;
        this.stamina = player.stamina;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setShooting(int shooting) {
        this.shooting = shooting;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getSpeed() {
        return speed;
    }

    public int getShooting() {
        return shooting;
    }

    public int getStamina() {
        return stamina;
    }
}
