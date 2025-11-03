package com.bluepal.repository;

import com.bluepal.entity.AgentLoad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentLoadRepository extends JpaRepository<AgentLoad, String> {
}
