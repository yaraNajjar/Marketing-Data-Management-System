import React, { createContext, useState, useContext } from "react";
import * as jose from "jose";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const storedToken = sessionStorage.getItem("token");
  const storedRoles = sessionStorage.getItem("role");
  const storedUserId = sessionStorage.getItem("currentUserId");
  const storedUserName = sessionStorage.getItem("currentUserName");

  const [isLoggedIn, setIsLoggedIn] = useState(!!storedToken);
  const [currentUserId, setCurrentUserId] = useState(storedUserId);
  const [currentUserName, setCurrentUserName] = useState(storedUserName);
  const [currentUserRoles, setCurrentUserRoles] = useState(
    storedRoles ? storedRoles.split(",") : []
  );
  const [token, setToken] = useState(storedToken);

  const login = (token) => {
    try {
      const decoded = jose.decodeJwt(token); // Decode the JWT to access payload
      const { accountId, sub, role } = decoded; // Assuming these fields are in your token's payload

      sessionStorage.setItem("token", token);
      sessionStorage.setItem("role", role);
      sessionStorage.setItem("currentUserId", accountId);
      sessionStorage.setItem("currentUserName", sub);

      setToken(token);
      setCurrentUserId(accountId);
      setCurrentUserName(sub);
      setCurrentUserRoles(role.split(","));
      setIsLoggedIn(true);
    } catch (error) {
      console.error("Failed to decode token", error);
      // Handle error or set up additional error state as necessary
    }
  };

  const logout = () => {
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("currentUserId");
    sessionStorage.removeItem("currentUserName");
    sessionStorage.removeItem("role");
    setToken(null);
    setCurrentUserId(null);
    setCurrentUserName(null);
    setCurrentUserRoles([]);
    setIsLoggedIn(false);
  };

  const isAdmin = currentUserRoles.includes("ROLE_ADMIN");

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn,
        token,
        login,
        logout,
        currentUserId,
        currentUserName,
        currentUserRoles,
        isAdmin,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
