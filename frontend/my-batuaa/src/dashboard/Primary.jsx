import React from "react";
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardMedia from '@mui/material/CardMedia';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import CardActionArea from '@mui/material/CardActionArea';
import CardActions from '@mui/material/CardActions';
import { Box, IconButton } from '@mui/material';
import copyLogo from '../images/copy_logo.png';
import { Add, Wallet } from '@mui/icons-material';
import AddMoney from './AddMoney.jsx';
import  walletLogo from '../images/wallet.png'
import transferLogo from '../images/transfer_logo.png'
import shoppinyLogo from '../images/shopying.png'
import "./WalletList.css";
import AddWallet from './AddWallet.jsx';
import { use, useEffect, useState } from 'react';
import axios from 'axios';
import ArrowBackIosIcon from "@mui/icons-material/ArrowBackIos";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import { esES } from "@mui/material/locale";
import { useNavigate } from "react-router-dom";

export default function PrimaryWallet() {

  const [wallets, setWallets]=useState([])
  const buyer_email="priyanka@gmail.com"

  const navigate = useNavigate();

const goToWallet = (walletId) => {
  navigate(`/addmoney/${walletId}`);
};


  const [start, setStart] = useState(0);
  // show 7 cards initiall

const limit=5; // only show 5 card per view 
 
// Move to previous 5 cards
  const handlePrev = () => {
    if (start > 0) {
      setStart((prev) => prev - limit);
     }
  };
  // Move to next 5 cards
  const handleNext = () => {
    if (start+limit< wallets.length) {
      setStart((prev) => prev + limit);
    
    }
  };
  useEffect(()=>{
  // axios.get("http://localhost:8031/wallet/api/v1/wallet-list/"+buyer_email)
   axios.get(`http://localhost:8031/wallet/api/v1/primary`)
   .then((res)=>{
    setWallets(res.data.data || [])
  console.log(" rest data: ")
  console.log(res.data)
}) .catch((err) => {
    console.error("Failed to fetch wallets:", err);
    setWallets([]); // fallback to empty array
  });

},[])

 const containerRef = React.useRef(null);
  // Get the current visible wallets
const visibleWallets = wallets.slice(start, start + limit);
  
return (
    <Box className="wallet-list-container"> 
   <Box className='box'>
    
     {/* Left Button */}
      <Button
        onClick= {handlePrev}
        disabled={start==0}
        sx={{
          position: "absolute",
          left: 0,
          top: "40%",
          zIndex: 1,
          backgroundColor: "white",
          borderRadius: "50%",
          minWidth: "40px",
          boxShadow: 2,
        }}
      >
        <ArrowBackIosIcon />
      </Button>

{/*  Scorllable card container */}
<Box ref={containerRef}  sx={{
          display: "flex",
          overflowX: "hidden",
          scrollBehavior: "smooth",
          px:6,
          gap: 5,
         scrollBehavior:'smooth',
         justifyContent:'center'
        }}>
  {visibleWallets.length > 0 ? (
    visibleWallets.map((wallet,index) => (
      <Card
        key= { wallet.walletid||index}
        sx={{
          maxWidth: 345,
          color: "#0F3A6E",
          borderRadius: 4,
          boxShadow: 3,
          borderColor: "#9828a2ff",
          borderBlockColor: "#ba357cff",
        }}
      >
        <CardActionArea sx={{ backgroundColor: "#0F3A6E" }}>
          <CardContent>
            <Typography
              gutterBottom
              variant="h5"
              align="right"
              component="div"
              sx={{ color: "white" }}
            >
              Wallet
            </Typography>
            <Typography variant="body2" align="left" sx={{ color: "white" }}>
              Name: {wallet.buyerName}
            </Typography>
            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                gap: 1,
                flexDirection: "row",
              }}
            >
              <Typography
                variant="body2"
                align="left"
                sx={{ color: "white" }}
              >
                Wallet Id: {wallet.walletId}
              </Typography>
              <IconButton
                aria-label="copy"
                sx={{ color: "#439b8bff", backgroundColor: "white" }}
              >
                <img
                  src={copyLogo}
                  alt="Copy"
                  style={{ width: 20, height: 20 }}
                />
              </IconButton>
            </Box>
            <Typography variant="body2" align="left" sx={{ color: "white" }}>
              Balance: {wallet.balance} INR
            </Typography>
          </CardContent>
        </CardActionArea>
        <CardActions className="wallet-card-actions">
          <Button
            size="medium"
            color="primary"
            className="add-wallet-btn"
            onClick={() => {
             goToWallet(wallet.walletId)
            }}
          >
            Add Money
          </Button>
        </CardActions>
      </Card>
    ))
  ) : (
    <p>No Wallets found</p>
  )}</Box>

   {/* Right Button */}
      <Button
        onClick={handleNext}
        disabled={(start+limit)>=wallets.length}
        sx={{
          position: "absolute",
          right: 0,
          top: "40%",
          zIndex: 1,
          backgroundColor: "white",
          borderRadius: "50%",
          minWidth: "40px",
          boxShadow: 2,
        }}
      >
        <ArrowForwardIosIcon />
      </Button>
</Box>


   <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={walletLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn"
          onClick={() => {
            window.location.href = '/add-wallet';
            // Navigate to AddWallet component
          }}  
        >
          Add new Wallet
        </Button>
      </CardActions>
    </Card>  

     <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={transferLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn">
          Transfer Money
        </Button>
      </CardActions>
    </Card>  
    
     <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={shoppinyLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn" 
        >
            Shopping
        </Button>
      </CardActions>
    </Card>  
    
  </Box>
  );

}
