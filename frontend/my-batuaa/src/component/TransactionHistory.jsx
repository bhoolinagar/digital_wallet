import axios from "axios";
import React, { useEffect, useState } from "react";
import {
  Card,
  CardContent,
  Typography,
  TextField,
  MenuItem,
  Box,
  Divider,
  IconButton,
  Button,  Snackbar
} from "@mui/material";
import { CallMadeRounded, CallReceivedRounded } from "@mui/icons-material";
import "./TransactionHistory.css";
import MuiAlert from "@mui/material/Alert";
import PrimaryWallet from "./Primary";
const BASE_URL = "http://localhost:8086/transaction/api/v2";

const Transactions = ({ emailId=sessionStorage.getItem("email"),  primaryWallet }) => {
  let token =sessionStorage.getItem("token")
  
  const [transactions, setTransactions] = useState([]);
  const [filter, setFilter] = useState("");
  const [type, setType] = useState("All Types");
  const [sort, setSort] = useState("Recent First");
  const [error, setError] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");

   const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success", // success | error
  });

console.log("P dal: "+ primaryWallet)
  useEffect(() => {
    if (primaryWallet) {
   // fetchTransactions(primaryWallet.walletId);
    loadTransactions();
  }
    
  }, [primaryWallet,emailId,token,type, filter, fromDate, toDate,]);
  // const params = { primaryWallet.walletId , emailId};
  // Fetch transactions from backend with optional filters

   
  const loadTransactions = async () => {
    if (!primaryWallet) return;
   const walletId = primaryWallet;
    try {
   
      let res
      if (type && type !== "All Types") {
       console.log("Filtering by type:", type);  
        params.type = type;    
       res = await axios.post(`${BASE_URL}/filter-by-type`, { walletId, emailId,type },
        { 
          headers: { "Content-Type": "application/json",Authorization: `Bearer ${token}`, } });
       setTransactions(res.data.data);
       console.log("Fetched data: ", res.data)
      setSnackbar({
        open: true,
        message: res.data.message || "Transactions fetched successfully!",
        severity: "success",
      });
     return;
      }
      if (filter) {
       const remark = filter;
         console.log("Filtering by filter:", filter);  
           
       res = await axios.post(`${BASE_URL}/view-transactions-by-remark`, { walletId,emailId,remark },
        { 
          headers: { "Content-Type": "application/json",
            Authorization: `Bearer ${token}`, } });
       setTransactions(res.data.data);
       console.log("Fetched data:  by remarks", res.data)
       
       setSnackbar({
        open: true,
        message: res.data.message || "Transactions fetched successfully!",
        severity: "success",
      });
      return;
      }
      
        //to filter transactions by custom date range 
      if (fromDate && toDate) {
      const startDate = fromDate;
       const endDate=toDate;
         console.log("Filtering by date:", fromDate);  
           
       res = await axios.post(`${BASE_URL}/filter-by-date`, 
        { walletId,emailId,startDate,endDate },
        { 
          headers: { "Content-Type": "application/json" ,Authorization: `Bearer ${token}`,} });
       setTransactions(res.data.data);
       console.log("Fetched data:  by bate", res.data)
      setSnackbar({
        open: true,
        message: res.data.message || "Transactions fetched successfully!",
        severity: "success",
      });
      return;
      }
    
      else{
      res = await axios.get(`${BASE_URL}/all-transactions`, { params },{ 
          headers: { 
            Authorization: `Bearer ${token}`,} });
     console.log("Fetched data: ", res.data)
      setTransactions(res.data);
      setSnackbar({
        open: true,
        message: res.data.message || "Transactions fetched successfully!",
        severity: "success",
      });
    }
      setError("");
    } catch (err) {
      console.error("Error fetching transactions", err);
      setError(res.data.message)
     // setError("Failed to load transactions. Please try again later.");
      setTransactions([]);
      setSnackbar({
        open: true,
        message:
          error.res?.data?.message ||
          "Failed to fetched transaction. Please try again.",
        severity: "error",
      });
    }
  };

  // Clear all filters
  const clearFilters = () => {
    setFilter("");
    setType("All Types");
    setFromDate("");
    setToDate("");
    setSort("Recent First");
    loadTransactions();
  };

  //Apply frontend sort only
  const sortedTransactions = [...transactions].sort((a, b) => {
    if (sort === "Recent First") return new Date(b.timestamp) - new Date(a.timestamp);
    if (sort === "Oldest First") return new Date(a.timestamp) - new Date(b.timestamp);
    if (sort === "High Amount First") return b.amount - a.amount;
    if (sort === "Low Amount First") return a.amount - b.amount;
    return 0;
  });

