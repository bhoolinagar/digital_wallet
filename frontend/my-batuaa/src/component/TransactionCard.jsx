import { useEffect, useState } from "react";
import axios from "axios";
import BasicCardFourCorners from "./BasicCardFourCorners";
import { Box, IconButton, Stack, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import {
  Card,
  CardContent,
  TextField,
  MenuItem,
  Divider,
  Button,  Snackbar
} from "@mui/material";
import MuiAlert from "@mui/material/Alert";
import { useParams } from "react-router-dom";

export default function TransactionCard({ primaryWallet }) {
  const token = sessionStorage.getItem("token");
  const emailId = sessionStorage.getItem("email");
  const [transactions, setTransactions] = useState([]);
const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success", // success | error
  });
  const [error, setError] = useState("");

const navigate = useNavigate();

//navigate to primary wallet
const goToTranscationFilter = (primarywallet) => {
   if (!primarywallet) return; 
  navigate(`/transactionfilter/${primarywallet}`);
  };


  useEffect(() => {
    const fetchTransactions = async () => {
      if (!primaryWallet) return;
      const walletId = primaryWallet?.walletId || primaryWallet;
      console.log("Transactions primary:", walletId);

      try {
        const res = await axios.get("http://localhost:8086/transaction/api/v2/all-transactions", {
          params: { emailId, walletId },
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
        setTransactions(res.data);
        console.log("Transaction data loaded:", res.data);
      
       setSnackbar({
      open: true,
      message: res.data.message || "Transactions fetched successfully!",
      severity: "success",
    });
    setError("");
      } catch (err) {
         setTransactions([]);
         setError("Failed to load transactions. Please try again later.");
        console.error("Error fetching transactions:", err);
     setSnackbar({
      open: true,
      message:
        err.response?.data?.message ||
        "Failed to fetch transactions. Please try again.",
      severity: "error",
    });
      }
    };

    fetchTransactions();
  }, [primaryWallet, emailId, token]);

  const handleCloseSnackbar = (_, reason) => {
    if (reason === "clickaway") return;
    setSnackbar({ ...snackbar, open: false });
  };

  return (
    <div>
    
      <Typography variant="h5" sx={{ mb: 2 }} align="left">
        Recent Transactions History
      </Typography>

      <Box sx={{ backgroundColor: "#f5f5f5", borderRadius: 2, p: 4, width: "100%" }}>
        <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
         
         <IconButton onClick={()=>goToTranscationFilter(primaryWallet?.walletId|| primaryWallet)}>
View all
         </IconButton>
        </Box>

        <Stack spacing={3} sx={{ mt: 4 }}>
          {transactions.map((transaction, index) => (
            <BasicCardFourCorners key={index} transaction={transaction} />
          ))}
        </Stack>
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
}
