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
import { useParams } from "react-router-dom";
import Navbar from "../Navbar";
import Footer from "./Footer";
const BASE_URL = "http://localhost:8086/transaction/api/v2";
const Transactions = ({ emailId=sessionStorage.getItem("email") }) => {
  let token =sessionStorage.getItem("token")
  
  //const {primarywallet} = useParams(); 
  let walletId = sessionStorage.getItem("walletId")
  const [transactions, setTransactions] = useState([]);
  const [filter, setFilter] = useState("");
  const [typefilter, setType] = useState("All Types");
  const [sort, setSort] = useState("Recent First");
  const [error, setError] = useState("");
  const [fromDate, setFromDate] = useState("");
  const [toDate, setToDate] = useState("");

   const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success", // success | error
  });

//console.log("P dal: "+ walletId)
  useEffect(() => {
    if(!token) return
    if (walletId) {
   // fetchTransactions(primaryWallet.walletId);
    loadTransactions();
  }
    
  }, [emailId,token,typefilter, filter, fromDate, toDate,]);
  // const params = { primaryWallet.walletId , emailId};
  // Fetch transactions from backend with optional filters

   
  const loadTransactions = async () => {
  if (!walletId || !emailId) return;

  try {
    let res;

    // Base headers
    const headers = {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    };

    // Filter by type
    if (typefilter && typefilter !== "All Types") {
      console.log("Filtering by type:", typefilter);
      const payload = { walletId, emailId, type: typefilter };

      res = await axios.post(`${BASE_URL}/filter-by-type`, payload, { headers });
      setTransactions(res.data.data);
    }

    //  Filter by remark
    else if (filter) {
      console.log("Filtering by remark:", filter);
      const payload = { walletId, emailId, remark: filter };

      res = await axios.post(`${BASE_URL}/view-transactions-by-remark`, payload, { headers });
      setTransactions(res.data.data);
    }

    //  Filter by date
    else if (fromDate && toDate) {
      console.log("Filtering by date range:", fromDate, "to", toDate);
      const payload = { walletId, emailId, startDate: fromDate, endDate: toDate };

      res = await axios.post(`${BASE_URL}/filter-by-date`, payload, { headers });
      setTransactions(res.data.data);
    }

    //  Default: all transactions
    else {
      console.log("Fetching all transactions");
      res = await axios.get(`${BASE_URL}/all-transactions`, {
        params: { emailId, walletId },
        headers,
      });
      setTransactions(res.data.data || res.data);
    }

    setSnackbar({
      open: true,
      message: res.data.message || "Transactions fetched successfully!",
      severity: "success",
    });
    setError("");

  } catch (err) {
    console.error("Error fetching transactions", err);
    setError("Failed to load transactions. Please try again later.");
    setTransactions([]);
    setSnackbar({
      open: true,
      message:
        err.response?.data?.message ||
        "Failed to fetch transactions. Please try again.",
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
      <Navbar/>
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
            value={typefilter}
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
                <Typography className="transaction-type" sx={{ fontWeight: 600,
              fontSize: "1.05rem",
                  color:item.type === "RECEIVED" ? "#2e7d32" : "#c62828"}}>{item.type}</Typography>
               <Typography variant="caption" color="textSecondary">
                wallet ID: {item.toWallet.walletId}
              </Typography>
                <Typography variant="body2" color="textSecondary" align="left">
                          Name: {item?.toWallet?.buyer?.name || " "}
                        </Typography>
            
              <Typography variant="body2" color="textSecondary" className="remarks">
                remarks 
                {item?.remarks ||'-'}
              </Typography>
             
            </Box>

            <Box className="amount-section">
              <Typography  sx={{
              fontWeight: 600,
              fontSize: "1.05rem",
              color: item.type === "RECEIVED" ? "#2e7d32" : "#c62828",
            }}
              className={`amount ${item.type === "RECEIVED" ? "RECEIVED" : "WITHDRAWN"}`}>
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
           <Footer/>
    </div>
  );
};

export default Transactions;
