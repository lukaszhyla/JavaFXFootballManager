package pl.sdacademy.dao;

import pl.sdacademy.entity.Team;
import pl.sdacademy.util.Utils;

import java.time.LocalDate;


public class TeamDao extends AbstractDao<Team> {

    @Override
    protected void update(Team entity, Team entityWithNewValues) {
        entity.setId(entityWithNewValues.getId());
        entity.setName(entityWithNewValues.getName());
        entity.setFoundationDate(entityWithNewValues.getFoundationDate());
        entity.setInfo(entityWithNewValues.getInfo());
    }

    @Override
    protected String createFileLineFromEntity(Team entity) {
        return entity.getId() + ","
                + Utils.encode(entity.getName()) + ","
                + entity.getFoundationDate() + ","
                + Utils.encode(entity.getInfo());
    }

    @Override
    protected Team copy(Team entity) {
        return new Team(entity);
    }

    @Override
    protected String getFileName() {
        return "teams.txt";
    }

    @Override
    protected Team createEntityFromFileLine(String fileLine) {
        String[] lineValues = fileLine.split(",");
        int id = Integer.parseInt(lineValues[0]);
        String name = Utils.decode(lineValues[1]);
        LocalDate foundationDate = LocalDate.parse(lineValues[2]);
        String info = Utils.decode(lineValues[3]);
        return new Team(id, name, foundationDate, info);
    }
}
