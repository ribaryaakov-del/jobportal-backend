package com.pluralsight.jobportal.service;

import com.pluralsight.jobportal.model.Job;
import com.pluralsight.jobportal.repository.ApplicationRepository;
import com.pluralsight.jobportal.repository.JobRepository;
import com.pluralsight.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job createJob(Job job){
        return jobRepository.save(job);
    }


    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }


}
