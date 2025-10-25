import React from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  AppBar,
  Box,
  Toolbar,
  Container,
  Avatar,
  Button,
  Typography,
} from "@mui/material";

import logo from "../src/images/mybatuaa.png";
import personLogo from "../src/assets/user.png";
import homeLogo from "../src/images/home_logo.png";
import logoutLogo from "../src/images/logout_logo.png";
import "./Navbar.css";

export default function Navbar() {
  const navigate = useNavigate();
  const handleLogout = () => {
    console.log("Logout clicked");
    // Example: redirect to login page
    navigate("/login");
  };

  return (
    <AppBar
    
      sx={{
        backgroundColor: "#ffffffff",
        width: "100vw",
        borderRadius: 1,
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
      }}
    >
      <Container maxWidth="xl">
        <Toolbar
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            minHeight: 100,
          }}
        >
          {/* Left: Logo */}
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <img
              src={logo}
              alt="My Batuaa Logo"
              style={{ width: 90, height: 90 }}
            />
          </Box>

          {/* Center: Home + Dashboard */}
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              flexDirection: "column",
              cursor: "pointer",
            }}
            onClick={() => navigate("/")}
          >
            <img
              src={homeLogo}
              alt="Home Icon"
              style={{ width: 50, height: 50 }}
            />
            <Typography
              variant="h6"
              sx={{
                color: "#0F3A6E",
                fontSize: 18,
                fontWeight: 600,
                letterSpacing: ".1rem",
                fontFamily: "Roboto Mono, monospace",
              }}
            >
              Dashboard
            </Typography>
          </Box>

          {/* Right: User Info + Logout */}
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              gap: 2,
            }}
          >
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
        onClick={() => navigate("/walletlist")}
            >
              <Avatar
                alt="User Avatar"
                src={personLogo}
                sx={{ width: 50, height: 50 }}
              />
            
              <Typography
                variant="subtitle1"
                sx={{
                  color: "#0F3A6E",
                  fontFamily: "Roboto Mono, monospace",
                  fontSize: 16,
                  fontWeight: 600,
                }}
              >
                Bhooli Nagar
              </Typography>
            </Box>

            <Button
              sx={{
                color: "#043253ff",
                fontWeight: 600,
                fontSize: 16,
                textTransform: "none",
              }}
              variant="text"
              startIcon={
                <img
                  src={logoutLogo}
                  alt="Logout Icon"
                  style={{ width: 25, height: 25 }}
                />
              }
              onClick={handleLogout}
            >
              Logout
            </Button>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
