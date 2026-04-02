import MenuIcon from '@mui/icons-material/Menu';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import Toolbar from '@mui/material/Toolbar';
import { useState } from 'react';

import Header from '../components/Header';
import SideMenu from '../components/SideMenu';

const DRAWER_WIDTH = 240;
const COLLAPSED_WIDTH = 68;

export default function Scene({ children }) {
  const [mobileOpen, setMobileOpen] = useState(false);
  const [desktopOpen, setDesktopOpen] = useState(true);
  const [isClosing, setIsClosing] = useState(false);

  const handleDrawerClose = () => {
    setIsClosing(true);
    setMobileOpen(false);
  };

  const handleDrawerTransitionEnd = () => setIsClosing(false);

  const handleDrawerToggle = () => {
    if (window.innerWidth < 600) {
      if (!isClosing) setMobileOpen((prev) => !prev);
    } else {
      setDesktopOpen((prev) => !prev);
    }
  };

  const drawerContent = <SideMenu open={desktopOpen} />;

  return (
    <Box sx={{ display: 'flex', minHeight: '100vh', bgcolor: '#f0f2f5' }}>
      {/* Top AppBar */}
      <AppBar
        position='fixed'
        elevation={0}
        sx={{
          bgcolor: '#fff',
          borderBottom: '1px solid rgba(0,0,0,0.07)',
          color: 'text.primary',
          zIndex: (theme) => theme.zIndex.drawer + 1
        }}
      >
        <Toolbar sx={{ gap: 1 }}>
          <IconButton
            edge='start'
            onClick={handleDrawerToggle}
            sx={{ mr: 1, color: '#374151' }}
          >
            <MenuIcon />
          </IconButton>
          <Header />
        </Toolbar>
      </AppBar>

      {/* Sidebar */}
      <Box
        component='nav'
        sx={{ 
          width: { sm: desktopOpen ? DRAWER_WIDTH : COLLAPSED_WIDTH }, 
          flexShrink: { sm: 0 },
          transition: 'width 0.2s ease'
        }}
      >
        {/* Mobile drawer */}
        <Drawer
          variant='temporary'
          open={mobileOpen}
          onTransitionEnd={handleDrawerTransitionEnd}
          onClose={handleDrawerClose}
          ModalProps={{ keepMounted: true }}
          sx={{
            display: { xs: 'block', sm: 'none' },
            '& .MuiDrawer-paper': {
              width: DRAWER_WIDTH,
              boxSizing: 'border-box',
              border: 'none',
              boxShadow: '4px 0 16px rgba(0,0,0,0.06)'
            }
          }}
        >
          {<SideMenu open={true} />}
        </Drawer>

        {/* Desktop permanent drawer */}
        <Drawer
          variant='permanent'
          sx={{
            display: { xs: 'none', sm: 'block' },
            '& .MuiDrawer-paper': {
              width: desktopOpen ? DRAWER_WIDTH : COLLAPSED_WIDTH,
              transition: 'width 0.2s ease',
              overflowX: 'hidden',
              boxSizing: 'border-box',
              border: 'none',
              borderRight: '1px solid rgba(0,0,0,0.07)',
              bgcolor: '#fff'
            }
          }}
          open
        >
          {drawerContent}
        </Drawer>
      </Box>

      {/* Main content */}
      <Box
        component='main'
        sx={{
          flexGrow: 1,
          width: { sm: `calc(100% - ${desktopOpen ? DRAWER_WIDTH : COLLAPSED_WIDTH}px)` },
          transition: 'width 0.2s ease',
          display: 'flex',
          flexDirection: 'column'
        }}
      >
        <Toolbar /> {/* spacer for fixed AppBar */}
        {children}
      </Box>
    </Box>
  );
}
