import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import LogoutIcon from '@mui/icons-material/Logout';
//import logo from '../assets/mybatuaa.png'; 
import logo from '../ assets/logo.png';
import personLogo from '../ assets/user.png';
import { Home, Logout } from '@mui/icons-material';
import logoutLogo from '../ assets/logout_logo.png';
import homeLogo from '../ assets/home_logo.png';


 export default function Dashboard() {
  const handleLogout = () => {
    console.log('Logout clicked');
  };

  return (
    <>
    <AppBar position="static"
  sx={{
    backgroundColor: '#e3edfbff',
    width: '99%',
    margin: '0 auto',
    borderRadius: 2,
  }}
>
  <Container
    maxWidth={false}
    sx={{
      width: '100%',
      paddingX: 2,
    }}
  >
    <Toolbar
      sx={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        minHeight: 120,
      }}
    >
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <img src={logo} alt="Logo" style={{ width: 100, height: 100, transform: 'scale(.9)' }} /> 
             
            
          </Box>
          {/* Left: Logo + Dashboard Text */}
          <Box sx={{ display: 'flex', alignItems: 'left', gap: -.3 ,textAlign: 'bottom',flexDirection: 'column'   }}>
           <img src={homeLogo} alt="Home" style={{ width: 50, height: 50 }} />
            <Typography
              variant="h6"
              sx={{ color: '#0F3A6E', fontSize: 18,
                 fontWeight: 600, letterSpacing: '.1rem', fontFamily: 'Roboto mono'   }}
            >
     Dashboard
            </Typography>
          </Box>

          {/* Center / Right: User profile + name */}
          <Box sx={{ display: 'flex', alignItems: 'center', flexDirection: 'column', gap: .2 }}>
            <Avatar
              alt="User Logo"
              src={personLogo}
              sx={{ width: 50, height: 50 }}
            />
            <Typography
              variant="h8"
              sx={{ color: '#0F3A6E', fontFamily: 'Roboto mono',
                fontSize: 18, fontWeight: 600 }}
            >
              Bhooli Nagar
            </Typography>
          </Box>

          {/* Right: Logout */}
          <Button
            variant="text"
            sx={{ color: '#0F3A6E', borderColor: '#0F3A6E',flexDirection: 'column' }}
            startIcon={<img src={logoutLogo} alt="Logout" style={{ width: 60, height: 60 }} />}
            onClick={handleLogout}
          ><Typography  variant="h8"
              sx={{ color: '#0F3A6E', fontFamily: 'Roboto mono',
                fontSize: 16, fontWeight: 600 }}>Logout</Typography>
            
          </Button>
        </Toolbar>
      </Container>
    </AppBar>
    </>
  );
}