import * as React from "react";
import {
  Box,
  Card,
  Typography,
  IconButton
} from "@mui/material";
import { CallMadeRounded, CallReceivedRounded } from "@mui/icons-material";
import "./TransactionHistory.css";

export default function BasicCardFourCorners({ transaction }) {
  if (!transaction) return null;

  // Determine wallet details based on type
  const isReceived = transaction.type === "RECEIVED";
  const walletInfo = isReceived ? transaction.fromWallet : transaction.toWallet;

  return (
    <Card sx={{ width: "90%", p: 2, mb: 1 }} className="transaction-item">
      <Box
        sx={{
          display: "flex",
          flexDirection: "row",
          alignItems: "center",
          gap: 2,
        }}
      >
        {/* 🔹 Transaction icon */}
        <Box className="icon-section">
          <IconButton
            className={`icon ${isReceived ? "RECEIVED" : "WITHDRAWN"}`}
            size="small"
          >
            {isReceived ? (
              <CallReceivedRounded color="success" />
            ) : (
              <CallMadeRounded color="error" />
            )}
          </IconButton>
        </Box>

        {/* 🔹 Left section (details) */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 0.5 }}>
          <Typography
            align="left"
            fontWeight={600}
            color={isReceived ? "green" : "red"}
          >
            {transaction.type || "-"}
          </Typography>

          <Typography variant="body2" color="textSecondary" align="left">
            Wallet ID: {walletInfo?.walletId || "N/A"}
          </Typography>

          <Typography variant="body2" color="textSecondary" align="left">
            Name: {walletInfo?.buyer?.name || "Unknown"}
          </Typography>

          <Typography
            variant="body2"
            color="textSecondary"
            align="left"
            className="remarks"
          >
            Remarks: {transaction?.remarks || "-"}
          </Typography>
        </Box>

        {/* Spacer */}
        <Box sx={{ flexGrow: 1 }} />

        {/* 🔹 Right section (amount + time) */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 0.5 }}>
          <Typography
            className={`amount ${isReceived ? "RECEIVED" : "WITHDRAWN"}`}
            align="right"
            sx={{
              fontWeight: 600,
              fontSize: "1.05rem",
              color: isReceived ? "#2e7d32" : "#c62828",
            }}
          >
            {isReceived ? "+" : "-"} ₹
            {transaction.amount ? transaction.amount.toFixed(2) : "0.00"}
          </Typography>

          <Typography variant="caption" align="right" color="textSecondary">
            {new Date(transaction.timestamp).toLocaleString(undefined, {
              month: "short",
              day: "2-digit",
              hour: "2-digit",
              minute: "2-digit",
              hour12: true,
            })}
          </Typography>
        </Box>
      </Box>
    </Card>
  );
}
