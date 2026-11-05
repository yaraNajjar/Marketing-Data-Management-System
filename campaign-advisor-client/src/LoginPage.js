import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { Card } from "primereact/card";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { Password } from "primereact/password";
import { Toast } from "primereact/toast";
import { useAuth } from "./components/AuthContext";

function LoginPage() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const toast = useRef(null);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/genToken", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username: username, password: password }),
      });

      if (!response.ok) {
        throw new Error(`Authentication failed: ${response.status}`);
      }

      const jwtToken = await response.text();
      login(jwtToken);
      navigate("/home");
      toast.current.show({
        severity: "success",
        summary: "Login Successful",
        detail: "You are now logged in.",
      });
    } catch (error) {
      console.error("Login failed", error);
      toast.current.show({
        severity: "error",
        summary: "Login Failed",
        detail: "Login failed. Please try again.",
      });
    }
  };

  return (
    <div
      className="flex align-items-center justify-content-center"
      style={{ height: "100vh" }}
    >
      <Toast ref={toast} />
      <Card title="Login" className="p-shadow-6" style={{ width: "25rem" }}>
        <form onSubmit={handleSubmit} className="p-fluid">
          <div className="p-field">
            <label htmlFor="username">Username</label>
            <InputText
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              autoFocus
            />
          </div>
          <br />
          <div className="p-field">
            <label htmlFor="password">Password</label>
            <Password
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              feedback={false}
              toggleMask
            />
          </div>
          <br />
          <Button type="submit" label="Login" className="p-mt-2" />
        </form>
      </Card>
    </div>
  );
}

export default LoginPage;
