import React from "react";
import "./App.css"; // Import CSS for styling
import { AuthProvider } from "./components/AuthContext"; // Import the provider
import TopNavBar from "./components/TopNavBar";
import RoutesComponent from "./components/RoutesComponent";

function App() {
  return (
    <AuthProvider>
      <div className="app">
        <header style={{ backgroundColor: "#F9F9F9" }}>
          <TopNavBar />
        </header>
        <div className="m-5">
          <RoutesComponent />
        </div>
      </div>
    </AuthProvider>
  );
}

export default App;
