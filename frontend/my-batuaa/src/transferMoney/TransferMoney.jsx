import React, { useState, useEffect } from "react";
import {
  Box,
  TextField,
  InputAdornment,
  Button,
  CircularProgress,Snackbar
} from "@mui/material";
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import axios from "axios";
import { useLocation, useNavigate } from "react-router-dom";
import "../component/AddMoney.css";
import Footer from "../component/Footer";
import Navbar from "../Navbar";
import { LoadingButton } from "@mui/lab";
import MuiAlert from "@mui/material/Alert";
import LoadingScreen from "../component/LoadingScreen";
export default function TransferMoney() {
  const location = useLocation();
  const navigate = useNavigate();
const goToDashboard = () => {
    navigate('/dashboard'); // redirects to AdminDashboard route
  };
  // Get email and primary wallet from state or session
  const { email, primaryWalletId } = location.state || {};
  const fromBuyerEmail = email || sessionStorage.getItem("email");
  const token = sessionStorage.getItem("token");
 console.log(" token "+ token+"  walletId "+ primaryWalletId)
  const [loading, setLoading] = useState(true);
  const [wallets, setWallets] = useState([]);
  const [message, setMessage] = useState("");
const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success",
  });
 const handleCloseSnackbar = (_, reason) => {
    if (reason === "clickaway") return;
    setSnackbar({ ...snackbar, open: false });
  };

  const [formData, setFormData] = useState({
    fromWalletId: primaryWalletId || "",
    toWalletId: "",
    amount: "",
    remarks: "",
  });

  
  // Fetch wallets on mount
  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    if (!fromBuyerEmail) {
      setMessage("User email not found. Please login again.");
     setSnackbar({
        open: true,
        message: "User email not found. Please login again.",
        severity: "error",
      });
      setLoading(false);
      return;
    }

    axios
      .get(`http://localhost:8031/wallet/api/v1/wallet-list/${encodeURIComponent(fromBuyerEmail)}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        const apiResponse = res.data;
        console.log("Wallet list response:", apiResponse);

        if (apiResponse.status === "success" && apiResponse.data?.length > 0) {
          setWallets(apiResponse.data);

          // Set default fromWalletId if not provided via props
          if (!formData.fromWalletId) {
            setFormData((prev) => ({
              ...prev,
              fromWalletId: apiResponse.data[0].walletId,
            }));
          }
        } else {
          setSnackbar({
            open: true,
            message: "No wallets found for this user.",
            severity: "warning",
          });
          setMessage("No wallets found for this user.");
        }
      })
      .catch((err) => {
        setSnackbar({
          open: true,
          message: err.response?.data?.message || "Error fetching wallets.",
          severity: "error",
        });
        console.error("Failed to fetch wallets:", err);
        setMessage(err.response?.data?.message || "Error fetching wallets.");
      })
      .finally(() => setLoading(false));
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.fromWalletId || !formData.toWalletId || !formData.amount) {
      setSnackbar({
        open: true,
        message: "Please fill all required fields.",
        severity: "error",
      });
      setMessage("Please fill all required fields.");
      return;
    }

    setLoading(true);
    try {
      // Get receiver email by walletId
      const emailRes = await axios.get(
        `http://localhost:8031/wallet/api/v1/details/${formData.toWalletId}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );
      const receiverEmail = emailRes.data.data?.buyerEmail;
      if (!receiverEmail) {
        throw new Error("Receiver wallet not found.");
      }

      const payload = {
        fromWalletId: formData.fromWalletId,
        toWalletId: formData.toWalletId,
        amount: parseFloat(formData.amount),
        remarks: formData.remarks,
        fromBuyerEmailId: fromBuyerEmail,
        toBuyerEmailId: receiverEmail,
      };
      console.log("Submitting transfer payload:", payload);

      const res = await axios.post(
        "http://localhost:8086/transaction/api/v2/transfer-wallet",
        payload,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setMessage(res.data.message || "Transaction successful!");
      setSnackbar({
        open: true,
        message: res.data.message || "Transaction successful!",
        severity: "success",
      });
      setFormData((prev) => ({ ...prev, toWalletId: "", amount: "", remarks: "" }));
   setTimeout(() => {
      navigate("/dashboard");
    }, 1000);
    
    } catch (err) {
      console.error("Transaction error:", err);
      setMessage(err.response?.data?.message || err.message || "Transaction failed.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {

    return (
      <div>
        <LoadingScreen/>
        <h6> Sending email to buyer</h6>
      </div>
    );
  }

  return (
    <div>
      <Navbar/>
    <form onSubmit={handleSubmit} className="add-money-form">
      <Box className="form-title">Transfer Money</Box>

      <div className="input-container">
        <TextField
          label="From Wallet ID"
          name="fromWalletId"
          value={formData.fromWalletId}
          fullWidth
          InputProps={{ readOnly: true }}
        />
      </div>

      <div className="input-container">
        <TextField
          required
          label="Receiver Wallet ID"
          name="toWalletId"
          value={formData.toWalletId}
          onChange={handleChange}
          fullWidth
        />
      </div>

      <div className="input-container">
        <TextField
          required
          label="Amount"
          name="amount"
          value={formData.amount}
          onChange={handleChange}
          placeholder="100.00"
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

      <div className="input-container">
        <TextField
          label="Remarks"
          name="remarks"
          value={formData.remarks}
          onChange={handleChange}
          placeholder="Optional Remarks"
          fullWidth
        />
      </div>

      <Button
        type="submit"
        variant="contained"
        endIcon={<ArrowForwardIcon />}
        sx={{
          width: "50%",
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
        // onClick={goToDashboard}
      >
        Submit
      </Button>

      {message && (
        <Box
          sx={{
            mt: 2,
            color: message.includes("successful") ? "green" : "red",
          }}
        >
          {message}
        </Box>
      )}
    </form>
    <Snackbar
        open={snackbar.open}
        autoHideDuration={4000}
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
    <Footer/>
    </div>
  );
}
