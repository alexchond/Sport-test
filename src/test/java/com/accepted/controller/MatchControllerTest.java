package com.accepted.controller;

import com.accepted.entity.Match;
import com.accepted.entity.Sport;
import com.accepted.exception.GlobalExceptionHandler;
import com.accepted.exception.ResourceNotFoundException;
import com.accepted.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchController.class)
@Import(GlobalExceptionHandler.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchService matchService;

    @Test
    void getAllMatches_shouldSucceed() throws Exception {
        when(matchService.findAll()).thenReturn(List.of(match()));

        mockMvc.perform(get("/api/matches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teamA").value("Team A"));
    }

    @Test
    void getById_found_shouldSucceed() throws Exception {
        Match m = match();
        m.setId(1L);
        when(matchService.findById(1L)).thenReturn(m);

        mockMvc.perform(get("/api/matches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teamA").value("Team A"));
    }

    @Test
    void getById_notFound_ShouldThrow() throws Exception {
        when(matchService.findById(99L)).thenThrow(new ResourceNotFoundException("Match not found with id: 99"));

        mockMvc.perform(get("/api/matches/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Match not found with id: 99"));
    }

    @Test
    void create_shouldSucceed() throws Exception {
        Match m = match();
        when(matchService.create(any(Match.class))).thenReturn(m);

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(m)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.teamB").value("Team B"));
    }

    @Test
    void update_shouldSucceed() throws Exception {
        Match m = match();
        when(matchService.update(eq(1L), any(Match.class))).thenReturn(m);

        mockMvc.perform(put("/api/matches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(m)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldSucceed() throws Exception {
        doNothing().when(matchService).delete(1L);

        mockMvc.perform(delete("/api/matches/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound_shouldThrow() throws Exception {
        doThrow(new ResourceNotFoundException("Match not found with id: 1")).when(matchService).delete(1L);

        mockMvc.perform(delete("/api/matches/1"))
                .andExpect(status().isNotFound());
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
