import {
  Box,
  TextField,
  InputAdornment,
  Button,
  Snackbar,
} from "@mui/material";

import MuiAlert from "@mui/material/Alert";
import { useState } from 'react';
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import './AddMoney.css';
import axios from "axios";
import { useParams } from "react-router-dom";
import Footer from "./Footer";
 import Navbar from "../Navbar";
export default function AddMoney() {
const { walletId } = useParams(); 
  const [formData, setFormData] = useState({ amount: '' });

  const [errors, setErrors] = useState({});
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success", // success | error
  });

  const validate=()=>{
    const newErrors={}
    if(!formData.amount.trim()){
      newErrors.amount="Amount is required.";
    } else if((isNaN(formData.amount)|| parseFloat(formData.amount)<=0)){
       newErrors.amount="Enter a valid positive amount.";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;

  }
  //const walletId = "WAL8BD4C4117";

  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };
 

  const handleSubmit = async (event) => {
    
    event.preventDefault();
if (!validate()) return; // stop if invalid
    console.log("Form submitted:", formData);
    try {
      const response = await axios.post(
        `http://localhost:8031/wallet/api/v1/add-money`,
        null,
        {
          params: {
            walletId: walletId,
            amount: formData.amount,
          },
        }
      );

      setSnackbar({
        open: true,
        message: response.data.message || "Money added successfully!",
        severity: "success",
      });
      setFormData({ amount: "" }); // reset form
    } catch (error) {
      console.error("Error adding money:", error.response?.data.data || error.message);
      setSnackbar({
        open: true,
        message:
          error.response?.data?.message ||
          "Failed to add money. Please try again.",
        severity: "error",
      });

    }
  };

  const handleCloseSnackbar = (_, reason) => {
    if (reason === "clickaway") return;
    setSnackbar({ ...snackbar, open: false });
  };

  return (
   
    <div> <Navbar/>
    <form onSubmit={handleSubmit} className="add-money-form">
      <Box className='box-container'>
        <Box className="form-title">Add Money</Box>

        <TextField
          className="input-container"
          label="Amount"
          name="amount"
          error={!!errors.amount}
          helperText={errors.amount}
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

        <Button
          type="submit"
         className="submit-button contained"
          endIcon={<ArrowForwardIcon />}
         
        >
          Submit
        </Button>
      </Box>

      {/* Snackbar Notification */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={5000} // 5 seconds
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
      >
        <MuiAlert
          elevation={6}
          variant="filled"
          onClose={handleCloseSnackbar}
          severity={snackbar.severity}
          sx={{ width: "100%" }}
        >
          {snackbar.message}
        </MuiAlert>
      </Snackbar>
    </form>
    <Footer/>
    </div>
  );
}
