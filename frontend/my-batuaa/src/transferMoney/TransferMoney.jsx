import React, { useState, useEffect } from "react";
import {
  Box,
  TextField,
  InputAdornment,
  Button,
  CircularProgress,
} from "@mui/material";
import CurrencyRupeeIcon from "@mui/icons-material/CurrencyRupee";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import axios from "axios";
import "../dashboard/AddMoney.css";

export default function TransferMoney() {
    // using state to handle loading spinner
  const [loading, setLoading] = useState(true);
    // using state to store list of user's wallets
  const [wallets, setWallets] = useState([]);
//   using state to store form input values
  const [formData, setFormData] = useState({
    fromWalletId: "",
    toWalletId: "",
    amount: "",
    remarks: "",
  });
//    state to display messages (errors/success)
  const [message, setMessage] = useState("");
//  currently Hardcoded the sender email, later this will be taken from session storage
  const fromBuyerEmail = "bhoolinagar@gmail.com";

//  pulling the user's wallet list when component initailly moounts
  useEffect(() => {
    axios

      .get(`http://localhost:8031/wallet/api/v1/wallet-list/${fromBuyerEmail}`)
      .then((res) => {
        const apiResponse = res.data;
        console.log("API Response:", res.data);

        //  if wallets exist, set first wallet as default "fromWalletId" a
        // s my current logic assumes that there is at present only 1 wallet in the list, 
        // logic can be modified based on primary.jsx
        if (apiResponse.status === "success" && apiResponse.data.length > 0) {
          setWallets(apiResponse.data);
          setFormData((prev) => ({
            ...prev,
            fromWalletId: apiResponse.data[0].walletId,
          }));
        } else {
          setMessage("No wallets found for this user.");
        }
      })
      .catch((err) => {
        const errMsg = err.response?.data?.message || "Error fetching wallets";
        setMessage(errMsg);
      })
    //  Hiding loading spinner after request
      .finally(() => setLoading(false));  
  }, []);

//   Handling form input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };
//  handling form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    //  added basic validation. 
    if (!formData.fromWalletId || !formData.toWalletId || !formData.amount) {
      setMessage("Please fill all fields.");
      return;
    }

    setLoading(true);
    try {
      // getting my receiver email using walletId
      const emailRes = await axios.get(
        `http://localhost:8031/wallet/api/v1/details/${formData.toWalletId}`
      );
      
      const receiverEmail = emailRes.data.data.buyerEmail;
      console.log("Receiver Email:", receiverEmail);

      // Building payload after fetching receiver email
      const payload = {
        fromWalletId: formData.fromWalletId,
        toWalletId: formData.toWalletId,
        amount: parseFloat(formData.amount),
        remarks: formData.remarks,
        fromBuyerEmailId: fromBuyerEmail,
        toBuyerEmailId: receiverEmail,
      };
      console.log("Submitting payload:", payload);

    //    generating transaction
      const res = await axios.post(
        "http://localhost:8086/transaction/api/v2/transfer-wallet",
        payload
      );

      setMessage(res.data.message);

      // reste form 
      setFormData((prev) => ({
        ...prev,
        toWalletId: "",
        amount: "",
        remarks: "",
      }));
    } catch (err) {
      const errMsg = err.response?.data?.message || "Transaction failed.";
      setMessage(errMsg);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <CircularProgress sx={{ mt: 10, color: "#0F3A6E" }} />;

  return (
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
      >
        Submit
      </Button>
{/* Displaying  message */} 
      {message && (
        <Box
          sx={{
            mt: 2,
            color: message.startsWith("Transaction") ? "green" : "red",
          }}
        >
          {message}
        </Box>
      )}
    </form>
  );
}