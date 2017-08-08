package pl.sdacademy.dao;

import pl.sdacademy.entity.Match;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by D on 09.05.2017.
 */
public class MatchDao extends AbstractDao<Match> {
    /**
     * Pobiera numer ostatniej rozegranej kolejki.
     * @return
     */
    public int getLastRoundNo() {
        return entities.stream()
                .map(m -> m.getRoundNo())
                .max((rn1, rn2) -> Integer.compare(rn1, rn2))
                .orElse(0);
    }

    /**
     * Pobiera mecze rozgrywane do zadanej rundy, dla zadanej druzyny.
     * @param teamId
     * @param roundNo
     * @return
     */
    public List<Match> getMatchesForTeamTillRound(int teamId, int roundNo) {
        return entities.stream()
                .filter(m ->
                        (m.getTeam1Id() == teamId || m.getTeam2Id() == teamId)
                                && m.getRoundNo() <= roundNo)
                .collect(Collectors.toList());
    }

    @Override
    protected void update(Match entity, Match entityWithNewValues) {
        entity.setRoundNo(entityWithNewValues.getRoundNo());
        entity.setTeam1Goals(entityWithNewValues.getTeam1Goals());
        entity.setTeam2Goals(entityWithNewValues.getTeam2Goals());
        entity.setTeam1Id(entityWithNewValues.getTeam1Id());
        entity.setTeam2Id(entityWithNewValues.getTeam2Id());
    }

    @Override
    protected String createFileLineFromEntity(Match entity) {
        return entity.getId() + ","
                + entity.getRoundNo() + ","
                + entity.getTeam1Id() + ","
                + entity.getTeam1Goals() + ","
                + entity.getTeam2Id() + ","
                + entity.getTeam2Goals();
    }

    @Override
    protected Match copy(Match entity) {
        return new Match(entity);
    }

    @Override
    protected String getFileName() {
        return "match.txt";
    }

    @Override
    protected Match createEntityFromFileLine(String fileLine) {
        String[] lineValues = fileLine.split(",");
        int id = Integer.parseInt(lineValues[0]);
        int roundNo = Integer.parseInt(lineValues[1]);
        int team1Id = Integer.parseInt(lineValues[2]);
        int team1Goals = Integer.parseInt(lineValues[3]);
        int team2Id = Integer.parseInt(lineValues[4]);
        int team2Goals = Integer.parseInt(lineValues[5]);
        return new Match(id, team1Id, team2Id, team1Goals, team2Goals, roundNo);
    }
}
