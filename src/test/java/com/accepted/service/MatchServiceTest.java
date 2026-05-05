package com.accepted.service;

import com.accepted.entity.Match;
import com.accepted.entity.Sport;
import com.accepted.exception.ResourceNotFoundException;
import com.accepted.repository.MatchRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private MatchService matchService;

    @Test
    void findAll_shouldSucceed() {
        Match m = match();
        when(matchRepository.findAll()).thenReturn(List.of(m));

        List<Match> result = matchService.findAll();

        assertThat(result).hasSize(1).contains(m);
    }

    @Test
    void findById_shouldSucceed() {
        Match m = match();
        when(matchRepository.findById(1L)).thenReturn(Optional.of(m));

        assertThat(matchService.findById(1L)).isEqualTo(m);
    }

    @Test
    void findById_notFound_shouldThrow() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void create_savesAndReturns() {
        Match m = match();
        when(matchRepository.save(m)).thenReturn(m);

        assertThat(matchService.create(m)).isEqualTo(m);
        verify(matchRepository).save(m);
    }

    @Test
    void update_updatesFieldsAndSaves() {
        Match existing = match();
        existing.setId(1L);
        Match updated = new Match();
        updated.setDescription("updated");
        updated.setMatchDate(LocalDate.of(2025, 6, 1));
        updated.setMatchTime(LocalTime.of(20, 0));
        updated.setTeamA("Team C");
        updated.setTeamB("Team D");
        updated.setSport(Sport.BASKETBALL);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(matchRepository.save(existing)).thenReturn(existing);

        Match result = matchService.update(1L, updated);

        assertThat(result.getDescription()).isEqualTo("updated");
        assertThat(result.getSport()).isEqualTo(Sport.BASKETBALL);
        verify(matchRepository).save(existing);
    }

    @Test
    void update_notFound_shouldThrow() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.update(99L, match()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_callsDeleteById() {
        Match m = match();
        when(matchRepository.findById(1L)).thenReturn(Optional.of(m));

        matchService.delete(1L);

        verify(matchRepository).deleteById(1L);
    }

    @Test
    void delete_notFound_shouldThrow() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(matchRepository, never()).deleteById(any());
    }

    private Match match() {
        Match m = new Match();
        m.setDescription("Team A vs Team B");
        m.setMatchDate(LocalDate.of(2025, 5, 10));
        m.setMatchTime(LocalTime.of(18, 0));
        m.setTeamA("Team A");
        m.setTeamB("Team B");
        m.setSport(Sport.FOOTBALL);
        return m;
    }
}
