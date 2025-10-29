import * as React from "react";
import { useNavigate } from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import Container from "@mui/material/Container";
import MenuIcon from "@mui/icons-material/Menu";
//Import images (no spaces in folder paths)
import logo from "../assets/logo.png"
import login from "../assets/login_log.png";
import register from "../assets/register_logo.png";
import landing from "../assets/landing.png";
import getstart from "../assets/getstart.png";

import Footer from "../component/Footer";
import PublicAppBar from "./PublicAppBar";
export default function Welcomepage() {
  const navigate = useNavigate();

  // States for mobile menu
  const [anchorElNav, setAnchorElNav] = React.useState(null);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };
  return (
    <>
      {/* Navbar */}
      <PublicAppBar/>

      {/* Hero Section */}
      <Container maxWidth="xl">
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            flexWrap: "wrap",
            py: 4,
          }}
        >
          {/* Left Side - Text */}
          <Box
            sx={{
              textAlign: "left",
              maxWidth: { xs: "100%", md: "50%" },
            }}
          >
            <Typography
              variant="h6"
              sx={{lineHeight: 6,
                color: "rgb(21 2 2 / 99%)",
                fontSize: 14,
                fontWeight: 600,
                letterSpacing: ".1rem",
              }}
            >
              Apne Paise Ka Digital Dost
            </Typography>

            <Typography
              variant="h1"
              sx={{
                color: "rgb(21 2 2 / 99%)",
                fontSize: { xs: 24, sm: 30, md: 40 },
                fontWeight: 700,
                lineHeight: 1.2,
                mt: 1,
              }}
            >
              TO MANAGE<br />
              YOUR<br />
              DIGITAL WALLET
            </Typography>

            <Typography
              variant="body1"
              sx={{lineHeight: 3,
                color: "rgb(21 2 2 / 99%)",
                fontSize: 14,
                fontWeight: 600,
                fontFamily:"Roboto Mono monospace",
                letterSpacing: ".05rem",
                mt: 5,
              }}
            >
              My Batuaa is a digital wallet platform built for the modern Indian
              consumer. It simplifies the way you manage, transfer, and track
              your money — all in one secure place. Whether it’s adding money
              from your bank, making quick transactions, or monitoring your
              spending, My Batuaa is designed to make digital finance effortless
              and transparent.
            </Typography>
{/* 
            <img
              src={getstart}
              alt="getstart"
              style={{ width: 250, height: 90, marginTop: 100 }}
            /> */}
          </Box>

          {/* Right Side - Image */}
          <Box
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              maxWidth: { xs: "100%", md: "45%" },
              mt: { xs: 3, md: 0 },
            }}
          >
            <img
              src={landing}
              alt="Landing"
              style={{ width: "100%", maxWidth: 500, height: "auto" }}
            />
          </Box>
        </Box>
      </Container>
      <Footer/>
    </>
  );
}

