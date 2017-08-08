package pl.sdacademy.dao;

import pl.sdacademy.entity.Player;
import pl.sdacademy.entity.Team;
import pl.sdacademy.util.Utils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class PlayerDao extends AbstractDao<Player> {

    public List<Player> getByTeamId(int teamId) {
        return entities.stream()
                .filter(p -> p.getTeamId() == teamId)
                .map(p -> copy(p))
                .collect(Collectors.toList());
    }

    @Override
    protected void update(Player entity, Player entityWithNewValues) {
        entity.setName(entityWithNewValues.getName());
        entity.setSurname(entityWithNewValues.getSurname());
        entity.setDateOfBirth(entityWithNewValues.getDateOfBirth());
        entity.setSpeed(entityWithNewValues.getSpeed());
        entity.setShooting(entityWithNewValues.getShooting());
        entity.setStamina(entityWithNewValues.getStamina());
        entity.setTeamId(entityWithNewValues.getTeamId());
    }

    @Override
    protected String createFileLineFromEntity(Player entity) {
        return entity.getId() + ","
                + Integer.toString(entity.getTeamId()) + ","
                + Utils.encode(entity.getName()) + ","
                + Utils.encode(entity.getSurname()) + ","
                + entity.getDateOfBirth() + ","
                + Integer.toString(entity.getSpeed()) + ","
                + Integer.toString(entity.getShooting()) + ","
                + Integer.toString(entity.getStamina());
    }

    @Override
    protected Player copy(Player entity) {
        return new Player(entity);
    }

    @Override
    protected String getFileName() {
        return "players.txt";
    }

    @Override
    protected Player createEntityFromFileLine(String fileLine) {
        String[] lineValues = fileLine.split(",");
        int id = Integer.parseInt(lineValues[0]);
        int teamId = Integer.parseInt(lineValues[1]);
        String name = lineValues[2];
        String surname = lineValues[3];
        LocalDate dateOfBirth = LocalDate.parse(lineValues[4]);
        int speed = Integer.parseInt(lineValues[5]);
        int shooting = Integer.parseInt(lineValues[6]);
        int stamina = Integer.parseInt(lineValues[7]);
        return new Player(id, teamId, name, surname, dateOfBirth, speed, shooting, stamina);
    }
}
