import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { setUserDetails } from "../store/userActions";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import {BACKEND_API_URL } from '../config/backend';

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value } );
  };

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        BACKEND_API_URL + '/api/auth/login',
        { email: form.email, password: form.password },
        { headers: { "Content-Type": "application/json" } }
      );

      console.log("user name", form.email);
      console.log("form.password", form.password)

      const token = response.data.token;
      localStorage.setItem("token", token);

      const resp = await axios.get(BACKEND_API_URL + "/api/auth/details", {
        headers: {
          Authorization: "Bearer " + token,
        },
      });
      console.log(resp.data);

      let user = {
        role: resp.data.roles[0],
        email: resp.data.email,
      };

      console.log(user);
      console.log(user.role);

      dispatch(setUserDetails(user));

      switch (user.role) {
        case "ROLE_APPLICANT":
          navigate("/applicant-dashboard");
          break;
        case "ROLE_ADMIN":
          navigate("/admin-dashboard");
          break;
        default:
          console.log("Invalid role");
      }
    } catch (error) {
        console.error("Login flow error:", error);
        console.error("Status:", error.response?.status);
        console.error("Data:", error.response?.data);
        console.error("Message:", error.message);
        alert("Invalid credentials");
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = BACKEND_API_URL + "/oauth2/authorization/google";
  };

  return (
    <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
      <div
        className="card shadow p-4"
        style={{ width: "350px", borderRadius: "15px" }}
      >
        <h4 className="text-center mb-4">Login</h4>

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <input
              type="text"
              name="email"
              className="form-control"
              placeholder="Username"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="mb-3">
            <input
              type="password"
              name="password"
              className="form-control"
              placeholder="Password"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="btn btn-success w-100 mb-3">
            Login
          </button>
        </form>

        <div className="d-flex align-items-center mb-3">
          <hr className="flex-grow-1" />
          <span className="px-2 text-muted">or</span>
          <hr className="flex-grow-1" />
        </div>

        <button
          onClick={handleGoogleLogin}
          className="btn w-100"
          style={{ backgroundColor: "#db4437", color: "white", fontWeight: "500" }}
        >
          Sign in with Google
        </button>
      </div>
    </div>
  );
}