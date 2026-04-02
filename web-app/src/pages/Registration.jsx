import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import GitHubIcon from '@mui/icons-material/GitHub';
import GoogleIcon from '@mui/icons-material/Google';
import PersonAddAltIcon from '@mui/icons-material/PersonAddAlt';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import {
  Box,
  Button,
  CircularProgress,
  Divider,
  Grid,
  IconButton,
  InputAdornment,
  Link,
  TextField,
  Typography
} from '@mui/material';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import SnackbarUI from '../components/ui/Snackbar';
import { API } from '../configurations/configuration';
import httpClient from '../configurations/httpClient';

const ACCENT = '#1e293b';
const ACCENT_HOVER = '#0f172a';

export default function RegistrationPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: '',
    password: '',
    firstName: '',
    lastName: '',
    email: ''
  });
  const [showPassword, setShowPassword] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'info'
  });

  useEffect(() => {
    document.title = 'Create Account';
  }, []);

  const handleFieldChange = (field) => (e) => {
    setFormData((prev) => ({ ...prev, [field]: e.target.value }));
  };

  const handleCloseSnackbar = () => {
    setSnackbar((prev) => ({ ...prev, open: false }));
  };

  const showSnackbar = (message, severity) => {
    setSnackbar({ open: true, message, severity });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);

    try {
      const response = await httpClient.post(API.SIGN_UP, formData);
      const { data } = response;

      if (data.code === 1000) {
        showSnackbar('Account created successfully! Redirecting to login…', 'success');
        setTimeout(() => navigate('/login'), 1800);
      } else {
        showSnackbar(data.message || 'Registration failed. Please try again.', 'error');
      }
    } catch (error) {
      const errorMessage =
        error?.response?.data?.message || 'An unexpected error occurred. Please try again.';
      showSnackbar(errorMessage, 'error');
    } finally {
      setIsLoading(false);
    }
  };

  const handleGoogleSignUp = () => {
    alert('Google sign-up coming soon!');
  };

  const handleGitHubSignUp = () => {
    alert('GitHub sign-up coming soon!');
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
        <Box
          sx={{
            width: '100%',
            maxWidth: 480,
            display: 'flex',
            flexDirection: 'column',
            gap: 0
          }}
        >
          <Button
            startIcon={<ArrowBackIcon fontSize='small' />}
            onClick={() => navigate('/login')}
            sx={{
              alignSelf: 'flex-start',
              mb: 1.5,
              color: '#6b7280',
              textTransform: 'none',
              fontWeight: 500,
              fontSize: '0.875rem',
              px: 1,
              borderRadius: '8px',
              '&:hover': {
                bgcolor: 'rgba(0,0,0,0.05)',
                color: '#111827'
              }
            }}
          >
            Back to Login
          </Button>

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
                <PersonAddAltIcon sx={{ color: '#fff', fontSize: 20 }} />
              </Box>
              <Box>
                <Typography
                  component='h1'
                  sx={{ fontSize: '1.4rem', fontWeight: 700, color: '#111827', lineHeight: 1.2 }}
                >
                  Create your account
                </Typography>
                <Typography sx={{ fontSize: '0.8rem', color: '#9ca3af', mt: 0.3 }}>
                  Join _sudo.dan today
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: 'flex', gap: 1.5, mt: 3 }}>
              <Button
                fullWidth
                variant='outlined'
                startIcon={<GoogleIcon />}
                onClick={handleGoogleSignUp}
                sx={oauthButtonStyles}
              >
                Google
              </Button>
              <Button
                fullWidth
                variant='outlined'
                startIcon={<GitHubIcon />}
                onClick={handleGitHubSignUp}
                sx={oauthButtonStyles}
              >
                GitHub
              </Button>
            </Box>

            <Divider sx={{ my: 2.5 }}>
              <Typography sx={{ fontSize: '0.75rem', color: '#9ca3af', px: 1 }}>
                or sign up with email
              </Typography>
            </Divider>

            <Box component='form' onSubmit={handleSubmit} noValidate>
              <Grid container spacing={1.5}>
                <Grid item xs={6}>
                  <TextField
                    label='First Name'
                    fullWidth
                    required
                    size='small'
                    value={formData.firstName}
                    onChange={handleFieldChange('firstName')}
                    sx={inputStyles}
                  />
                </Grid>
                <Grid item xs={6}>
                  <TextField
                    label='Last Name'
                    fullWidth
                    required
                    size='small'
                    value={formData.lastName}
                    onChange={handleFieldChange('lastName')}
                    sx={inputStyles}
                  />
                </Grid>
              </Grid>

              <TextField
                label='Username'
                fullWidth
                required
                size='small'
                value={formData.username}
                onChange={handleFieldChange('username')}
                sx={{ ...inputStyles, mt: 1.5 }}
              />

              <TextField
                label='Email Address'
                type='email'
                fullWidth
                required
                size='small'
                value={formData.email}
                onChange={handleFieldChange('email')}
                sx={{ ...inputStyles, mt: 1.5 }}
              />

              <TextField
                label='Password'
                type={showPassword ? 'text' : 'password'}
                fullWidth
                required
                size='small'
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
                        {showPassword ? <VisibilityOff fontSize='small' /> : <Visibility fontSize='small' />}
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
                  'Create Account'
                )}
              </Button>
            </Box>

            <Typography
              sx={{ textAlign: 'center', mt: 2.5, color: '#6b7280', fontSize: '0.85rem' }}
            >
              Already have an account?{' '}
              <Link
                component='button'
                type='button'
                onClick={() => navigate('/login')}
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
                Sign in
              </Link>
            </Typography>
          </Box>
        </Box>
      </Box>
    </>
  );
}

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
