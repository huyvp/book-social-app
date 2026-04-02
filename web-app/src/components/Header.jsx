import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import EmailIcon from '@mui/icons-material/Email';
import LogoutIcon from '@mui/icons-material/Logout';
import NotificationsIcon from '@mui/icons-material/Notifications';
import PersonIcon from '@mui/icons-material/Person';
import {
  Avatar,
  Badge,
  Box,
  Divider,
  ListItemIcon,
  Menu,
  MenuItem,
  Tooltip,
  Typography
} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { isAuthenticated, logOut } from '../services/authenticationService';
import { getMyInfo } from '../services/userService';

const ACCENT = '#1e293b';

export default function Header() {
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    if (isAuthenticated()) {
      getMyInfo()
        .then((res) => {
          if (res?.data?.result) {
            setUser(res.data.result);
          }
        })
        .catch(() => {
          // Silent catch in header if token expires, main pages usually handle logout
        });
    }
  }, []);

  const handleOpenMenu = (event) => setAnchorEl(event.currentTarget);
  const handleCloseMenu = () => setAnchorEl(null);

  const handleProfile = () => {
    handleCloseMenu();
    navigate('/profile');
  };

  const handleLogout = () => {
    handleCloseMenu();
    logOut();
    navigate('/login');
  };

  return (
    <>
      {/* Logo */}
      <Box
        onClick={() => navigate('/')}
        sx={{
          display: 'flex',
          alignItems: 'center',
          gap: 1,
          cursor: 'pointer',
          userSelect: 'none'
        }}
      >
        <Box
          component='img'
          src='/logo/200.png'
          sx={{ width: 32, height: 32, borderRadius: '8px' }}
        />
        <Typography
          sx={{
            fontWeight: 800,
            fontSize: '1.2rem',
            letterSpacing: '-0.5px',
            color: ACCENT,
            display: { xs: 'none', sm: 'block' }
          }}
        >
          _sudo.dan
        </Typography>
      </Box>

      <Box sx={{ flexGrow: 1 }} />

      {/* Right-side actions */}
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
        {/* Email / Messages */}
        <Tooltip title='Messages'>
          <IconButton color='inherit' size='medium'>
            <Badge color='error' variant='dot'>
              <EmailIcon sx={{ fontSize: 22 }} />
            </Badge>
          </IconButton>
        </Tooltip>

        {/* Notifications */}
        <Tooltip title='Notifications'>
          <IconButton color='inherit' size='medium'>
            <Badge color='error' variant='dot'>
              <NotificationsIcon sx={{ fontSize: 22 }} />
            </Badge>
          </IconButton>
        </Tooltip>

        {/* Avatar / Profile menu */}
        <Tooltip title='My account'>
          <IconButton onClick={handleOpenMenu} size='small' sx={{ ml: 0.5 }}>
            <Avatar
              src={user?.avatar || ''}
              sx={{
                width: 34,
                height: 34,
                bgcolor: ACCENT,
                fontSize: '0.85rem',
                fontWeight: 700
              }}
            >
              {/* Fallback to initials, or Account icon if still loading */}
              {user ? (
                user.avatar ? null : (
                  `${user?.firstName?.[0] || ''}${user?.lastName?.[0] || ''}`
                )
              ) : (
                <AccountCircleIcon sx={{ fontSize: 22 }} />
              )}
            </Avatar>
          </IconButton>
        </Tooltip>
      </Box>

      {/* Dropdown menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleCloseMenu}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
        slotProps={{
          paper: {
            elevation: 3,
            sx: {
              mt: 1,
              minWidth: 180,
              borderRadius: '12px',
              border: '1px solid rgba(0,0,0,0.06)',
              overflow: 'visible',
              '&::before': {
                content: '""',
                display: 'block',
                position: 'absolute',
                top: 0,
                right: 16,
                width: 10,
                height: 10,
                bgcolor: 'background.paper',
                transform: 'translateY(-50%) rotate(45deg)',
                zIndex: 0,
                borderTop: '1px solid rgba(0,0,0,0.06)',
                borderLeft: '1px solid rgba(0,0,0,0.06)'
              }
            }
          }
        }}
      >
        <MenuItem onClick={handleProfile} sx={{ gap: 1, borderRadius: '8px', mx: 0.5, my: 0.25 }}>
          <ListItemIcon>
            <PersonIcon fontSize='small' sx={{ color: ACCENT }} />
          </ListItemIcon>
          <Typography variant='body2' fontWeight={500}>Update Profile</Typography>
        </MenuItem>

        <Divider sx={{ my: 0.5 }} />

        <MenuItem onClick={handleLogout} sx={{ gap: 1, borderRadius: '8px', mx: 0.5, my: 0.25, color: '#ef4444' }}>
          <ListItemIcon>
            <LogoutIcon fontSize='small' sx={{ color: '#ef4444' }} />
          </ListItemIcon>
          <Typography variant='body2' fontWeight={500}>Log Out</Typography>
        </MenuItem>
      </Menu>
    </>
  );
}