const handleCloseSnackbar = (_, reason) => {
    if (reason === "clickaway") return;
    setSnackbar({ ...snackbar, open: false });
  };

  return (
    <div>
    <Box className="transaction-container">
      <Typography variant="h5" className="transaction-title">
        Transaction History
      </Typography>
      <Typography variant="body2" color="textSecondary" className="transaction-subtitle">
        View and manage all your transactions
      </Typography>

      <Card className="filter-card">
        <CardContent className="filter-content">
          <TextField
            label="Search by Remarks"
            variant="outlined"
            size="small"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
            className="filter-input"
          />

          <TextField
            select
            label="Transaction Type"
            size="small"
            value={type}
            onChange={(e) => setType(e.target.value)}
            className="filter-select"
          >
            <MenuItem value="All Types">All Types</MenuItem>
            <MenuItem value="RECEIVED">RECEIVED</MenuItem>
            <MenuItem value="WITHDRAWN">WITHDRAWN</MenuItem>
          </TextField>

          <TextField
            select
            label="Sort By"
            size="small"
            value={sort}
            onChange={(e) => setSort(e.target.value)}
            className="filter-select"
          >
            <MenuItem value="Recent First">Recent First</MenuItem>
            <MenuItem value="Oldest First">Oldest First</MenuItem>
            <MenuItem value="High Amount First">High Amount First</MenuItem>
            <MenuItem value="Low Amount First">Low Amount First</MenuItem>
          </TextField>

          {/* Date Range */}
          <TextField
            label="From"
            type="date"
            size="small"
            InputLabelProps={{ shrink: true }}
            value={fromDate}
            onChange={(e) => setFromDate(e.target.value)}
            className="filter-select"
          />
          <TextField
            label="To"
            type="date"
            size="small"
            InputLabelProps={{ shrink: true }}
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            className="filter-select"
          />

          {/* Buttons */}
         <Box className="filter-buttons">
  <Button 
    className="submit-button contained"
  
    onClick={loadTransactions}
  >
    Apply Filter
  </Button>
  <Button 
    className="clear-button contained"

   
    onClick={clearFilters}
  >
    Clear Filters
  </Button>
</Box>
        </CardContent>
      </Card>

      {error && <Typography color="error" sx={{ mt: 2 }}>{error}</Typography>}

      {sortedTransactions.length === 0 && !error && (
        <Typography sx={{ mt: 3 }} color="textSecondary">
          No transactions found.
        </Typography>
      )}

      {sortedTransactions.map((item, idx) => (
        <Card key={idx} className="transaction-item">
          <CardContent className="transaction-row">
            <Box className="icon-section">
              <IconButton
                className={`icon ${item.type === "RECEIVED" ? "RECEIVED" : "WITHDRAWN"}`}
                size="small"
              >
                {item.type === "RECEIVED" ? (
                  <CallReceivedRounded color="success" />
                ) : (
                  <CallMadeRounded color="error" />
                )}
              </IconButton>
            </Box>

            <Box className="transaction-section">
               <Typography variant="caption" color="textSecondary">
                ID: {item.transactionId}
              </Typography>
              <Typography className="transaction-type">{item.type}</Typography>
              <Typography variant="body2" color="textSecondary" className="remarks">
                {item.remarks}
              </Typography>
             
            </Box>

            <Box className="amount-section">
              <Typography className={`amount ${item.type === "RECEIVED" ? "RECEIVED" : "WITHDRAWN"}`}>
                {item.type === "RECEIVED" ? "+" : "-"} ₹{item.amount.toFixed(2)}
              </Typography>
              <Typography variant="caption" color="textSecondary" >
                {new Date(item.timestamp).toLocaleString(undefined, {
                  month: "short",
                  day: "2-digit",
                  hour: "2-digit",
                  minute: "2-digit",
                })}
              </Typography>
            </Box>
          </CardContent>
          <Divider />
        </Card>
      ))}
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
    </div>
  );
};

export default Transactions;
