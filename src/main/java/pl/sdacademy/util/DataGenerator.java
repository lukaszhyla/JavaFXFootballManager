package pl.sdacademy.util;

import pl.sdacademy.dao.DaoManager;
import pl.sdacademy.dao.MatchDao;
import pl.sdacademy.entity.Match;
import pl.sdacademy.entity.Team;

import java.util.*;
import java.util.stream.Collectors;


public class DataGenerator {

    public static List<Match> getRoundMatches(int roundNo) {
        List<Match> matches = DaoManager.MATCH_DAO.getAll();
        List<Integer> teamIds = DaoManager.TEAM_DAO
                .getAll()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        List<Match> thisRoundMatches = new ArrayList<>();

        List<Integer> teamIdsThatDidNotPlayThisRound = new ArrayList<>(teamIds);

        boolean cannotPlayAnotherMatchThisRound = false;
        while (teamIdsThatDidNotPlayThisRound.size() > 0 && !cannotPlayAnotherMatchThisRound) {
            boolean matchWasPlayed = false;
            for(int j = 0; j < teamIdsThatDidNotPlayThisRound.size(); j++) {
                int team1Id = teamIdsThatDidNotPlayThisRound.get(j);
                for(int k = j + 1; k < teamIdsThatDidNotPlayThisRound.size(); k++) {
                    int team2Id = teamIdsThatDidNotPlayThisRound.get(k);
                    if(!checkIfTeamsPlayedMatch(matches, team1Id, team2Id)) {
                        teamIdsThatDidNotPlayThisRound.remove(new Integer(team1Id));
                        teamIdsThatDidNotPlayThisRound.remove(new Integer(team2Id));

                        Match match = new Match(team1Id, team2Id, 0, 0, roundNo);
                        thisRoundMatches.add(match);
                        matches.add(match);
                        matchWasPlayed = true;
                        break;
                    }
                }
                if(matchWasPlayed) {
                    break;
                }
            }
            if(!matchWasPlayed) {
                cannotPlayAnotherMatchThisRound = true;
            }
        }
        return thisRoundMatches;
    }

    private static boolean checkIfTeamsPlayedMatch(List<Match> matches, int team1Id, int team2Id) {
        return matches.stream().anyMatch(m -> m.getTeam1Id() == team1Id && m.getTeam2Id() == team2Id
                || m.getTeam1Id() == team2Id && m.getTeam2Id() == team1Id);
    }
}
