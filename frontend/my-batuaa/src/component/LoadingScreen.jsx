import { Box, CircularProgress, Typography } from "@mui/material";

export default function LoadingScreen() {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column", // stack vertically
        justifyContent: "center",
        alignItems: "center",
        height: "100vh", // full screen height
        textAlign: "center",
        backgroundColor: "#f9f9f9",
      }}
    >
      <CircularProgress size={60} thickness={5} color="primary" />
       <Typography
        variant="h6"
        sx={{
          mt: 3,
          fontWeight: 500,
          color: "#0F3A6E",
        }}
      >Sending email to buyer
      </Typography>
      <Typography
        variant="h6"
        sx={{
          mt: 3,
          fontWeight: 500,
          color: "#0F3A6E",
        }}
      >
        Loading, please wait...
      </Typography>
      
    </Box>
  );
}
