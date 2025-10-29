import React, { useState } from "react";
import {
  Box,
  TextField,
  InputAdornment,
  Button, Snackbar,
  Typography,
} from "@mui/material";
import AccountBalanceIcon from "@mui/icons-material/AccountBalance";
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import { LoadingButton } from "@mui/lab"; // ✅ loading button
import MuiAlert from "@mui/material/Alert";
import axios from "axios";
import Navbar from "../Navbar";
import Footer from "./Footer";
import { useNavigate } from "react-router-dom";
import "./AddMoney.css";

export default function AddWallet() {
  const emailId = sessionStorage.getItem("email");
  const token = sessionStorage.getItem("token");
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    accountNumber: "",
    bankName: "",
    balance: "",
  });

  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false); // ✅ new state
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success",
  });

  // 🧮 Validation
  const validate = () => {
    const newErrors = {};

    if (!formData.accountNumber.trim()) {
      newErrors.accountNumber = "Account number is required.";
    } else if (!/^\d{11}$/.test(formData.accountNumber)) {
      newErrors.accountNumber = "Enter a valid account number (11 digits).";
    }

    if (!formData.bankName.trim()) {
      newErrors.bankName = "Bank name is required.";
    } else if (!/^[A-Za-z\s]+$/.test(formData.bankName)) {
      newErrors.bankName = "Bank name must contain only letters.";
    }

    if (!formData.balance.trim()) {
      newErrors.balance = "Amount is required.";
    } else if (isNaN(formData.balance) || parseFloat(formData.balance) <= 0) {
      newErrors.balance = "Enter a valid positive amount.";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // 📝 Input handler
  const handleChange = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  // 🚀 Submit handler
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!validate()) return;

    setLoading(true); // start spinner

    const payload = {
      emailId,
      balance: parseFloat(formData.balance),
      bankName: formData.bankName,
      accountNumber: formData.accountNumber,
    };

    try {
      const response = await axios.post(
        `http://localhost:8031/wallet/api/v1/link-bank-account`,
        payload,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setSnackbar({
        open: true,
        message:
          response.data?.data?.message || "New wallet generated successfully!",
        severity: "success",
      });

      setFormData({ accountNumber: "", bankName: "", balance: "" });
      setTimeout(() => navigate("/dashboard"), 1500); // redirect after success
    } catch (error) {
      console.error("Error generating wallet:", error);
      setSnackbar({
        open: true,
        message:
          error.response?.data?.message ||
          "Failed to generate wallet. Please try again.",
        severity: "error",
      });
    } finally {
      setLoading(false); // stop spinner
    }
  };

  // Snackbar close
  const handleCloseSnackbar = (_, reason) => {
    if (reason === "clickaway") return;
    setSnackbar({ ...snackbar, open: false });
  };

  return (
    <div>
      <Navbar />

      <form onSubmit={handleSubmit} className="add-money-form">
        <Box className="form-title">Add New Wallet</Box>

        {/* Account Number */}
        <div className="input-container">
          <TextField
            required
            label="Bank Account Number"
            name="accountNumber"
            error={!!errors.accountNumber}
            helperText={errors.accountNumber}
            placeholder="Enter your bank account number"
            value={formData.accountNumber}
            onChange={handleChange}
            disabled={loading}
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

        {/* Bank Name */}
        <div className="input-container">
          <TextField
            required
            label="Bank Name"
            name="bankName"
            error={!!errors.bankName}
            helperText={errors.bankName}
            placeholder="Enter your bank name"
            value={formData.bankName}
            onChange={handleChange}
            disabled={loading}
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

        {/* Balance */}
        <div className="input-container">
          <TextField
            required
            label="Amount"
            name="balance"
            placeholder="100.00"
            error={!!errors.balance}
            helperText={errors.balance}
            value={formData.balance}
            onChange={handleChange}
            disabled={loading}
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

        {/* ✅ Loading Submit Button */}
        <LoadingButton
          type="submit"
          variant="contained"
          loading={loading}
          loadingPosition="end"
          endIcon={<ArrowForwardIcon />}
          className="submit-button"
          sx={{
            mt: 2,
            bgcolor: "#0F3A6E",
            "&:hover": { bgcolor: "#0c2e58" },
          }}
        >
          {loading ? "Submitting..." : "Submit"}
        </LoadingButton>

        {/* Snackbar Notification */}
        <Snackbar
          open={snackbar.open}
          autoHideDuration={5000}
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

      <Footer />
    </div>
  );
}
