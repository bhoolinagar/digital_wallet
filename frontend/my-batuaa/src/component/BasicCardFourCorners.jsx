import * as React from 'react';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';

const bull = (
  <Box
    component="span"
    sx={{ display: 'inline-block', mx: '2px', transform: 'scale(0.8)' }}
  >
    •
  </Box>

  
);

export default function BasicCardFourCorners(props) {
  return (
<Card sx={{ width: 800, padding: 2 }}>
      <Box sx={{ display: "flex", flexDirection: "row", alignItems: "center", gap: 2 }}>
        {/* Arrow image left */}
        <img src="https://upload.wikimedia.org/wikipedia/commons/7/7e/Arrow_east.svg" alt="Arrow" style={{ width: 32, height: 32 }} />
        {/* Left side */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
          <Typography variant="body2" align="left" sx={{ fontWeight: "bold", color: "black" }}>{props.transaction.status}</Typography>
          <Typography variant="body2" align="left">           {props.transaction.remarks}
             </Typography>
        </Box>
        {/* Spacer */}
        <Box sx={{ flexGrow: 1 }} />
        {/* Right side */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 1 }}>
          <Typography variant="body2" align="right" sx={{ color: "green" }}>{props.transaction.amount}</Typography>
          <Typography variant="body2" align="right"> {props.transaction.timestamp}
            </Typography>
        </Box>
      </Box>
    </Card>  );
}