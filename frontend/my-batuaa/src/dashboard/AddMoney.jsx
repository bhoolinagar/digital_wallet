import {
  Box,
  TextField,
  InputAdornment,
  Button,
  Typography,
} from "@mui/material";

import { useState } from 'react';
import Input from '@mui/material/Input';
import InputLabel from '@mui/material/InputLabel';
import FormControl from '@mui/material/FormControl';
import personLogo from '../images/person.png';
import AccountCircle from '@mui/icons-material/AccountCircle';
import  bankLogo from '../images/bank_logo.png';        
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import './AddMoney.css';
export default function AddMoney() {
  const [formData, setFormData] = useState({
    amount: '',
  });

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log("Form Submitted:", formData);
  };

  return (
    <form onSubmit={handleSubmit} className="add-money-form">
      <Box className="">
        <Box
         className="form-title">
          Add Money
        </Box>
      {/* Amount */}
      
      <TextField className="input-container"
        label="Amount"
    name="amount"
       enterKeyHint="Enter amount"
        value={formData.amount}
        onChange={handleChange}
        fullWidth
        placeholder="100.00"
        variant="outlined"
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <CurrencyRupeeIcon sx={{ color: "#0F3A6E", fontSize: 40 }} />
            </InputAdornment>
          ),
        }}
        
      />

      {/* Submit Button */}
      
      <Button
        type="submit"
        variant="contained"
        endIcon={<ArrowForwardIcon />}
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
      >
        Submit
      </Button>
      </Box>
    

    </form>
  );
}
