import { useState, useEffect, useMemo } from "react";
import axios from "axios";
import {
  Box,
  Typography,
  Button,
  Grid,
  Paper,
  CircularProgress,
} from "@mui/material";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  LineChart,
  Line,
} from "recharts";
import Footer from "../component/Footer";
import AdminNavbar from "./AdminNavbar";

// Simple StatCard using MUI Paper
function StatCard({ title, value, subtitle }) {
  return (

    <Paper elevation={2} sx={{ p: 2 }}>
      <Typography variant="subtitle2" color="textSecondary">
        {title}
      </Typography>
      <Typography variant="h5" fontWeight="bold">
        {value}
      </Typography>
      <Typography variant="caption" color="textSecondary">
        {subtitle}
      </Typography>
    </Paper>
  );
}

export function AdminDashboard() {
  const [data, setData] = useState([]); 
  const [period, setPeriod] = useState("monthly");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  //const userEmail = "antima@gmail.com";
  //const userRole = "ADMIN";
  const userEmail= sessionStorage.getItem("email")
  const userRole =sessionStorage.getItem("role")
  let token =sessionStorage.getItem("token")
  
  // Transform raw transactions to chart data
  const transformChartData = (transactions) => {
    if (!transactions || transactions.length === 0) return [];

    const groupBy =
      period === "daily"
        ? (t) => t.timestamp.slice(0, 10) // YYYY-MM-DD
        : (t) => t.timestamp.slice(0, 7); // YYYY-MM

    const chartData = transactions.reduce((acc, t) => {
      const key = groupBy(t);
      const existing = acc.find((d) => d.key === key);
      if (existing) {
        existing.count += 1;
        existing.total += t.amount;
      } else {
        acc.push({ key, count: 1, total: t.amount });
      }
      return acc;
    }, []);

    return chartData;
  };

  // Transform for Line chart (daily)
  const transformLineChartData = (transactions) => {
    if (!transactions || transactions.length === 0) return [];

    const chart = transactions.reduce((acc, t) => {
      const day = t.timestamp.slice(0, 10);
      const existing = acc.find((d) => d.key === day);
      if (existing) {
        existing.total += t.amount;
      } else {
        acc.push({ key: day, total: t.amount });
      }
      return acc;
    }, []);

    // sort by date ascending
    return chart.sort((a, b) => new Date(a.key) - new Date(b.key));
  };

  useEffect(() => {
    const fetchDashboardData = async () => {
      setLoading(true);
      setError(null);

      try {
        const response = await axios.get(
          `http://localhost:8086/transaction/api/v2/admin-transactions`,
          { params: { email: userEmail, role: userRole, period } ,
 headers: { "Content-Type": "application/json" , 
            Authorization: `Bearer ${token}`,}
        }
        );
       
        

        const rawData = Array.isArray(response.data)
          ? response.data
          : response.data.data || [];

      
        const chartData = transformChartData(rawData);
        const lineChartData = transformLineChartData(rawData);
         const totalTransactions = rawData.length;
        const totalVolume = rawData.reduce((sum, t) => sum + t.amount, 0);

        setData({
          chartData,
          lineChartData,
          totalTransactions,
          totalVolume: `₹${totalVolume.toLocaleString()}`,
        });
      } catch (err) {
        console.error(err);
        setError(err.message || "Failed to load data");
        setData([]);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, [period]);

 
  const chartData = useMemo(() => data?.chartData || [], [data]);
  const lineChartData = useMemo(() => data?.lineChartData || [], [data]);

  return (
    <div>
      <AdminNavbar/>
    <Box sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
       Transaction reports and analysis insights
      </Typography>
     
      {/* Period Selector */}
      <Box sx={{ display: "flex", gap: 1, mb: 2 }}>
        {["daily", "monthly"].map((p) => (
          <Button
            key={p}
            className="submit-button contained"
            variant={period === p ? "contained" : "outlined"}
            onClick={() => setPeriod(p)}
          >
            {p.charAt(0).toUpperCase() + p.slice(1)}
          </Button>
        ))}
      </Box>

      {/* Error */}
      {error && (
        <Paper sx={{ p: 2, mb: 2, bgcolor: "#fff3cd" }}>
          <Typography color="textSecondary">{error}</Typography>
        </Paper>
      )}

      {/* Stats Cards */}
      <Grid container spacing={2} mb={3}>
        <Grid item xs={12} md={6}>
          <StatCard
            title="Total Transactions"
            value={data?.totalTransactions?.toLocaleString() || "0"}
            subtitle={`Last ${period}`}
          />
        </Grid>
        <Grid item xs={12} md={6}>
          <StatCard
            title="Total Volume"
            value={data?.totalVolume || "₹0"}
            subtitle={`Total amount processed`}
          />
        </Grid>
      </Grid>

      {/* Bar Chart */}
      <Paper sx={{ p: 3, mb: 4 }}>
        <Typography variant="h6" gutterBottom>
          {period === "daily" ? "Daily Transactions" : "Monthly Transactions"}
        </Typography>

        {loading ? (
          <Box
            sx={{
              height: 320,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <CircularProgress />
          </Box>
        ) : chartData.length > 0 ? (
          <ResponsiveContainer width="100%" height={320}>
            <BarChart
              data={chartData}
              margin={{ top: 20, right: 30, left: 0, bottom: 20 }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
              <XAxis dataKey="key" stroke="#555" />
              <YAxis stroke="#555" />
              <Tooltip />
              <Legend />
              <Bar
                dataKey="count"
                fill="#1976d2"
                radius={[8, 8, 0, 0]}
                name="Transaction Count"
              />
              <Bar
                dataKey="total"
                fill="#0f3a6e"
                radius={[8, 8, 0, 0]}
                name="Total Amount"
              />
            </BarChart>
          </ResponsiveContainer>
        ) : (
          <Box
            sx={{
              height: 320,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Typography color="textSecondary">
              No chart data available
            </Typography>
          </Box>
        )}
      </Paper>

      {/* Line Chart */}
      <Paper sx={{ p: 3 }}>
        <Typography variant="h6" gutterBottom>
          Transaction Volume (Daily)
        </Typography>

        {loading ? (
          <Box
            sx={{
              height: 320,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <CircularProgress />
          </Box>
        ) : lineChartData.length > 0 ? (
          <ResponsiveContainer width="100%" height={320}>
            <LineChart
              data={lineChartData}
              margin={{ top: 20, right: 30, left: 0, bottom: 20 }}
            >
              <CartesianGrid strokeDasharray="3 3" stroke="#e0e0e0" />
              <XAxis dataKey="key" stroke="#555" />
              <YAxis stroke="#555" />
              <Tooltip />
              <Legend />
              <Line
                type="monotone"
                dataKey="total"
                stroke="#0f3a6e"
                name="Total Volume"
                strokeWidth={2}
              />
            </LineChart>
          </ResponsiveContainer>
        ) : (
          <Box
            sx={{
              height: 320,
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Typography color="textSecondary">No data available</Typography>
          </Box>
        )}

      </Paper>
      {/* KPI Cards */}
      <Grid container spacing={2} mt={2}>
        <Grid item xs={12} md={4}>
          <StatCard
            title="Average Transaction Value"
            value={
              data?.totalTransactions
                ? `₹${(
                    parseFloat(data.totalVolume.replace(/[₹,]/g, "")) /
                    data.totalTransactions
                  ).toFixed(2)}`
                : "₹0"
            }
            subtitle="Average per transaction"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard
            title="Highest Transaction Day"
            value={
              data?.lineChartData && data.lineChartData.length > 0
                ? data.lineChartData.reduce((max, curr) =>
                    curr.total > max.total ? curr : max
                  ).key
                : "N/A"
            }
            subtitle="Day with maximum total volume"
          />
        </Grid>
        <Grid item xs={12} md={4}>
          <StatCard
            title="Peak Volume"
            value={
              data?.lineChartData && data.lineChartData.length > 0
                ? `₹${data.lineChartData
                    .reduce((max, curr) => (curr.total > max.total ? curr : max))
                    .total.toLocaleString()}`
                : "₹0"
            }
            subtitle="Highest daily total"
          />
        </Grid>
      </Grid>
    </Box>
    <Footer/>
    </div>
  );
}