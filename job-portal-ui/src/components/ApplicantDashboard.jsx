import { useState, useEffect } from "react";
import Navbar from "./Navbar";
import axios from "axios";
import {BACKEND_API_URL } from '../config/backend';


const ApplicantDashboard = () => {

    const[jobs, setJobs] = useState([]);

    useEffect(() => {
        getAllJobs();
    }, []) ;

    const getAllJobs = async () => {
        try{
            const response = await axios.get(BACKEND_API_URL + "/api/jobs/all");
            setJobs(response.data) 
        } catch (error) {
            console.log("Error fetching jobs:", error);
        }
    }

    const apply = async(jobId) => {
        try{
            const response = await axios.post(BACKEND_API_URL + "/api/application/apply/" + jobId, {}, {
                    headers: {
                        "Authorization": "Bearer " + localStorage.getItem("token")
                    }
                }
            )
            console.log("Application response:", response.data);
            alert("Applied successfully");
        } catch (error) {
            console.log("Error applying for job:", error);
            console.log("Status:", error.response?.status);
            console.log("Data:", error.response?.data);
            alert("Failed to apply for job");
        }
    }
        
        
    return (<div>
        <Navbar />
        <h1>Applicant Dashboard</h1>
        <div className="container"> 
            <div className="row">
                {
                    jobs.map((job, index) => (
                        <div className= "col-sm-4" key={ index}>
                            <div className="card mb-4">
                                <div className="card-body">
                                    <h4>Title: {job.title}</h4>
                                    <p>Details: {job.description}</p>
                                    <p>Company: {job.company}</p>
                                    <p>Posted Date: {job.postedDate}</p>
                                    <div>
                                        <button className="btn btn-primary" onClick={() => apply(job.id)}>Apply</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    </div>
    );
}

export default ApplicantDashboard;