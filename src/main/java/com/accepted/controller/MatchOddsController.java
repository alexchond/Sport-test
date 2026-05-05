package com.accepted.controller;

import com.accepted.entity.MatchOdds;
import com.accepted.service.MatchOddsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match-odds")
@Tag(name = "MatchOdds")
public class MatchOddsController {

    private final MatchOddsService matchOddsService;

    public MatchOddsController(MatchOddsService matchOddsService) {
        this.matchOddsService = matchOddsService;
    }

    @GetMapping
    public ResponseEntity<List<MatchOdds>> getOdds() {
        return ResponseEntity.ok(matchOddsService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "odds by id")
    public ResponseEntity<MatchOdds> getOddsById(@PathVariable Long id) {
        return ResponseEntity.ok(matchOddsService.findById(id));
    }

    @GetMapping("/match/{matchId}")
    @Operation(summary = "get odds for a match")
    public ResponseEntity<List<MatchOdds>> getOddsByMatch(@PathVariable Long matchId) {
        return ResponseEntity.ok(matchOddsService.getByMatchId(matchId));
    }

    @PostMapping
    @Operation(summary = "add odds to match")
    public ResponseEntity<MatchOdds> create(@RequestParam Long matchId, @RequestBody MatchOdds odds) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchOddsService.create(matchId, odds));
    }

    @PutMapping("/{id}")
    @Operation(summary = "update odds")
    public ResponseEntity<MatchOdds> update(@PathVariable Long id, @RequestBody MatchOdds odds) {
        return ResponseEntity.ok(matchOddsService.update(id, odds));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        matchOddsService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
