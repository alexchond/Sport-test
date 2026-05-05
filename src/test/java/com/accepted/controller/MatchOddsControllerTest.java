package com.accepted.controller;

import com.accepted.entity.MatchOdds;
import com.accepted.exception.GlobalExceptionHandler;
import com.accepted.exception.ResourceNotFoundException;
import com.accepted.service.MatchOddsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MatchOddsController.class)
@Import(GlobalExceptionHandler.class)
class MatchOddsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchOddsService matchOddsService;

    @Test
    void getAllOdds_returns200() throws Exception {
        when(matchOddsService.findAll()).thenReturn(List.of(odds()));

        mockMvc.perform(get("/api/match-odds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specifier").value("1"));
    }

    @Test
    void getById_found_shouldSucceed() throws Exception {
        when(matchOddsService.findById(1L)).thenReturn(odds());

        mockMvc.perform(get("/api/match-odds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.odd").value(1.85));
    }

    @Test
    void getById_notFound_shouldThrow() throws Exception {
        when(matchOddsService.findById(99L)).thenThrow(new ResourceNotFoundException("MatchOdds not found with id: 99"));

        mockMvc.perform(get("/api/match-odds/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("MatchOdds not found with id: 99"));
    }

    @Test
    void getByMatch_shouldSucceed() throws Exception {
        when(matchOddsService.getByMatchId(1L)).thenReturn(List.of(odds()));

        mockMvc.perform(get("/api/match-odds/match/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specifier").value("1"));
    }

    @Test
    void getByMatch_matchNotFound_shouldThrows() throws Exception {
        when(matchOddsService.getByMatchId(99L)).thenThrow(new ResourceNotFoundException("Match not found with id: 99"));

        mockMvc.perform(get("/api/match-odds/match/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldSucceed() throws Exception {
        MatchOdds o = odds();
        when(matchOddsService.create(eq(1L), any(MatchOdds.class))).thenReturn(o);

        mockMvc.perform(post("/api/match-odds")
                        .param("matchId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(o)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.specifier").value("1"));
    }

    @Test
    void update_shouldSucceed() throws Exception {
        MatchOdds o = odds();
        when(matchOddsService.update(eq(1L), any(MatchOdds.class))).thenReturn(o);

        mockMvc.perform(put("/api/match-odds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(o)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_shouldSucceed() throws Exception {
        doNothing().when(matchOddsService).delete(1L);

        mockMvc.perform(delete("/api/match-odds/1"))
                .andExpect(status().isNoContent());
    }

    private MatchOdds odds() {
        MatchOdds o = new MatchOdds();
        o.setSpecifier("1");
        o.setOdd(1.85);
        return o;
    }
}
