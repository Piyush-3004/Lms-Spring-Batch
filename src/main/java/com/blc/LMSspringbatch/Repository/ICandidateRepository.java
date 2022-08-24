package com.blc.LMSspringbatch.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blc.LMSspringbatch.Model.CandidateModel;

public interface ICandidateRepository extends JpaRepository<CandidateModel ,Long>{

	
}
