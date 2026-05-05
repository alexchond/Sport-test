package com.accepted.service;

import com.accepted.entity.Match;
import com.accepted.exception.ResourceNotFoundException;
import com.accepted.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> findAll() {
        return matchRepository.findAll();
    }

    public Match findById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
    }

    public Match create(Match match) {
        return matchRepository.save(match);
    }

    public Match update(Long id, Match matchData) {
        Match current = findById(id);
        current.setDescription(matchData.getDescription());
        current.setMatchDate(matchData.getMatchDate());
        current.setMatchTime(matchData.getMatchTime());
        current.setTeamA(matchData.getTeamA());
        current.setTeamB(matchData.getTeamB());
        current.setSport(matchData.getSport());
        return matchRepository.save(current);
    }

    public void delete(Long id) {
        findById(id);
        matchRepository.deleteById(id);
    }
}
