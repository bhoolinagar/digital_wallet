import React from "react";
import { useNavigate } from "react-router-dom";
import { AppBar, Box, Toolbar, Container, Avatar, Button, Typography } from "@mui/material";
import logo from "../src/images/mybatuaa.png";
import personLogo from "../src/assets/user.png";
import homeLogo from "../src/images/home_logo.png";
import logoutLogo from "../src/images/logout_logo.png";
import "./Navbar.css";

export default function Navbar() {
  let buyer_email= sessionStorage.getItem("email")
  let buyer_Name= sessionStorage.getItem("name")
  console.log("Email:ID ", buyer_email)
  const navigate = useNavigate();
const handleLogout = () => {
sessionStorage.clear();
 console.log("Logout clicked");
    navigate("/");
  };
const handleDashboard = () => { 
    console.log("dashboard clicked");
    navigate(`/dashboard`);
  };
  return (
    <AppBar
      position="fixed"
      sx={{
        backgroundColor: "#e5efffff",
        width: "100vw",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
        zIndex: 1100,
      }}
    >
      <Container maxWidth="xl">
        <Toolbar
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            minHeight: { xs: 70, md: 100 },
            flexDirection: { xs: "column", md: "row" },
            gap: { xs: 1, md: 0 },
          }}
        >
          {/* Left: Logo */}
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <img src={logo} alt="My Batuaa Logo" style={{ width: 90, height: 90 }} />
          </Box>

          {/* Center: Dashboard link */}
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              flexDirection: "column",
              cursor: "pointer",
              "&:hover": { transform: "scale(1.05)", transition: "0.2s" },
            }}
            onClick={() => handleDashboard()}
          >
            <img src={homeLogo} alt="Home Icon" style={{ width: 50, height: 50 }} />
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
          <Box sx={{ display: "flex", alignItems: "center", gap: 2 }}>
            <Box
              sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                cursor: "pointer",
                "&:hover": { transform: "scale(1.05)", transition: "0.2s" },
              }}
              onClick={() => navigate("/walletlist")}
            >
              <Avatar alt="User Avatar" src={personLogo} sx={{ width: 50, height: 50 }} />
              <Typography
                variant="subtitle1"
                sx={{
                  color: "#0F3A6E",
                  fontFamily: "Roboto Mono, monospace",
                  fontSize: 16,
                  fontWeight: 600,
                }}
              >
                {buyer_Name}
              </Typography>
            </Box>

            <Button
              sx={{
                flexDirection:"column",
                color: "#043253",
                fontWeight: 600,
                fontSize: 16,
                textTransform: "none",
                "&:hover": {
                  backgroundColor: "rgba(15,58,110,0.1)",
                  borderRadius: "8px",
                },
              }}
              variant="text"
              startIcon={<img src={logoutLogo} alt="Logout Icon" style={{ width: 50, height: 50 }} />}
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
