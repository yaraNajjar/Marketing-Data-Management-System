import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext"; // Validate this import path
import LoginPage from "../LoginPage";
import Home from "./Home";
import ActionsTab from "./ActionsTab";
import AllActionsTab from "./AllActionsTab";
import HistoryTab from "./HistoryTab";
import AllHistoryTab from "./AllHistoryTab";
import AboutTab from "./AboutTab";
import UsersList from "./UsersList";

function RoutesComponent() {
  const { isLoggedIn, isAdmin } = useAuth();
  const redirectLogin = <Navigate to="/login" />;

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/"
        element={isLoggedIn ? <Navigate to="/Home" /> : redirectLogin}
      />
      <Route path="/Home" element={isLoggedIn ? <Home /> : redirectLogin} />
      <Route
        path="/users"
        element={isLoggedIn && isAdmin ? <UsersList /> : redirectLogin}
      />
      <Route
        path="/actions"
        element={
          isLoggedIn ? (
            isAdmin ? (
              <AllActionsTab />
            ) : (
              <ActionsTab />
            )
          ) : (
            redirectLogin
          )
        }
      />
      <Route
        path="/history"
        element={
          isLoggedIn ? (
            isAdmin ? (
              <AllHistoryTab />
            ) : (
              <HistoryTab />
            )
          ) : (
            redirectLogin
          )
        }
      />
      <Route
        path="/about"
        element={isLoggedIn ? <AboutTab /> : redirectLogin}
      />
    </Routes>
  );
}

export default RoutesComponent;
