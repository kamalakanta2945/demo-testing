package com.bluepal.controller;

import com.bluepal.entity.AgentLoad;
import com.bluepal.repository.AgentLoadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AgentLoadRepository agentLoadRepository;

    public AssignmentController(AgentLoadRepository agentLoadRepository) {
        this.agentLoadRepository = agentLoadRepository;
    }

    @GetMapping("/load")
    public ResponseEntity<List<AgentLoad>> getAgentLoad() {
        return ResponseEntity.ok(agentLoadRepository.findAll());
    }
}
