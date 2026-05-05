package com.accepted.service;

import com.accepted.entity.Match;
import com.accepted.entity.MatchOdds;
import com.accepted.exception.ResourceNotFoundException;
import com.accepted.repository.MatchOddsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchOddsService {

    private final MatchOddsRepository matchOddsRepository;
    private final MatchService matchService;

    public MatchOddsService(MatchOddsRepository matchOddsRepository, MatchService matchService) {
        this.matchOddsRepository = matchOddsRepository;
        this.matchService = matchService;
    }

    public List<MatchOdds> findAll() {
        return matchOddsRepository.findAll();
    }

    public MatchOdds findById(Long id) {
        return matchOddsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MatchOdds not found with id: " + id));
    }

    public List<MatchOdds> getByMatchId(Long matchId) {
        matchService.findById(matchId);
        return matchOddsRepository.findByMatchId(matchId);
    }

    public MatchOdds create(Long matchId, MatchOdds odds) {
        Match match = matchService.findById(matchId);
        odds.setMatch(match);
        return matchOddsRepository.save(odds);
    }

    public MatchOdds update(Long id, MatchOdds updated) {
        MatchOdds existing = findById(id);
        existing.setSpecifier(updated.getSpecifier());
        existing.setOdd(updated.getOdd());
        return matchOddsRepository.save(existing);
    }

    public void delete(Long id) {
        findById(id);
        matchOddsRepository.deleteById(id);
    }
}
