import React, { useState } from "react";
import {
  Box,
  TextField,
  InputAdornment,
  Button,
  Typography,
} from "@mui/material";
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import './AddMoney.css';
import personLogo from "../assets/person.png"; 
export default function AddWallet() {

const [formData, setFormData] = useState({
    accountNumber: "",
    bankName: "",
    amount: "",
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log("Form submitted:", formData);
  };

  return (
    <form onSubmit={handleSubmit} className="add-money-form">
      <Box className="form-title">Add New Wallet</Box>

      <div className="input-container">
        <TextField
          required
          label="Bank Account Number"
          name="bankAccount"
         
          placeholder="Enter your bank account number"
          value={formData.bankAccount}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <AccountBalanceIcon sx={{ color: "#0F3A6E", fontSize: 30 }} />
              </InputAdornment>
            ),
          }}
        />
      </div>

      <div className="input-container">
        <TextField
          required
          label="Bank Name"
          name="bankName"
        
            placeholder="Enter your bank name"
          value={formData.bankName}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <AccountBalanceIcon
                  sx={{ color: "#0F3A6E", fontSize: 30 }}
                />
              </InputAdornment>
            ),
          }}
        />
      </div>

      <div className="input-container">
        <TextField
          required
          label="Amount"
          name="amount"
            placeholder="100.00"
         
          value={formData.amount}
          onChange={handleChange}
          variant="outlined"
          fullWidth
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <CurrencyRupeeIcon sx={{ color: "#0F3A6E", fontSize: 30 }} />
              </InputAdornment>
            ),
          }}
        />
      </div>

      <Button type="submit" variant="contained" 
      sx={{
            width: '50%',
            height: 50,
          mt: 2,
          py: 1.5,
          px: 4,
          borderRadius: "40px",
          backgroundColor: "#0F3A6E",
          textTransform: "none",
          fontSize: 20,
          fontWeight: 600,
          fontFamily: "Roboto Mono, monospace",
          "&:hover": { backgroundColor: "#0d2e59" },
        }}
      endIcon={<ArrowForwardIcon />}
      >
        Submit
      </Button>
    </form>
  );
}