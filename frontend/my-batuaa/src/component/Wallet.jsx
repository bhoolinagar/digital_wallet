
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
import "./WalletList.css";
export default function Wallet(wallet){
const copyToClipboard = (id) => {
    navigator.clipboard.writeText(id);
    alert("Wallet ID copied!");
  };
    
return(
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
                Wallet Id: {wallet.walletid}
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
              window.location.href = "/addmoney";
            }}
          >
            Add Money
          </Button>
        </CardActions>
      </Card>
   )
}