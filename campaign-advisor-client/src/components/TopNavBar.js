import React, { useState, useRef } from "react";
import { Menubar } from "primereact/menubar";
import { Menu } from "primereact/menu";
import { Button } from "primereact/button";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

export default function TopNavBar() {
  const { isLoggedIn, logout, currentUserName, isAdmin } = useAuth();
  const navigate = useNavigate();
  const avatarMenu = useRef(null);

  // Function to handle menu item click
  const handleNavigation = (path) => {
    if (isLoggedIn) {
      navigate(path);
    } else {
      navigate("/login");
    }
  };

  const [menuItems, setMenuItems] = useState([
    {
      label: "Logout",
      icon: "pi pi-sign-out",
      command: () => {
        logout();
      },
    },
  ]);

  const items = [
    {
      label: "Home",
      icon: "pi pi-home",
      command: () => handleNavigation("/Home"),
    },
    ...(isLoggedIn
      ? [
          ...(isAdmin
            ? [
                {
                  label: "All Actions & Metrics",
                  icon: "pi pi-play-circle",
                  command: () => handleNavigation("/actions"),
                },
                {
                  label: "All History",
                  icon: "pi pi-history",
                  command: () => handleNavigation("/history"),
                },
                {
                  label: "Users",
                  icon: "pi pi-user",
                  command: () => handleNavigation("/users"),
                },
              ]
            : [
                {
                  label: "Actions & Metrics",
                  icon: "pi pi-play-circle",
                  command: () => handleNavigation("/actions"),
                },
                {
                  label: "History",
                  icon: "pi pi-history",
                  command: () => handleNavigation("/history"),
                },
              ]),
        ]
      : []),
    {
      label: "About",
      icon: "pi pi-info-circle",
      command: () => handleNavigation("/about"),
    },
  ];

  const start = (
    <img
      alt="logo"
      src="https://primefaces.org/cdn/primereact/images/logo.png"
      height="40"
      className="mr-2"
    ></img>
  );

  const end = isLoggedIn ? (
    <div className="flex align-items-center gap-2">
      <Menu model={menuItems} popup ref={avatarMenu} id="popup_menu_left" />
      <Button
        label={<span className="p-mr-2">{currentUserName || "User"}</span>}
        icon="pi pi-user"
        className="mr-2"
        onClick={(event) => avatarMenu.current.toggle(event)}
        aria-controls="popup_menu_left"
        aria-haspopup
        rounded
        text
        raised
        severity="info"
        aria-label="User"
      />
    </div>
  ) : (
    <Button
      label="Login"
      icon="pi pi-sign-in"
      onClick={() => navigate("/login")}
    />
  );
  return (
    <div className="mb-5">
      <Menubar model={items} start={start} end={end} />
    </div>
  );
}
