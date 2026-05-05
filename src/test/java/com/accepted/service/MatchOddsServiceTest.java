package com.accepted.service;

import com.accepted.entity.Match;
import com.accepted.entity.MatchOdds;
import com.accepted.exception.ResourceNotFoundException;
import com.accepted.repository.MatchOddsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchOddsServiceTest {

    @Mock
    private MatchOddsRepository matchOddsRepository;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchOddsService matchOddsService;

    @Test
    void findAll_shouldSucceed() {
        MatchOdds odds = odds();
        when(matchOddsRepository.findAll()).thenReturn(List.of(odds));

        assertThat(matchOddsService.findAll()).containsExactly(odds);
    }

    @Test
    void findById_shouldSucceed() {
        MatchOdds odds = odds();
        when(matchOddsRepository.findById(1L)).thenReturn(Optional.of(odds));

        assertThat(matchOddsService.findById(1L)).isEqualTo(odds);
    }

    @Test
    void findById_notFound_shouldThrows() {
        when(matchOddsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> matchOddsService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getByMatchId_validatesMatchExistsFirst() {
        Match match = new Match();
        when(matchService.findById(1L)).thenReturn(match);
        when(matchOddsRepository.findByMatchId(1L)).thenReturn(List.of(odds()));

        List<MatchOdds> result = matchOddsService.getByMatchId(1L);

        assertThat(result).hasSize(1);
        verify(matchService).findById(1L);
    }

    @Test
    void getByMatchId_matchNotFound_shouldThrow() {
        when(matchService.findById(99L)).thenThrow(new ResourceNotFoundException("Match not found with id: 99"));

        assertThatThrownBy(() -> matchOddsService.getByMatchId(99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(matchOddsRepository, never()).findByMatchId(any());
    }

    @Test
    void create_setsMatchAndSaves() {
        Match match = new Match();
        MatchOdds odds = odds();
        when(matchService.findById(1L)).thenReturn(match);
        when(matchOddsRepository.save(odds)).thenReturn(odds);

        MatchOdds result = matchOddsService.create(1L, odds);

        assertThat(result.getMatch()).isEqualTo(match);
        verify(matchOddsRepository).save(odds);
    }

    @Test
    void update_updatesFields() {
        MatchOdds existing = odds();
        MatchOdds updated = new MatchOdds();
        updated.setSpecifier("2");
        updated.setOdd(2.5);

        when(matchOddsRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(matchOddsRepository.save(existing)).thenReturn(existing);

        MatchOdds result = matchOddsService.update(1L, updated);

        assertThat(result.getSpecifier()).isEqualTo("2");
        assertThat(result.getOdd()).isEqualTo(2.5);
    }

    @Test
    void delete_shouldSucceed() {
        when(matchOddsRepository.findById(1L)).thenReturn(Optional.of(odds()));

        matchOddsService.delete(1L);

        verify(matchOddsRepository).deleteById(1L);
    }

    private MatchOdds odds() {
        MatchOdds o = new MatchOdds();
        o.setSpecifier("1");
        o.setOdd(1.85);
        return o;
    }
}
