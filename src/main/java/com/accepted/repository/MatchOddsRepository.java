package com.accepted.repository;

import com.accepted.entity.MatchOdds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchOddsRepository extends JpaRepository<MatchOdds, Long> {

    List<MatchOdds> findByMatchId(Long matchId);
}
