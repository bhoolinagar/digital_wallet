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
import { Add, Padding, Wallet } from '@mui/icons-material';
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

import { ClipboardWithIcon } from "flowbite-react";
export default function PrimaryWallet() {

  const [primarywallet, setprimaryWallet]=useState([])
  const [wallet_id, setWalletId]=useState("")
  const buyer_email="priyanka@gmail.com"

  // const primary_wallet_id=""
  const navigate = useNavigate();

const goToWallet = (walletId) => {
  navigate(`/addmoney/${walletId}`);
};

const goToAddWallet = (emailId) => {
  navigate(`/addwallet/${emailId}`);
};

  useEffect(()=>{
  // axios.get("http://localhost:8031/wallet/api/v1/wallet-list/"+buyer_email)
   axios.get(`http://localhost:8031/wallet/api/v1/primary?email=${buyer_email}`)
   .then((res)=>{
    setprimaryWallet(res.data.data || [])
  setWalletId(res.data.data.walletId)
  console.log(" rest data: ")
  console.log(res.data)
}) .catch((err) => {
    console.error("Failed to fetch wallets:", err);
    setprimaryWallet([]); // fallback to empty array
  });

},[])

  // Get the current visible wallets
//const visibleWallets = wallets.slice(start, start + limit);
  
return (
<div >
    <Box className="wallet-list-container">
 <Card

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
              Email : {buyer_email}
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
                Wallet Id : {primarywallet.walletId}
              </Typography>

              <ClipboardWithIcon valueToCopy={primarywallet.walletId} />
            </Box>
            <Typography variant="body2" align="left" sx={{ color: "white" }}>
              Balance :  ₹ {primarywallet.balance}
            </Typography>
          </CardContent>
        </CardActionArea>
        <CardActions className="wallet-card-actions">
          <Button
            size="medium"
            color="primary"
            className="add-wallet-btn"

            onClick={() => {
             goToWallet(primarywallet.walletId)
            }}
          >
            Add Money
          </Button>
        </CardActions>
      </Card>






   <Card className="wallet-card" sx={{borderRadius:4 ,width:270}}>
      <CardActionArea>
        <img src={walletLogo} alt="Wallet" className="wallet-image" />
      </CardActionArea>

      <CardActions className="wallet-card-actions">
        <Button size="medium" color="primary" className="add-wallet-btn"

            onClick={()=>goToAddWallet(buyer_email)}
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
        <Button size="medium" color="primary" className="add-wallet-btn"
        onClick={() =>
          goToTransferMoney(primarywallet.walletId)}
          >
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
  </div>
  );

}
