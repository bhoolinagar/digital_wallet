//  This will hold the sidebar and render the selected page inside it.

import { useState } from "react";
import { Drawer, List, ListItem, ListItemButton, ListItemText, Box, Toolbar, Typography } from "@mui/material";
import { useNavigate, Outlet, useLocation } from "react-router-dom";

const drawerWidth = 220;

export function AdminLayout() {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
  { text: "Dashboard", path: "/admin", exact: true },
  { text: "Reports", path: "/admin/reports" },
];

  return (
    <Box sx={{ display: "flex" }}>
      {/* Sidebar */}
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: {
            width: drawerWidth,
            boxSizing: "border-box",
            bgcolor: "white",
            color: "#0f3a6e",
          },
        }}
      >
        <Toolbar>
          <Typography variant="h6" sx={{ fontWeight: "bold" }}>
            Admin Panel
          </Typography>
        </Toolbar>
        <List>
          {menuItems.map((item) => (
            <ListItem key={item.text} disablePadding>
              <ListItemButton
                selected={location.pathname === item.path}
                onClick={() => navigate(item.path)}
                sx={{
                  color: location.pathname === item.path ? "#0c284aff" : "white",
                  fontWeight: "bold",
                  bgcolor:
                    location.pathname === item.path ? "white" : "transparent",
                  borderRadius: 1,
                  mx: 1,
                  my: 0.5,
                  "&:hover": {
                    bgcolor:
                      location.pathname === item.path
                        ? "white"
                        : "rgba(255,255,255,0.15)",
                  },
                }}
              >
                <ListItemText
                  primary={item.text}
                  primaryTypographyProps={{
                    fontWeight:
                      location.pathname === item.path ? "bold" : "normal",
                  }}
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>
      </Drawer>

      {/* Main Content */}
      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar /> {/* Adds top padding to match drawer */}
        <Outlet /> {/* Renders the selected route (Dashboard or Reports) */}
      </Box>
    </Box>
  );
}
