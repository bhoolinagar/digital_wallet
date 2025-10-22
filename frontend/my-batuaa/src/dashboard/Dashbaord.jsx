import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Container from "@mui/material/Container";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";

import logo from "../images/mybatuaa.png";
import personLogo from "../assets/user.png";
import homeLogo from "../images/home_logo.png";
import logoutLogo from "../images/logout_logo.png";

import "./Dashboard.css";

export default function Dashboard() {
  const handleLogout = () => {
    console.log("Logout clicked");
    // Example: redirect to login page
    // window.location.href = "/login";
  };

  return (
    <AppBar
      position="static"
      sx={{
        backgroundColor: "#f3f5f9",
        width: "99%",
        margin: "0 auto",
        borderRadius: 2,
      }}
    >
      <Container
        maxWidth={false}
        sx={{
          width: "100%",
          paddingX: 2,
        }}
      >
        <Toolbar
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            minHeight: 120,
          }}
        >
          {/* Left: App Logo */}
          <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
            <img
              src={logo}
              alt="My Batuaa Logo"
              style={{ width: 100, height: 100, transform: "scale(.9)" }}
            />
          </Box>

          {/* Center: Home Icon + Dashboard Text */}
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              flexDirection: "column",
              gap: 0.2,
            }}
          >
            <img src={homeLogo} alt="Home" style={{ width: 50, height: 50 }} />
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

          {/* Right: User Info */}
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              flexDirection: "column",
              gap: 0.2,
            }}
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
                fontSize: 18,
                fontWeight: 600,
              }}
            >
              Bhooli Nagar
            </Typography>
          </Box>

          {/* Logout Button */}
          <Button
            className="logout-btn"
            variant="text"
            startIcon={<img src={logoutLogo} alt="Logout" />}
            onClick={handleLogout}
          >
            Logout
          </Button>
        </Toolbar>
      </Container>
    </AppBar>
  );
}
