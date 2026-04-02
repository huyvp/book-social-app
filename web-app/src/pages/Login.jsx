import GitHubIcon from '@mui/icons-material/GitHub';
import GoogleIcon from '@mui/icons-material/Google';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import {
  Box,
  Button,
  CircularProgress,
  Divider,
  IconButton,
  InputAdornment,
  Link,
  TextField,
  Typography
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import SnackbarUI from '../components/ui/Snackbar';
import { isAuthenticated, logIn } from '../services/authenticationService';

const ACCENT = '#1e293b';
const ACCENT_HOVER = '#0f172a';

export default function LoginPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({ username: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'error'
  });

  useEffect(() => {
    document.title = 'Sign In';
    if (isAuthenticated()) navigate('/');
  }, [navigate]);

  const handleFieldChange = (field) => (e) => {
    setFormData((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleCloseSnackbar = () => {
    setSnackbar((prev) => ({ ...prev, open: false }));
  };

  const showSnackbar = (message, severity = 'error') => {
    setSnackbar({ open: true, message, severity });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);

    try {
      await logIn(formData.username, formData.password);
      navigate('/');
    } catch (error) {
      const errorMessage =
        error?.response?.data?.message || 'Invalid credentials. Please try again.';
      showSnackbar(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleSignIn = () => {
    alert('Google sign-in coming soon!');
  };

  const handleGitHubSignIn = () => {
    alert('GitHub sign-in coming soon!');
  };

  return (
    <>
      <SnackbarUI
        open={snackbar.open}
        message={snackbar.message}
        onClose={handleCloseSnackbar}
        severity={snackbar.severity}
      />

      <Box
        sx={{
          minHeight: '100vh',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          bgcolor: '#f0f2f5',
          py: 4,
          px: 2
        }}
      >
        <Box sx={{ width: '100%', maxWidth: 420 }}>
          <Box
            sx={{
              bgcolor: '#fff',
              borderRadius: '20px',
              boxShadow: '0 4px 24px rgba(0,0,0,0.08)',
              p: { xs: 3, sm: 4 },
              border: '1px solid rgba(0,0,0,0.06)'
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, mb: 0.5 }}>
              <Box
                sx={{
                  width: 42,
                  height: 42,
                  borderRadius: '12px',
                  bgcolor: ACCENT,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  flexShrink: 0
                }}
              >
                <LockOutlinedIcon sx={{ color: '#fff', fontSize: 20 }} />
              </Box>
              <Box>
                <Typography
                  component='h1'
                  sx={{ fontSize: '1.4rem', fontWeight: 700, color: '#111827', lineHeight: 1.2 }}
                >
                  Welcome back
                </Typography>
                <Typography sx={{ fontSize: '0.8rem', color: '#9ca3af', mt: 0.3 }}>
                  Sign in to _sudo.dan
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: 'flex', gap: 1.5, mt: 3 }}>
              <Button
                fullWidth
                variant='outlined'
                startIcon={<GoogleIcon />}
                onClick={handleGoogleSignIn}
                sx={oauthButtonStyles}
              >
                Google
              </Button>
              <Button
                fullWidth
                variant='outlined'
                startIcon={<GitHubIcon />}
                onClick={handleGitHubSignIn}
                sx={oauthButtonStyles}
              >
                GitHub
              </Button>
            </Box>

            <Divider sx={{ my: 2.5 }}>
              <Typography sx={{ fontSize: '0.75rem', color: '#9ca3af', px: 1 }}>
                or sign in with email
              </Typography>
            </Divider>

            <Box component='form' onSubmit={handleSubmit} noValidate>
              <TextField
                label='Username'
                fullWidth
                required
                size='small'
                autoComplete='username'
                value={formData.username}
                onChange={handleFieldChange('username')}
                sx={inputStyles}
              />

              <TextField
                label='Password'
                type={showPassword ? 'text' : 'password'}
                fullWidth
                required
                size='small'
                autoComplete='current-password'
                value={formData.password}
                onChange={handleFieldChange('password')}
                sx={{ ...inputStyles, mt: 1.5 }}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position='end'>
                      <IconButton
                        onClick={() => setShowPassword((prev) => !prev)}
                        edge='end'
                        size='small'
                        sx={{ color: '#9ca3af', '&:hover': { color: '#374151' } }}
                      >
                        {showPassword ? (
                          <VisibilityOff fontSize='small' />
                        ) : (
                          <Visibility fontSize='small' />
                        )}
                      </IconButton>
                    </InputAdornment>
                  )
                }}
              />

              <Button
                type='submit'
                fullWidth
                disabled={isLoading}
                sx={{
                  mt: 2.5,
                  py: 1.3,
                  borderRadius: '10px',
                  bgcolor: ACCENT,
                  color: '#fff',
                  fontWeight: 600,
                  fontSize: '0.95rem',
                  textTransform: 'none',
                  letterSpacing: '0.2px',
                  boxShadow: '0 4px 14px rgba(30,41,59,0.3)',
                  transition: 'all 0.2s ease',
                  '&:hover': {
                    bgcolor: ACCENT_HOVER,
                    boxShadow: '0 6px 20px rgba(30,41,59,0.4)',
                    transform: 'translateY(-1px)'
                  },
                  '&:disabled': {
                    bgcolor: '#e5e7eb',
                    color: '#9ca3af',
                    boxShadow: 'none',
                    transform: 'none'
                  }
                }}
              >
                {isLoading ? (
                  <CircularProgress size={20} sx={{ color: '#9ca3af' }} />
                ) : (
                  'Sign In'
                )}
              </Button>
            </Box>

            <Typography
              sx={{ textAlign: 'center', mt: 2.5, color: '#6b7280', fontSize: '0.85rem' }}
            >
              Don't have an account?{' '}
              <Link
                component='button'
                type='button'
                onClick={() => navigate('/register')}
                underline='hover'
                sx={{
                  color: ACCENT,
                  fontWeight: 600,
                  cursor: 'pointer',
                  background: 'none',
                  border: 'none',
                  fontSize: 'inherit',
                  verticalAlign: 'baseline'
                }}
              >
                Create one
              </Link>
            </Typography>
          </Box>
        </Box>
      </Box>
    </>
  );
}

const oauthButtonStyles = {
  borderRadius: '10px',
  borderColor: '#e5e7eb',
  color: '#374151',
  textTransform: 'none',
  fontWeight: 500,
  fontSize: '0.875rem',
  py: 1.1,
  '&:hover': {
    borderColor: '#d1d5db',
    bgcolor: '#f9fafb'
  }
};

const inputStyles = {
  '& .MuiOutlinedInput-root': {
    borderRadius: '10px',
    '& fieldset': { borderColor: '#e5e7eb' },
    '&:hover fieldset': { borderColor: '#d1d5db' },
    '&.Mui-focused fieldset': { borderColor: ACCENT, borderWidth: '1.5px' }
  },
  '& .MuiInputLabel-root.Mui-focused': {
    color: ACCENT
  }
};
