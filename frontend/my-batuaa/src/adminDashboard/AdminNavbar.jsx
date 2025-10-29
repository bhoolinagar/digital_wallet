import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  AppBar,
  Box,
  Toolbar,
  Container,
  Avatar,
  Button,
  Typography,
  IconButton,
  Menu,
  MenuItem,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import logo from "../assets/logo_copy.png";
import personLogo from "../assets/user.png";
import homeLogo from "../images/home_logo.png";
import logoutLogo from "../images/logout_logo.png";
import "../Navbar.css";

export default function AdminNavbar() {
  const navigate = useNavigate();
  const buyer_email = sessionStorage.getItem("email");
  const buyer_Name = sessionStorage.getItem("name");

  // For mobile menu toggle
  const [anchorElNav, setAnchorElNav] = useState(null);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleLogout = () => {
    sessionStorage.clear();
    console.log("Logout clicked");
    navigate("/");
  };

  const handleDashboard = () => {
    console.log("Admin dashboard clicked");
    navigate(`/admin/admindashboard`);
  };

  return (
   <> <AppBar
        position="fixed"
        sx={{
          
          backgroundColor: "#e5efff",
          boxShadow: "0px 2px 6px rgba(0,0,0,0.15)",
          height: "80px",
          justifyContent: "center",
        }}
      >
        <Container maxWidth="xl">
          <Toolbar
            disableGutters
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              flexDirection: { xs: "row", md: "row" },
            }}
          >
            {/* Left: Logo */}
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                cursor: "pointer", 
                marginLeft:'250px'
              }}
              onClick={() => handleDashboard}
            >
              <img
                src={logo}
                alt="My Batuaa Logo"
                style={{ width: 120, height: 70 }}
              />
            </Box>

            {/* Center: Dashboard link (Hidden on mobile) */}
            <Box
              sx={{
                display: { xs: "none",
                   md: "flex" },
                alignItems: "center",
                gap: 3,
                cursor: "pointer",
                "&:hover": { transform: "scale(1.05)", transition: "0.2s" },
              }}
              onClick={handleDashboard}
            >
              <img
                src={homeLogo}
                alt="Home Icon"
                style={{ width: 45, height: 45 }}
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

            {/* Right: User Info + Logout (Desktop) */}
            <Box
              sx={{
                display: { xs: "none", md: "flex" },
                alignItems: "center",
                gap: 6,
              }}
            >
              <Box
                sx={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  cursor: "pointer",
                  "&:hover": { transform: "scale(1.05)", transition: "0.2s" },
                }}
                
              >
                <Avatar
                  alt="User Avatar"
                  src={personLogo}
                  sx={{ width: 48, height: 48 }}
                />
                <Typography
                  variant="subtitle1"
                  sx={{
                    color: "#0F3A6E",
                    fontFamily: "Roboto Mono, monospace",
                    fontSize: 15,
                    fontWeight: 600,
                  }}
                >
                  {buyer_Name || "Guest"}
                </Typography>
              </Box>

              <Button
                variant="text"
                onClick={handleLogout}
                startIcon={
                  <img
                    src={logoutLogo}
                    alt="Logout Icon"
                    style={{ width: 45, height: 45 }}
                  />
                }
                sx={{
                  flexDirection: "column",
                  color: "#043253",
                  fontWeight: 600,
                  fontSize: 16,
                  textTransform: "none",
                  "&:hover": {
                    backgroundColor: "rgba(15,58,110,0.1)",
                    borderRadius: "8px",
                  },
                }}
             
             >
                Logout
              </Button>
            </Box>

            {/* Mobile Menu Icon */}
            <Box sx={{ display: { 
              xs: "flex", md: "none" } }}>
              <IconButton onClick={handleOpenNavMenu}>
                <MenuIcon sx={{ color: "#0F3A6E" }} />
              </IconButton>
              <Menu
                anchorEl={anchorElNav}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
              >
                <MenuItem
                  onClick={() => {
                    handleCloseNavMenu();
                   handleDashboard();
                  }}
                >
                  Dashboard
                </MenuItem>
                <MenuItem
                  onClick={() => {
                    handleCloseNavMenu();
                    
                  }}
                >
                  Profile
                </MenuItem>
                <MenuItem
                  onClick={() => {
                    handleCloseNavMenu();
                    handleLogout();
                  }}
                >
                  Logout
                </MenuItem>
              </Menu>
            </Box>
          </Toolbar>
        </Container>
      </AppBar>

      {/* Spacer to prevent content overlap */}
      <Toolbar /></>
  );
}
