import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import WalletList from './WalletList';
import Navbar from '../Navbar';
import { useNavigate } from 'react-router-dom';
import PrimaryWallet from './Primary';

export default function Dashboard() {

  const navigate = useNavigate();

  const goToAdminDashboard = () => {
    navigate('/adminDashboard'); // redirects to AdminDashboard route
  };

  return (
    <div>
     <Navbar></Navbar>
     <Box height={20}/>
     <PrimaryWallet></PrimaryWallet>


      <div>
      <h1>Dashboard</h1>
      <button onClick={goToAdminDashboard}>
        Go to Admin Dashboard
      </button>
    </div>

{/*<WalletList/> */}

    </div>
  );
}