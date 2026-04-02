import ChatIcon from '@mui/icons-material/Chat';
import HomeIcon from '@mui/icons-material/Home';
import PeopleIcon from '@mui/icons-material/People';
import {
  Box,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Tooltip,
  Typography
} from '@mui/material';
import { useLocation, Link } from 'react-router-dom';

const ACCENT = '#1e293b';

const NAV_ITEMS = [
  { label: 'Home', icon: <HomeIcon />, to: '/' },
  { label: 'Friends', icon: <PeopleIcon />, to: '/friends' },
  { label: 'Chat', icon: <ChatIcon />, to: '/chat' }
];

export default function SideMenu({ open = true }) {
  const location = useLocation();

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
      <Toolbar />

      {/* Brand label on mobile */}
      <Box sx={{ px: 2, pb: 1, display: { sm: 'none' } }}>
        <Typography
          sx={{
            fontWeight: 800,
            fontSize: '1.1rem',
            color: ACCENT
          }}
        >
          _sudo.dan
        </Typography>
      </Box>

      <List sx={{ px: 1, pt: 1 }}>
        {NAV_ITEMS.map(({ label, icon, to }) => {
          const isActive = location.pathname === to;
          return (
            <ListItem key={label} disablePadding sx={{ mb: 0.5 }}>
              <ListItemButton
                component={Link}
                to={to}
                sx={{
                  borderRadius: '10px',
                  py: 1.1,
                  px: open ? 1.5 : 1,
                  justifyContent: open ? 'initial' : 'center',
                  bgcolor: isActive ? 'rgba(30,41,59,0.08)' : 'transparent',
                  '&:hover': {
                    bgcolor: isActive
                      ? 'rgba(30,41,59,0.12)'
                      : 'rgba(0,0,0,0.04)'
                  }
                }}
              >
                <Tooltip title={!open ? label : ''} placement='right'>
                  <ListItemIcon
                    sx={{
                      minWidth: open ? 38 : 0,
                      justifyContent: 'center',
                      color: isActive ? ACCENT : '#6b7280'
                    }}
                  >
                    {icon}
                  </ListItemIcon>
                </Tooltip>
                
                {open && (
                  <ListItemText
                    primary={label}
                    primaryTypographyProps={{
                      fontWeight: isActive ? 700 : 500,
                      fontSize: '0.9rem',
                      color: isActive ? ACCENT : '#374151',
                      whiteSpace: 'nowrap'
                    }}
                  />
                )}
                
                {isActive && open && (
                  <Box
                    sx={{
                      width: 4,
                      height: 20,
                      borderRadius: 2,
                      bgcolor: ACCENT
                    }}
                  />
                )}
              </ListItemButton>
            </ListItem>
          );
        })}
      </List>
    </Box>
  );
}
