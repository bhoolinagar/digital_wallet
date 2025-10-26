//  This page allows downloading all transactions as CSV.

import axios from "axios";
import { Box, Typography, Button, Paper } from "@mui/material";
import DownloadIcon from "@mui/icons-material/Download";

export function Reports() {
  const handleDownload = async () => {
    try {
      const response = await axios.get(
        "http://localhost:8086/transaction/api/v2/admin-transactions",
        { params: { email: "antima@gmail.com", role: "ADMIN" } }
      );

      // Extracting the array
      const transactions = Array.isArray(response.data)
        ? response.data
        : response.data.data || [];

      if (!transactions.length) {
        alert("No data available to download.");
        return;
      }

      // Convert JSON to CSV
      const csvHeaders = Object.keys(transactions[0]);
      const csvRows = [
        csvHeaders.join(","), 
        ...transactions.map((t) =>
          csvHeaders.map((key) => JSON.stringify(t[key] ?? "")).join(",")
        ),
      ];
      const csvContent = csvRows.join("\n");

      // Create downloadable file
      const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
      const url = window.URL.createObjectURL(blob);

      // Trigger browser download
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "transactions_report.csv");
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Error downloading report:", error);
      alert("Failed to download report. Check console for details.");
    }
  };

  return (
    <Paper sx={{ p: 4 }}>
      <Typography variant="h4" gutterBottom sx={{fontWeight: "bold"}}>
        Reports
      </Typography>
      <Typography variant="body2" color="textSecondary" gutterBottom >
        Download complete transaction details as a CSV file for analysis or audit.
      </Typography>

      <Box sx={{ mt: 3 }}>
        <Button
          variant="contained"
          color="primary"
          startIcon={<DownloadIcon />}
          onClick={handleDownload}
        >
          Download CSV
        </Button>
      </Box>
    </Paper>
  );
}
