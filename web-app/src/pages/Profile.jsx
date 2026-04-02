import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PhotoCameraIcon from '@mui/icons-material/PhotoCamera';
import {
  Avatar,
  Box,
  Button,
  CircularProgress,
  Divider,
  Grid,
  TextField,
  Tooltip,
  Typography
} from '@mui/material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import dayjs from 'dayjs';
import { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import SnackbarUI from '../components/ui/Snackbar';
import { isAuthenticated, logOut } from '../services/authenticationService';
import { getMyInfo, updateProfile, uploadAvatar } from '../services/userService';

const ACCENT = '#1e293b';
const ACCENT_HOVER = '#0f172a';

export default function Profile() {
  const navigate = useNavigate();
  const fileInputRef = useRef(null);

  const [userDetails, setUserDetails] = useState(null);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [city, setCity] = useState('');
  const [dob, setDob] = useState(null);

  const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
  const [uploading, setUploading] = useState(false);
  const [isUpdating, setIsUpdating] = useState(false);

  const showSnackbar = (message, severity = 'success') =>
    setSnackbar({ open: true, message, severity });

  const handleCloseSnackbar = () =>
    setSnackbar((prev) => ({ ...prev, open: false }));

  const getUserDetails = async () => {
    try {
      const response = await getMyInfo();
      const { result } = response.data;
      setUserDetails(result);
      setFirstName(result.firstName || '');
      setLastName(result.lastName || '');
      setEmail(result.email || '');
      setCity(result.city || '');
      setDob(result.dob ? dayjs(result.dob) : null);
    } catch (error) {
      if (error.response?.status === 401) {
        logOut();
        navigate('/login');
      }
    }
  };

  useEffect(() => {
    document.title = 'Update Profile';
    if (!isAuthenticated()) {
      navigate('/login');
    } else {
      getUserDetails();
    }
  }, [navigate]);

  const handleUpdate = async () => {
    setIsUpdating(true);
    try {
      const profileData = {
        firstName,
        lastName,
        email,
        city,
        dob: dob ? dob.format('YYYY-MM-DD') : null
      };

      await updateProfile(profileData);
      setUserDetails((prev) => ({ ...prev, ...profileData }));
      showSnackbar('Profile updated successfully!');
    } catch {
      showSnackbar('Failed to update profile. Please try again.', 'error');
    } finally {
      setIsUpdating(false);
    }
  };

  const handleAvatarClick = () => {
    if (!uploading) fileInputRef.current.click();
  };

  const handleFileSelect = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    if (!file.type.match('image.*')) {
      showSnackbar('Please select an image file', 'error');
      return;
    }

    try {
      setUploading(true);
      const formData = new FormData();
      formData.append('file', file);
      const response = await uploadAvatar(formData);
      const imageUrl = response.data.result.avatar;

      setUserDetails((prev) => ({ ...prev, avatar: imageUrl }));
      showSnackbar('Avatar updated successfully!');
    } catch {
      showSnackbar('Failed to upload avatar. Please try again.', 'error');
    } finally {
      setUploading(false);
      event.target.value = ''; // Reset input to allow re-uploading same file
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: '#f0f2f5', display: 'flex', flexDirection: 'column' }}>
      <SnackbarUI
        open={snackbar.open}
        onClose={handleCloseSnackbar}
        message={snackbar.message}
        severity={snackbar.severity}
      />

      {/* Simplified Top Navigation */}
      <Box
        sx={{
          height: 64,
          bgcolor: '#fff',
          borderBottom: '1px solid rgba(0,0,0,0.07)',
          display: 'flex',
          alignItems: 'center',
          px: { xs: 2, sm: 3 },
          position: 'sticky',
          top: 0,
          zIndex: 10
        }}
      >
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/')}
          sx={{
            color: '#4b5563',
            textTransform: 'none',
            fontWeight: 600,
            fontSize: '0.9rem',
            borderRadius: '8px',
            '&:hover': { bgcolor: 'rgba(0,0,0,0.04)', color: '#111827' }
          }}
        >
          Back
        </Button>
        <Box sx={{ flexGrow: 1 }} />
        <Typography fontWeight={800} fontSize='1.1rem' color={ACCENT} sx={{ userSelect: 'none' }}>
          _sudo.dan
        </Typography>
      </Box>

      {/* Main Content */}
      <Box sx={{ flexGrow: 1, py: { xs: 3, sm: 5 }, px: 2, display: 'flex', justifyContent: 'center' }}>
        <Box sx={{ width: '100%', maxWidth: 560 }}>
          {userDetails ? (
            <Box
              sx={{
                bgcolor: '#fff',
                borderRadius: '20px',
                boxShadow: '0 4px 24px rgba(0,0,0,0.06)',
                p: { xs: 3, sm: 5 },
                border: '1px solid rgba(0,0,0,0.06)'
              }}
            >
              {/* Header section: Avatar + Welcome */}
              <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mb: 4 }}>
                <Tooltip title='Click to update avatar' placement='right'>
                  <Box sx={{ position: 'relative', display: 'inline-block', mb: 2 }}>
                    <Avatar
                      src={userDetails.avatar}
                      onClick={handleAvatarClick}
                      sx={{
                        width: 100,
                        height: 100,
                        fontSize: '2.5rem',
                        bgcolor: ACCENT,
                        cursor: 'pointer',
                        transition: 'all 0.2s',
                        boxShadow: '0 4px 14px rgba(0,0,0,0.1)',
                        '&:hover': { opacity: 0.9, transform: 'scale(1.02)' }
                      }}
                    >
                      {userDetails.firstName?.[0]}
                      {userDetails.lastName?.[0]}
                    </Avatar>

                    {/* Hover overlay for camera icon */}
                    <Box
                      onClick={handleAvatarClick}
                      sx={{
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        right: 0,
                        bottom: 0,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        bgcolor: 'rgba(0, 0, 0, 0.4)',
                        borderRadius: '50%',
                        opacity: 0,
                        transition: 'opacity 0.2s',
                        cursor: 'pointer',
                        '&:hover': { opacity: 1 }
                      }}
                    >
                      <PhotoCameraIcon sx={{ color: '#fff', fontSize: 32 }} />
                    </Box>

                    {/* Upload spinner */}
                    {uploading && (
                      <Box
                        sx={{
                          position: 'absolute',
                          top: 0,
                          left: 0,
                          right: 0,
                          bottom: 0,
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                          bgcolor: 'rgba(0,0,0,0.5)',
                          borderRadius: '50%',
                          zIndex: 2
                        }}
                      >
                        <CircularProgress size={30} sx={{ color: '#fff' }} />
                      </Box>
                    )}
                  </Box>
                </Tooltip>

                <input
                  type='file'
                  accept='image/*'
                  ref={fileInputRef}
                  style={{ display: 'none' }}
                  onChange={handleFileSelect}
                />

                <Typography sx={{ fontSize: '1.4rem', fontWeight: 700, color: '#111827' }}>
                  {userDetails.firstName} {userDetails.lastName}
                </Typography>
                <Typography sx={{ fontSize: '0.9rem', color: '#6b7280' }}>
                  @{userDetails.username}
                </Typography>
              </Box>

              <Divider sx={{ mb: 3 }} />

              <Typography sx={{ fontWeight: 600, color: '#374151', mb: 2, fontSize: '1.1rem' }}>
                Personal Information
              </Typography>

              {/* Form Grid */}
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label='First Name'
                    fullWidth
                    size='small'
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    sx={inputStyles}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label='Last Name'
                    fullWidth
                    size='small'
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    sx={inputStyles}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    label='Email Address'
                    type='email'
                    fullWidth
                    size='small'
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    sx={inputStyles}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    label='City'
                    fullWidth
                    size='small'
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                    sx={inputStyles}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                      label='Date of Birth'
                      value={dob}
                      onChange={(newValue) => setDob(newValue)}
                      slotProps={{
                        textField: {
                          size: 'small',
                          fullWidth: true,
                          sx: inputStyles
                        }
                      }}
                    />
                  </LocalizationProvider>
                </Grid>
              </Grid>

              {/* Save Button */}
              <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 4 }}>
                <Button
                  variant='contained'
                  onClick={handleUpdate}
                  disabled={isUpdating}
                  sx={{
                    px: 4,
                    py: 1.2,
                    borderRadius: '10px',
                    bgcolor: ACCENT,
                    color: '#fff',
                    fontWeight: 600,
                    textTransform: 'none',
                    boxShadow: '0 4px 14px rgba(30,41,59,0.3)',
                    '&:hover': {
                      bgcolor: ACCENT_HOVER,
                      boxShadow: '0 6px 20px rgba(30,41,59,0.4)',
                    },
                    '&:disabled': {
                      bgcolor: '#e5e7eb',
                      color: '#9ca3af'
                    }
                  }}
                >
                  {isUpdating ? <CircularProgress size={22} sx={{ color: '#fff' }} /> : 'Save Changes'}
                </Button>
              </Box>
            </Box>
          ) : (
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                gap: 2,
                justifyContent: 'center',
                alignItems: 'center',
                height: 300,
                bgcolor: '#fff',
                borderRadius: '20px',
                border: '1px solid rgba(0,0,0,0.06)'
              }}
            >
              <CircularProgress size={30} sx={{ color: ACCENT }} />
              <Typography color='text.secondary'>Loading your profile…</Typography>
            </Box>
          )}
        </Box>
      </Box>
    </Box>
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
